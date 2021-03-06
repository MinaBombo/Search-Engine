package Database;

import BusinessModel.Word;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class WordDatabaseModule implements DatabaseModule<Word> {
    private DatabaseConnector connector;

    public WordDatabaseModule(DatabaseConnector connector) {
        this.connector = connector;
    }

    @Override
    public void insert(Word word) throws SQLException {
        String sqlStatement = "INSERT INTO " + DatabaseColumn.WORD.toString() + " (Text,DocumentID) VALUES (?,?)";
        PreparedStatement statement = connector.getPooledConnection().prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, word.getText());
        statement.setInt(2, word.getDocument().getId());
        int affectedRows = statement.executeUpdate();
        if (affectedRows > 0) {
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                word.setId(resultSet.getInt(1));
            }
        }
        statement.close();
    }

    @Override
    public void update(Word word) throws SQLException {
        String sqlStatement = "UPDATE " + DatabaseColumn.WORD.toString() + " SET Text = ? , DocumentID = ? WHERE ID = ?";
        PreparedStatement statement = connector.getPooledConnection().prepareStatement(sqlStatement);
        statement.setString(1, word.getText());
        statement.setInt(2, word.getDocument().getId());
        statement.setInt(3, word.getId());
        statement.executeUpdate();
        statement.close();
    }

    @Override
    public void delete(Word word) throws SQLException {
        String sqlStatement = "DELETE FROM " + DatabaseColumn.WORD.toString() + " WHERE ID = ?";
        PreparedStatement statement = connector.getPooledConnection().prepareStatement(sqlStatement);
            statement.setInt(1, word.getId());
            statement.executeUpdate();
            statement.close();
        }

    @Override
    public List<Word> select() throws SQLException {
        return null;
    }
}
