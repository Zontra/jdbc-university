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
        List<Course> courses = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM courses");
        while (rs.next()) {
            int course_id = rs.getInt("course_id");
            int type_id = rs.getInt("type_id");
            int professor_id = rs.getInt("professor_id");
            int description = rs.getInt("description");
            LocalDate begin_date = rs.getDate("begin_date").toLocalDate();
            courses.add(new Course(course_id, type_id, professor_id, description, begin_date));
        }
        return courses;
    }

    @Override
    public List<Course> findAllByProfessor(Professor professor) throws SQLException {
        List<Course> courses = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM courses WHERE professor_id = " + professor.getId());
        while (rs.next()) {
            int course_id = rs.getInt("course_id");
            int type_id = rs.getInt("type_id");
            int professor_id = rs.getInt("professor_id");
            int description = rs.getInt("description");
            LocalDate begin_date = rs.getDate("begin_date").toLocalDate();
            courses.add(new Course(course_id, type_id, professor_id, description, begin_date));
        }

        return courses;
    }

    @Override
    public Optional<Course> findById(int id) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * from course WHERE course_id =" + id);
        while(rs.next()) {
            int course_id = rs.getInt("course_id");
            int type_id = rs.getInt("type_id");
            int professor_id = rs.getInt("professor_id");
            int description = rs.getInt("description");
            LocalDate begin_date = rs.getDate("begin_date").toLocalDate();
            return Optional.of(new Course(course_id, type_id, professor_id, description, begin_date));
        }
        return Optional.empty();
    }

    @Override
    public Course save(Course course) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("insert into course (course_id, type_id, professor_id, description, begin_date) values (?, ?, ?, ?, ?)");

        ps.setInt(1, course.getId());
        ps.setObject(2, course.getType());
        ps.setObject(3, course.getProfessor());
        ps.setObject(4, course.getDescription());
        ps.setDate(5, Date.valueOf(course.getBegin()));
        ps.executeUpdate();

        return course;
    }

    @Override
    public List<Course> findAllByStudent(Student student) throws SQLException {
        List<Course> courses = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM courses_students WHERE student_id = " + student.getId());
        while (rs.next()) {
            int course_id = rs.getInt("course_id");
            int type_id = rs.getInt("type_id");
            int professor_id = rs.getInt("professor_id");
            int description = rs.getInt("description");
            LocalDate begin_date = rs.getDate("begin_date").toLocalDate();
            courses.add(new Course(course_id, type_id, professor_id, description, begin_date));
        }
        return null;
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
        ps.executeUpdate();
    }
}
