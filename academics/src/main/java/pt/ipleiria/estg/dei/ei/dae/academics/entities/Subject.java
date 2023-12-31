package pt.ipleiria.estg.dei.ei.dae.academics.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

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
@SQLDelete(sql="UPDATE subjects SET deleted = TRUE WHERE code = ? AND version = ?")
@Where(clause = "deleted IS FALSE")
public class Subject implements Serializable {
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

    @ManyToMany
    @JoinTable(
            name = "subjects_teachers",
            joinColumns = @JoinColumn(
                    name = "subject_code",
                    referencedColumnName = "code"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "teacher_username",
                    referencedColumnName = "username"
            )
    )
    List<Teacher> teachers;

    @Version
    private int version;

    private boolean deleted;

    private Date deleted_at;

    public Subject() {
        this.students = new LinkedList<Student>();
        this.teachers = new LinkedList<Teacher>();
    }

    public Subject(long code, String name, Course course, long courseYear, String scholarYear) {
        this.code = code;
        this.name = name;
        this.course = course;
        this.courseYear = courseYear;
        this.scholarYear = scholarYear;
        this.students = new LinkedList<Student>();
        this.teachers = new LinkedList<Teacher>();
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

    public List<Teacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<Teacher> teachers) {
        this.teachers = teachers;
    }

    public void addTeacher(Teacher teacher) {
        if (this.teachers.contains(teacher)) {
            return;
        }

        this.teachers.add(teacher);
    }

    public void removeTeacher(Teacher teacher) {
        if (!this.teachers.contains(teacher)) {
            return;
        }

        this.teachers.remove(teacher);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subject subject = (Subject) o;
        return code == subject.code;
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Date getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(Date deleted_at) {
        this.deleted_at = deleted_at;
    }
}
