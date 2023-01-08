package domain;


import java.util.Objects;

public class Student {

    private final Integer id;
    private String lastName;
    private String firstName;

    public Student(Integer id, String lastName, String firstName) {
        if (lastName.isBlank() || firstName.isBlank())
            throw new IllegalArgumentException();
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
    }

    public Student(String lastName, String firstName) {
        this(null, lastName, firstName);
    }

    public Integer getId() {
        return id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return getId().equals(student.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
