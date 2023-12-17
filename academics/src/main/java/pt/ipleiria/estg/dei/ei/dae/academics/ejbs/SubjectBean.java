package pt.ipleiria.estg.dei.ei.dae.academics.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Course;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Student;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Subject;

import java.util.Date;
import java.util.List;

@Stateless
public class SubjectBean {

    @PersistenceContext
    private EntityManager entityManager;

    public void create(long code, String name, long courseCode, long courseYear, String scholarYear) {
        Course foundCourse = entityManager.find(Course.class, courseCode);

        if (foundCourse != null) {
            var subject = new Subject(code, name, foundCourse, courseYear, scholarYear);
            foundCourse.addSubject(subject);
            entityManager.persist(subject);
        }
    }

    public List<Subject> getAllSubjects() {
        return entityManager.createNamedQuery("getAllSubjects", Subject.class).getResultList();
    }

    public Subject find(long subjectCode) {
        return entityManager.find(Subject.class, subjectCode);
    }

    public Subject findStudentsInSubjects(long subjectCode) {
        Subject subject = find(subjectCode);

        // subject not found
        if (subject == null) {
            return null;
        }

        // lazy load students from subject
        Hibernate.initialize(subject.getStudents());
        return subject;
    }

    public Subject findTeachersInSubjects(long subjectCode) {
        Subject subject = find(subjectCode);

        // subject not found
        if (subject == null) {
            return null;
        }

        // lazy load students from subject
        Hibernate.initialize(subject.getTeachers());
        return subject;
    }

    public void updateSubject(long code, String name, long courseYear, String scholarYear) {
        Subject subject = entityManager.find(Subject.class, code);
        subject.setName(name);
        subject.setCourseYear(courseYear);
        subject.setScholarYear(scholarYear);
        entityManager.merge(subject);
    }

    public void deleteSubject(long code) {
        Subject subject = entityManager.find(Subject.class, code);
        subject.setDeleted_at(new Date());
        entityManager.persist(subject);
        entityManager.flush();
        entityManager.remove(subject);
    }

}
