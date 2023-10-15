package pt.ipleiria.estg.dei.ei.dae.academics.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Course;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Subject;

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

}
