package pt.ipleiria.estg.dei.ei.dae.academics.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Course;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Student;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Subject;

import java.util.List;
import java.util.Objects;

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
        try {
            return entityManager.createNamedQuery("getStudentWithSubjects", Student.class)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
            //todo: not best approach, can lead to 2 scenarios:
            // - User does not exist
            // - User does exist but has no subjects
        }
    }

    public List<Student> getAll() {
        // remember, maps to: “SELECT s FROM Student s ORDER BY s.name”
        return entityManager.createNamedQuery("getAllStudents", Student.class).getResultList();
    }

    public void enrollStudentInSubject (String username, long subjectCode) {
        Student foundStudent = entityManager.find(Student.class, username);
        Subject foundSubject = entityManager.find(Subject.class, subjectCode);

        if (foundStudent != null && foundSubject != null) {
            if (foundStudent.getCourse().equals(foundSubject.getCourse())) {
                foundSubject.addStudent(foundStudent);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentBean that = (StudentBean) o;
        return Objects.equals(entityManager, that.entityManager);
    }

    @Override
    public int hashCode() {
        return Objects.hash(entityManager);
    }
}
