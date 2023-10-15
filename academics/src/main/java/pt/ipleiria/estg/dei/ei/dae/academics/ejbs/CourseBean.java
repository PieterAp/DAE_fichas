package pt.ipleiria.estg.dei.ei.dae.academics.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Course;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Student;

import java.util.List;

@Stateless
public class CourseBean {

    @PersistenceContext
    private EntityManager entityManager;

    public void create(long code, String name) {
        var course = new Course(code, name);
        entityManager.persist(course);
    }

    public Course find(long courseCode) {
        return entityManager.find(Course.class, courseCode);
    }

    public List<Course> getAllCourses() {
        return entityManager.createNamedQuery("getAllCourses", Course.class).getResultList();
    }
}
