package pt.ipleiria.estg.dei.ei.dae.academics.entities;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllTeachers",
                query = "SELECT t " +
                        "FROM Teacher t " +
                        "ORDER BY t.name" // JPQL
        )
})
public class Teacher extends User implements Serializable {
    String office;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "teachers")
    List<Subject> subjects;

    public Teacher() {
        this.subjects = new LinkedList<Subject>();
    }

    public Teacher(String username, String password, String name, String email, String office) {
        super(username, password, name, email);
        this.office = office;
        this.subjects = new LinkedList<Subject>();
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
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
