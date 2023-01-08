package persistence;

import domain.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record JdbcCourseRepository(Connection connection) implements CourseRepository {

    @Override
    public List<Course> findAll() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM courses JOIN professors ON courses.professor_id = professors.professor_id");
        return getCourseList(rs);
    }

    @Override
    public List<Course> findAllByProfessor(Professor professor) throws SQLException {
        if (professor.getId() == null) {
            throw new IllegalArgumentException("Professor id cannot be null");
        }
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM courses JOIN professors ON courses.professor_id = professors.professor_id WHERE courses.professor_id = " + professor.getId());
        return getCourseList(rs);
    }

    @Override
    public Optional<Course> findById(int id) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * from courses JOIN professors ON courses.professor_id = professors.professor_id WHERE course_id =" + id);
        while(rs.next()) {
            int course_id = rs.getInt("course_id");
            char type_id = rs.getCharacterStream("type_id").toString().charAt(0);
            int professor_id = rs.getInt("professor_id");
            String last_name = rs.getString("last_name");
            String first_name = rs.getString("first_name");
            String description = rs.getString("description");
            LocalDate begin_date = rs.getDate("begin_date").toLocalDate();

            CourseType type = new CourseType(type_id, description);
            Professor professornew = new Professor(professor_id, last_name, first_name);
            return Optional.of(new Course(course_id, type, professornew, description, begin_date));
        }
        return Optional.empty();
    }

    @Override
    public Course save(Course course) throws SQLException {
        if (course.getId() != null) {
            throw new IllegalArgumentException("Course id must be null");
        } else if (LocalDate.now().isAfter(course.getBegin())) {
            throw new IllegalArgumentException("Course begin date must be in the future");
        }
        int key = 0;
        PreparedStatement ps = connection.prepareStatement("insert into courses (type_id, professor_id, description, begin_date) values (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, course.getType().getId() + "");
        ps.setInt(2, course.getProfessor().getId());
        ps.setString(3, course.getDescription());
        ps.setDate(4, Date.valueOf(course.getBegin()));
        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        if (rs != null && rs.next()) {
            key = rs.getInt(1);
        }
        return new Course(key, course.getType(), course.getProfessor(), course.getDescription(), course.getBegin());
    }

    @Override
    public List<Course> findAllByStudent(Student student) throws SQLException {
        if (student.getId() == null) {
            throw new IllegalArgumentException("Student id is not set");
        }
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM courses_students JOIN courses ON courses.course_id = courses_students.course_id" +
                " join professors on courses.professor_id = professors.professor_id WHERE student_id = " + student.getId());
       return getCourseList(rs);
    }

    @Override
    public void enrollInCourse(Student student, Course course) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("insert into courses_students (course_id, student_id) values (?, ?)");
        ps.setInt(1, course.getId());
        ps.setInt(2, student.getId());
        ps.executeUpdate();
    }

    @Override
    public void unenrollFromCourse(Student student, Course course) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("delete from courses_students where course_id = ? and student_id = ?");
        ps.setInt(1, course.getId());
        ps.setInt(2, student.getId());
        if (ps.executeUpdate() == 0) {
            throw new IllegalArgumentException("Student is not enrolled in this course");
        }
        ps.executeUpdate();
    }

    private List<Course> getCourseList (ResultSet rs) throws SQLException {
        List<Course> courses = new ArrayList<>();
        while (rs.next()) {
            int course_id = rs.getInt("course_id");
            char type_id = rs.getCharacterStream("type_id").toString().charAt(0);
            int professor_id = rs.getInt("professor_id");
            String last_name = rs.getString("last_name");
            String first_name = rs.getString("first_name");
            String description = rs.getString("description");
            LocalDate begin_date = rs.getDate("begin_date").toLocalDate();
            CourseType type = new CourseType(type_id, description);
            Professor professornew = new Professor(professor_id, last_name, first_name);
            courses.add(new Course(course_id, type, professornew, description, begin_date));
        }
        return courses;
    }
}
