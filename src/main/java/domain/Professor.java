package domain;


import java.util.Objects;

public class Professor {

    private final Integer id;
    private String lastName;
    private String firstName;

    public Professor(Integer id, String lastName, String firstName) {
        if (lastName.isBlank() || firstName.isBlank())
            throw new IllegalArgumentException();
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Professor(String lastName, String firstName) {
        this(null, lastName, firstName);
    }

    public Integer getId() {
        return id;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Professor professor = (Professor) o;
        return getId().equals(professor.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
