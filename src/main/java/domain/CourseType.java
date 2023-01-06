package domain;


public class CourseType {

    private final char id;
    private final String description;

    public CourseType(char id, String description) {
        if (description.isBlank())
            throw new IllegalArgumentException();
        this.id = id;
        this.description = description;
    }

    public char getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }
}
