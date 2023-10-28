package pt.ipleiria.estg.dei.ei.dae.academics.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Course;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Student;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Subject;

import java.util.List;

@Stateless
public class StudentBean {

    @PersistenceContext
    private EntityManager entityManager;

    public void create(String username, String password, String name, String email, long courseCode) {
        Course foundCourse = entityManager.find(Course.class, courseCode);

        if (foundCourse != null) {
            var student = new Student(username, password, name, email, foundCourse);
            foundCourse.addStudent(student);
            entityManager.persist(student);
        }
    }

    public Student find(String username) {
        return entityManager.find(Student.class, username);
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
}
