package persistence;

import domain.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record JdbcStudentRepository(Connection connection) implements StudentRepository {

    @Override
    public List<Student> findAll() throws SQLException {
        List<Student> students = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM students");
        while(rs.next()) {
            int student_id = rs.getInt("student_id");
            String first_name = rs.getString("first_name");
            String last_name = rs.getString("last_name");
            students.add(new Student(student_id, last_name, first_name));
        }
        return students;
    }

    @Override
    public Optional<Student> findById(int id) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM students WHERE student_id = '" + id + "'");
        while(rs.next()) {
            int student_id = rs.getInt("student_id");
            String first_name = rs.getString("first_name");
            String last_name = rs.getString("last_name");
            return Optional.of(new Student(student_id, last_name, first_name));
        }
        return Optional.empty();
    }

    @Override
    public Student save(Student student) throws SQLException {
        int key = 0;
        if(student.getId()!=null){
            throw new IllegalArgumentException("Student already exists");
        }
        PreparedStatement ps = connection.prepareStatement("insert into students(last_name, first_name) values (?, ?)", Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, student.getLastName());
        ps.setString(2, student.getFirstName());
        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        if (rs != null && rs.next()) {
            key = rs.getInt(1);
        }
        return new Student(key, student.getLastName(), student.getFirstName());
    }

    @Override
    public void update(Student student) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("UPDATE students set last_name = ?, first_name = ? WHERE student_id = ?");
        ps.setString(1, student.getLastName());
        ps.setString(2, student.getFirstName());
        ps.setInt(3, student.getId());
        ps.executeUpdate();
        if (ps.executeUpdate() == 0) {
            throw new SQLException("Updating student failed, no rows affected.");
        }
        if (ps.executeUpdate() == 0) {
            throw new SQLException("Student not found");
        }
    }

    @Override
    public void delete(Student student) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("DELETE FROM students WHERE student_id = ?");
        ps.setInt(1, student.getId());
        ps.executeUpdate();
    }
}
