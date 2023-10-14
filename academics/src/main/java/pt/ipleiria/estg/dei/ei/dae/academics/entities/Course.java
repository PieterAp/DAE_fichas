package pt.ipleiria.estg.dei.ei.dae.academics.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.LinkedList;
import java.util.List;

@Entity
@Table(
    name = "courses",
    uniqueConstraints = @UniqueConstraint(columnNames = {"name"})
)
@NamedQueries({
    @NamedQuery(
        name = "getAllCourses",
        query = "SELECT c FROM Course c ORDER BY c.name" // JPQL
    )
})
public class Course {
    @Id
    long code;

    @NotNull
    String name;

    @OneToMany(mappedBy = "course", cascade = CascadeType.DETACH)
    List<Student> students;

    public Course() {
        this.students = new LinkedList<Student>();
    }

    public Course(long code, String name) {
        this.code = code;
        this.name = name;
        this.students = new LinkedList<Student>();
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

    public void addStudent(Student student) {
        this.students.add(student);
    }

    public void removeStudent(Student student) {
        this.students.remove(student);
    }
}
