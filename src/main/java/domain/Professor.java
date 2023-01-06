package domain;


public class Professor {

    private final Integer id;
    private String lastName;
    private String firstName;

    public Professor(Integer id, String lastName, String firstName) {
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
}
