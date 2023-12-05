package pt.ipleiria.estg.dei.ei.dae.academics.ejbs;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Course;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Student;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Subject;
import pt.ipleiria.estg.dei.ei.dae.academics.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.academics.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.academics.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.academics.security.Hasher;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Stateless
public class StudentBean {

    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    private Hasher hasher;

    public boolean exists(String username) {
        Query query = entityManager.createQuery(
                "SELECT COUNT(s.username) FROM Student s WHERE s.username = :username",
                Long.class
        );
        query.setParameter("username", username);
        return (Long)query.getSingleResult() > 0L;
    }

    public void create(String username, String password, String name, String email, long courseCode) throws MyEntityExistsException, MyEntityNotFoundException, MyConstraintViolationException {
        if (exists(username)) {
            throw new MyEntityExistsException("Student with username '" + username + "' already exists");
        }

        Course foundCourse = entityManager.find(Course.class, courseCode);
        if (foundCourse == null) {
            throw new MyEntityNotFoundException("Course with code '" + courseCode + "' not found");
        }

        try {
            Student student = new Student(username, hasher.hash(password), name, email, foundCourse);
            foundCourse.addStudent(student);
            entityManager.persist(student);
            entityManager.flush(); // when using Hibernate, to force it to throw a
            // ContraintViolationException, as in the JPA specification
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(e);
        }
    }

    public Student find(String username) {
        return entityManager.find(Student.class, username);
    }

    public Student findWithSubjects(String username) throws MyEntityNotFoundException {
        Student student = entityManager.find(Student.class, username);
        if (student == null) {
            throw new MyEntityNotFoundException("Student with username '" + username + "' not found");
        }

        Hibernate.initialize(student.getSubjects());
        return student;
    }

    public Student findStudentWithSubjects(String username) {
        Student student = find(username);

        // student not found
        if (student == null) {
            return null;
        }

        // student has no subjects
        /*
        if (student.getSubjects().isEmpty()) {
            return new Student();
        }
         */

        // lazy load subjects from student
        Hibernate.initialize(student.getSubjects());
        return student;
    }

    public List<Subject> subjectsAvailable (String username) {
        Student student = find(username);
        Course studentCourse = student.getCourse();

        Hibernate.initialize(student.getSubjects());
        Hibernate.initialize(studentCourse.getSubjects());

        List<Subject> studentSubjects = student.getSubjects();
        List<Subject> courseSubjects = new LinkedList<>(studentCourse.getSubjects());
        courseSubjects.removeAll(studentSubjects);

        return courseSubjects;
    }

    public List<Student> getAll() {
        return entityManager.createNamedQuery("getAllStudents", Student.class).getResultList();
    }

    public boolean enrollStudentInSubject (String username, long subjectCode) {
        Student foundStudent = entityManager.find(Student.class, username);
        Subject foundSubject = entityManager.find(Subject.class, subjectCode);

        if (foundStudent != null && foundSubject != null) {
            if (foundStudent.getCourse().equals(foundSubject.getCourse())) {
                foundSubject.addStudent(foundStudent);
                foundStudent.addSubject(foundSubject); //important! to make sure this is kept in memory!
                return true;
            }
            return false;
        }
        return false;
    }

    public boolean unrollStudentFromSubject (String username, long subjectCode) {
        Student foundStudent = entityManager.find(Student.class, username);
        Subject foundSubject = entityManager.find(Subject.class, subjectCode);

        if (foundStudent != null && foundSubject != null) {
            if (foundStudent.getSubjects().contains(foundSubject)) {
                foundSubject.removeStudent(foundStudent);
                foundStudent.removeSubject(foundSubject); //important! to make sure this is kept in memory!
                return true;
            }
            return false;
        }
        return false;
    }

    public void updateStudent(String username, String email, String name, String password, long courseCode) {
        Student student = entityManager.find(Student.class, username);

        if (student == null) {
            System.err.println("ERROR_STUDENT_NOT_FOUND: " + username);
            return;
        }

        entityManager.lock(student, LockModeType.OPTIMISTIC);
        student.setEmail(email);
        student.setName(name);
        if (password!=null) {
            student.setPassword(hasher.hash(password));
        }

        //update student course
        if (student.getCourse().getCode() != courseCode) {
            Course course = entityManager.find(Course.class, courseCode);
            if (course == null) {
                System.err.println("ERROR_COURSE_NOT_FOUND: " + courseCode);
                return;
            }

            //unroll student from subjects from previous course
            Hibernate.initialize(student.getSubjects());
            List<Subject> subjects = new LinkedList<Subject>(student.getSubjects());

            for (Subject subject : subjects) {
                unrollStudentFromSubject(student.getUsername(), subject.getCode());
            }

            student.setCourse(course);
        }

        entityManager.merge(student);
    }

    public void deleteStudent(String username) {
        Student student = entityManager.find(Student.class, username);
        entityManager.remove(student);
    }
}
