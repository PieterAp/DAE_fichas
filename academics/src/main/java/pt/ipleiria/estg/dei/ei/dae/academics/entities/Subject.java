package pt.ipleiria.estg.dei.ei.dae.academics.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.LinkedList;
import java.util.List;

@Entity
@Table(
    name = "subjects",
    uniqueConstraints = @UniqueConstraint(columnNames = {"name","course_code","scholar_year"})
)
@NamedQueries({
    @NamedQuery(
        name = "getAllSubjects",
        query = "SELECT s " +
                "FROM Subject s " +
                "ORDER BY course.name ASC, scholarYear DESC, courseYear ASC, name ASC" // JPQL
    )
})
public class Subject {
    @Id
    long code;

    @NotNull
    String name;

    @ManyToOne
    @JoinColumn(name = "course_code")
    Course course;

    @NotNull
    @Column(name = "course_year")
    long courseYear;

    @NotNull
    @Column(name = "scholar_year")
    String scholarYear;

    @ManyToMany
    @JoinTable(
        name = "subjects_students",
        joinColumns = @JoinColumn(
            name = "subject_code",
            referencedColumnName = "code"
        ),
        inverseJoinColumns = @JoinColumn(
            name = "student_username",
            referencedColumnName = "username"
        )
    )
    List<Student> students;

    public Subject() {

    }

    public Subject(long code, String name, Course course, long courseYear, String scholarYear) {
        this.code = code;
        this.name = name;
        this.course = course;
        this.courseYear = courseYear;
        this.scholarYear = scholarYear;
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

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public long getCourseYear() {
        return courseYear;
    }

    public void setCourseYear(long courseYear) {
        this.courseYear = courseYear;
    }

    public String getScholarYear() {
        return scholarYear;
    }

    public void setScholarYear(String scholarYear) {
        this.scholarYear = scholarYear;
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
