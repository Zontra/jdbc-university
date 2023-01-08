package domain;

import java.time.LocalDate;
import java.util.Objects;

public class Course {

    private final Integer id;
    private final CourseType type;
    private final Professor professor;
    private final String description;
    private final LocalDate begin;

    public Course(Integer id, CourseType type, Professor professor, String description, LocalDate begin) {
        if (type == null || professor == null || description.isBlank() || begin == null)
            throw new IllegalArgumentException();
        this.id = id;
        this.type = type;
        this.professor = professor;
        this.description = description;
        this.begin = begin;
    }

    public Course(CourseType type, Professor professor, String description, LocalDate begin) {
        this(null, type, professor, description, begin);
    }

    public Integer getId() {
        return id;
    }

    public CourseType getType() {
        return type;
    }

    public Professor getProfessor() {
        return professor;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getBegin() {
        return begin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return Objects.equals(getId(), course.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
