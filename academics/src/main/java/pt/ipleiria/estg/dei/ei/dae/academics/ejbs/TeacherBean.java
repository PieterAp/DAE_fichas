package pt.ipleiria.estg.dei.ei.dae.academics.ejbs;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Subject;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Teacher;
import pt.ipleiria.estg.dei.ei.dae.academics.security.Hasher;

import java.util.LinkedList;
import java.util.List;

@Stateless
public class TeacherBean {

    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    private Hasher hasher;

    public void create(String username, String password, String name, String email, String office) {
        Teacher teacher = new Teacher(username, hasher.hash(password), name, email, office);
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

    public boolean dissociateTeacherFromSubject(String username, long subjectCode) {
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

    public void updateTeacher(String username, String email, String name, String password, String office) {
        Teacher teacher = entityManager.find(Teacher.class, username);
        teacher.setEmail(email);
        teacher.setName(name);
        teacher.setPassword(password);
        teacher.setOffice(office);
        entityManager.merge(teacher);
    }

    public void deleteTeacher(String username) {
        Teacher teacher = entityManager.find(Teacher.class, username);

        Hibernate.initialize(teacher.getSubjects());
        List<Subject> teacherSubjects = new LinkedList<>(teacher.getSubjects());
        for (Subject subject: teacherSubjects) {
            dissociateTeacherFromSubject(username,subject.getCode());
        }

        entityManager.remove(teacher);
    }
}
