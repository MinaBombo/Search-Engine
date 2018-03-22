package Database;

import Indexer.Word;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class WordListDatabaseModule implements DatabaseModule<List<Word>> {
    private DataBaseConnector connector;

    WordListDatabaseModule(DataBaseConnector connector) {
        this.connector = connector;
    }

    @Override
    public void insert(List<Word> words) throws SQLException {
        String sqlStatement = "INSERT INTO " + DatabaseColumn.WORD.toString() + " (Text,DocumentID) VALUES (?,?)";
        PreparedStatement statement = connector.getConnection().prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
        for (Word word : words) {
            statement.setString(1, word.getText());
            statement.setInt(2, word.getDocument().getId());
            statement.addBatch();
        }
        statement.executeBatch();
        ResultSet resultSet = statement.getGeneratedKeys();
        int wordCounter = 0;
        while (resultSet.next()) {
            words.get(wordCounter++).setId(resultSet.getInt(1));
        }
    }

    @Override
    public void update(List<Word> words) throws SQLException {
        String sqlStatement = "UPDATE " + DatabaseColumn.WORD.toString() + " SET Text = ? , DocumentID = ? WHERE ID = ?";
        PreparedStatement statement = connector.getConnection().prepareStatement(sqlStatement);
        for (Word word : words) {
            statement.setString(1, word.getText());
            statement.setInt(2, word.getDocument().getId());
            statement.setInt(3, word.getId());
            statement.addBatch();
        }
        statement.executeBatch();
        statement.close();

    }

    @Override
    public void delete(List<Word> words) throws SQLException {
        String sqlStatement = "DELETE FROM " + DatabaseColumn.WORD.toString() + " WHERE ID = ?";
        PreparedStatement statement = connector.getConnection().prepareStatement(sqlStatement);
        for (Word word : words) {
            statement.setInt(1, word.getId());
            statement.addBatch();
        }
        statement.executeBatch();
        statement.close();

    }
}
