package persistence;

import domain.Professor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record JdbcProfessorRepository(Connection connection) implements ProfessorRepository {

    @Override
    public List<Professor> findAll() throws SQLException {
        List<Professor> professors = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM professors");
        while(rs.next()) {
            int professor_id = rs.getInt("professor_id");
            String last_name = rs.getString("last_name");
            String first_name = rs.getString("first_name");
            professors.add(new Professor(professor_id, last_name, first_name));
        }
        return professors;
    }

    @Override
    public Optional<Professor> findById(int id) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * from professors WHERE professor_id =" + id);
        while(rs.next()) {
            int professor_id = rs.getInt("professor_id");
            String last_name = rs.getString("last_name");
            String first_name = rs.getString("first_name");
            return Optional.of(new Professor(professor_id, last_name, first_name));
        }
        return Optional.empty();
    }

    @Override
    public Professor save(Professor professor) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("insert into professors(professor_id, last_name, first_name) values (?, ?, ?)");
        ps.setInt(1, professor.getId());
        ps.setString(2, professor.getLastName());
        ps.setString(3, professor.getFirstName());
        ps.executeQuery();
        return professor;
    }

    @Override
    public void delete(Professor professor) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("DELETE FROM professors WHERE professor_id = ?");
        ps.setInt(1, professor.getId());
        ps.executeQuery();
    }
}
