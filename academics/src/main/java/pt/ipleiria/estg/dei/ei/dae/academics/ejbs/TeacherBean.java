package pt.ipleiria.estg.dei.ei.dae.academics.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Student;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Subject;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Teacher;

import java.util.List;

@Stateless
public class TeacherBean {

    @PersistenceContext
    private EntityManager entityManager;

    public void create(String username, String password, String name, String email, String office) {
        Teacher teacher = new Teacher(username, password, name, email, office);
        entityManager.persist(teacher);
    }

    public Teacher find(String username) {
        return entityManager.find(Teacher.class, username);
    }

    public Teacher findTeacherWithSubjects(String username) {
        Teacher teacher = find(username);

        // teacher not found
        if (teacher == null) {
            return null;
        }

        // lazy load subjects from teacher
        Hibernate.initialize(teacher.getSubjects());
        return teacher;
    }

    public List<Teacher> getAll() {
        return entityManager.createNamedQuery("getAllTeachers", Teacher.class).getResultList();
    }

    public boolean associateTeacherToSubject (String username, long subjectCode) {
        Teacher foundTeacher = entityManager.find(Teacher.class, username);
        Subject foundSubject = entityManager.find(Subject.class, subjectCode);

        if (foundTeacher != null && foundSubject != null) {
            foundTeacher.addSubject(foundSubject);
            foundSubject.addTeacher(foundTeacher);
            return true;
        }
        return false;
    }

    public boolean dissociateTeacherToSubject (String username, long subjectCode) {
        Teacher foundTeacher = entityManager.find(Teacher.class, username);
        Subject foundSubject = entityManager.find(Subject.class, subjectCode);

        if (foundTeacher != null && foundSubject != null) {
            if (foundTeacher.getSubjects().contains(foundSubject)) {
                foundTeacher.removeSubject(foundSubject);
                foundSubject.removeTeacher(foundTeacher);
                return true;
            }
            return false;
        }
        return false;
    }

    public void updateTeacher(String username, String email, String name, String password) {
        Student student = entityManager.find(Student.class, username);
        student.setEmail(email);
        student.setName(name);
        student.setPassword(password);
        entityManager.merge(student);
    }

    public void deleteTeacher(String username) {
        Teacher teacher = entityManager.find(Teacher.class, username);
        entityManager.remove(teacher);
    }
}
