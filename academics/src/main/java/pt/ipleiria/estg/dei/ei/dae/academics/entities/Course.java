package pt.ipleiria.estg.dei.ei.dae.academics.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(
        name = "courses",
        uniqueConstraints = @UniqueConstraint(columnNames = {"name"})
)
@NamedQueries({
        @NamedQuery(
                name = "getAllCourses",
                query = "SELECT c " +
                        "FROM Course c " +
                        "ORDER BY c.name" // JPQL
        )
})
public class Course {
    @Id
    long code;

    @NotNull
    String name;

    // orphanRemoval = true
    @OneToMany(mappedBy = "course", cascade = CascadeType.REMOVE)
    List<Student> students;

    // orphanRemoval = true
    @OneToMany(mappedBy = "course", cascade = CascadeType.REMOVE)
    List<Subject> subjects;

    public Course() {
        this.students = new LinkedList<Student>();
        this.subjects = new LinkedList<Subject>();
    }

    public Course(long code, String name) {
        this.code = code;
        this.name = name;
        this.students = new LinkedList<Student>();
        this.subjects = new LinkedList<Subject>();
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }

    public void addStudent(Student student) {
        if (this.students.contains(student)) {
            return;
        }

        this.students.add(student);
    }

    public void removeStudent(Student student) {
        if (!this.students.contains(student)) {
            return;
        }

        this.students.remove(student);
    }

    public void addSubject (Subject subject) {
        if (this.subjects.contains(subject)) {
            return;
        }

        this.subjects.add(subject);
    }

    public void removeSubject (Subject subject) {
        if (this.subjects.contains(subject)) {
            return;
        }

        this.subjects.remove(subject);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return code == course.code && Objects.equals(name, course.name) && Objects.equals(students, course.students) && Objects.equals(subjects, course.subjects);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, name, students, subjects);
    }
}
