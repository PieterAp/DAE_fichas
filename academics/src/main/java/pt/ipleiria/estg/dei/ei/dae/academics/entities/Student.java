package pt.ipleiria.estg.dei.ei.dae.academics.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

@Entity
@Table(name = "students")
@NamedQueries({
    @NamedQuery(
        name = "getAllStudents",
        query = "SELECT s FROM Student s ORDER BY s.name" // JPQL
    )
})
public class Student implements Serializable {
    @Id
    String username;

    @NotNull
    String password;

    @NotNull
    String name;

    @NotNull
    @Email
    String email;

    @ManyToOne
    @JoinColumn(name = "course_code")
    @NotNull
    Course course;

    public Student() {

    }

    public Student(String username, String password, String name, String email, Course course) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.course = course;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
