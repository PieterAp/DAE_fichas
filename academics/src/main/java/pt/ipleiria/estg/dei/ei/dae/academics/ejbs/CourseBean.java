package pt.ipleiria.estg.dei.ei.dae.academics.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Course;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Student;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Subject;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Teacher;

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

    public List<Subject> getSubjects(long courseCode) {
        Course course = entityManager.find(Course.class, courseCode);
        Hibernate.initialize(course.getSubjects());

        return course.getSubjects();
    }

    public List<Student> getStudents(long courseCode) {
        Course course = entityManager.find(Course.class, courseCode);
        Hibernate.initialize(course.getStudents());

        return course.getStudents();
    }

    public List<Course> getAllCourses() {
        return entityManager.createNamedQuery("getAllCourses", Course.class).getResultList();
    }

    public boolean deleteCourse(long courseCode) {
        Course course = entityManager.find(Course.class,courseCode);
        entityManager.remove(course);
        //entityManager.flush();

        //todo: take care of this conflict later on

        if (entityManager.find(Course.class, courseCode) == null) {
            return true; //was deleted
        }
        return false; //was not deleted
    }

    public void updateCourse (long courseCodeID, String name) {
        Course course = find(courseCodeID);
        course.setName(name);
        entityManager.merge(course); //boa prática
    }
}
