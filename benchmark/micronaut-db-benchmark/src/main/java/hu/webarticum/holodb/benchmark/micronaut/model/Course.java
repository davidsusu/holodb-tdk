package hu.webarticum.holodb.benchmark.micronaut.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Serdeable
@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="school_year", nullable = false)
    private Integer schoolYear;

    @Column(nullable = false)
    private Integer term;
    
    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @JsonIgnore
    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
        name = "course_student", 
        joinColumns = { @JoinColumn(name = "course_id") }, 
        inverseJoinColumns = { @JoinColumn(name = "student_id") }
    )
    private List<Student> students;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getSchoolYear() {
        return schoolYear;
    }

    public void setSchoolYear(int schoolYear) {
        this.schoolYear = schoolYear;
    }

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setTerm(Subject subject) {
        this.subject = subject;
    }

    public List<Student> getStudents() {
        return new ArrayList<>(students);
    }

    public void setCourses(List<Student> students) {
        this.students = new ArrayList<>(students);
    }

}
