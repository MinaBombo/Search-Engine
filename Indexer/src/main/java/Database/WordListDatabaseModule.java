package Database;

import Indexer.Document;
import Indexer.Word;
import org.postgresql.PGConnection;
import org.postgresql.copy.CopyIn;
import org.postgresql.copy.CopyManager;

import java.nio.charset.Charset;
import java.sql.*;
import java.util.List;

public class WordListDatabaseModule implements DatabaseModule<List<Word>> {
    private DatabaseConnector connector;

    WordListDatabaseModule(DatabaseConnector connector) {
        this.connector = connector;
    }

    @Override
    public void insert(List<Word> words) throws SQLException {
        String sqlStatement = "INSERT INTO " + DatabaseColumn.WORD.toString() + " (Text,DocumentID) VALUES (?,?)";
        PreparedStatement statement = connector.getPooledConnection().prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
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

    public void copy(List<Word> words) throws SQLException {
        String sqlStatement = "COPY " + DatabaseColumn.WORD + "(text , documentID) FROM STDIN";
        Connection connection = connector.getNonPooledConnection();
        CopyManager manager = ((PGConnection) connection).getCopyAPI();
        StringBuilder builder = new StringBuilder();
        CopyIn copyIn = manager.copyIn(sqlStatement);
        for (Word word : words) {
            builder.append(word.getText()).append('\t').append(word.getDocument().getId()).append('\n');
        }
        byte[] bytes = builder.toString().getBytes(Charset.forName("UTF-8"));
        copyIn.writeToCopy(bytes, 0, bytes.length);
        copyIn.endCopy();
        connection.close();
    }

    @Override
    public void update(List<Word> words) throws SQLException {
        String sqlStatement = "UPDATE " + DatabaseColumn.WORD.toString() + " SET Text = ? , DocumentID = ? WHERE ID = ?";
        PreparedStatement statement = connector.getPooledConnection().prepareStatement(sqlStatement);
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
        PreparedStatement statement = connector.getPooledConnection().prepareStatement(sqlStatement);
        for (Word word : words) {
            statement.setInt(1, word.getId());
            statement.addBatch();
        }
        statement.executeBatch();
        statement.close();

    }

    @Override
    public List<List<Word>> select() throws SQLException {
        return null;
    }
}
