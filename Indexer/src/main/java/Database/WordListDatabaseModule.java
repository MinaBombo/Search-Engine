package Database;

import Indexer.Document;
import Indexer.Word;
import org.postgresql.PGConnection;
import org.postgresql.copy.CopyIn;
import org.postgresql.copy.CopyManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
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
        String sqlStatement = "COPY " + DatabaseColumn.WORD + "(text , documentID) FROM STDIN WITH (FORMAT TEXT, DELIMITER '\t')";
        Connection connection = connector.getNonPooledConnection();
        CopyManager manager = ((PGConnection) connection).getCopyAPI();
        StringBuilder builder = new StringBuilder();
        CharSequence forbidden = "\\.";
        for (Word word : words) {
            //if(!word.getText().contains(forbidden))
            builder.append(word.getText()).append('\t').append(word.getDocument().getId()).append('\n');
        }
        BufferedReader reader = new BufferedReader(new StringReader(builder.toString()));
        try{
            manager.copyIn(sqlStatement,reader);
        }
        catch (IOException  | SQLException exception){
            System.err.println("Error while Copying words");
            exception.printStackTrace();
        }
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
