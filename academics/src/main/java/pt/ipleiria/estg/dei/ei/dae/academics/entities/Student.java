package pt.ipleiria.estg.dei.ei.dae.academics.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllStudents",
                query = "SELECT s " +
                        "FROM Student s " +
                        "ORDER BY s.name" // JPQL
        )
})
public class Student extends User implements Serializable {

    @ManyToOne
    @JoinColumn(name = "course_code")
    @NotNull
    Course course;

    @ManyToMany(mappedBy = "students")
    List<Subject> subjects;


    public Student() {
        this.subjects = new LinkedList<Subject>();
    }

    public Student(String username, String password, String name, String email, Course course) {
        super(username, password, name, email);
        this.course = course;
        this.subjects = new LinkedList<Subject>();
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }

    public void addSubject (Subject subject) {
        if (this.subjects.contains(subject)) {
            return;
        }

        this.subjects.add(subject);
    }

    public void removeSubject (Subject subject) {
        if (!this.subjects.contains(subject)) {
            return;
        }

        this.subjects.remove(subject);
    }
}
