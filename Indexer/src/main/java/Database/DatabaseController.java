package Database;

import Indexer.Document;
import Indexer.Word;

import java.io.Closeable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class DatabaseController implements Closeable {
    DataBaseConnector connector;

    public DatabaseController() throws SQLException {
        connector = new DataBaseConnector();
    }

    @Override
    public void close() {
        connector.close();
    }

    public void insertDocument(Document document) {
        String sqlStatement = "INSERT INTO " + DatabaseColumn.DOCUMENT.toString() + "(Path,URL) VALUES (?,?)";
        try (PreparedStatement statement = connector.getConnection().prepareStatement(sqlStatement,
                Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, document.getPath());
            statement.setString(2, document.getUrl());
            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                ResultSet resultSet = statement.getGeneratedKeys();
                while (resultSet.next()) {
                    document.setId(resultSet.getInt(1));
                }
            }
        } catch (SQLException exception) {
            handleException(exception, "Error in Inserting document");
        }
    }

    public void updateDocument(Document document) {
        String sqlStatement = "UPDATE " + DatabaseColumn.DOCUMENT.toString() + " SET Path = ? ,URL = ? WHERE ID = ?";
        try (PreparedStatement statement = connector.getConnection().prepareStatement(sqlStatement)) {
            statement.setString(1, document.getPath());
            statement.setString(2, document.getUrl());
            statement.setInt(3, document.getId());
            statement.executeUpdate();
        } catch (SQLException exception) {
            handleException(exception, "Error in updating document");
        }
    }

    public void deleteDocument(Document document) {
        String sqlStatement = "DELETE FROM " + DatabaseColumn.DOCUMENT.toString() + " WHERE ID = ?";
        try (PreparedStatement statement = connector.getConnection().prepareStatement(sqlStatement)) {
            statement.setInt(1, document.getId());
            statement.executeUpdate();

        } catch (SQLException exception) {
            handleException(exception, "Error in deleting document");
        }
    }

    public void insertWord(Word word) {
        String sqlStatement = "INSERT INTO " + DatabaseColumn.WORD.toString() + " (Text,DocumentID) VALUES (?,?)";
        try (PreparedStatement statement = connector.getConnection().prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, word.getText());
            statement.setInt(2, word.getDocument().getId());
            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                ResultSet resultSet = statement.getGeneratedKeys();
                if (resultSet.next()) {
                    word.setId(resultSet.getInt(1));
                }
            }
        } catch (SQLException exception) {
            handleException(exception, "Error in inserting new word");
        }
    }

    public void updateWord(Word word) {
        String sqlStatement = "UPDATE " + DatabaseColumn.WORD.toString() + " SET Text = ? , DocumentID = ? WHERE ID = ?";
        try (PreparedStatement statement = connector.getConnection().prepareStatement(sqlStatement)) {
            statement.setString(1, word.getText());
            statement.setInt(2, word.getDocument().getId());
            statement.setInt(3, word.getId());
            statement.executeUpdate();
        } catch (SQLException exception) {
            handleException(exception, "Error in updating word");
        }
    }

    public void deleteWord(Word word) {
        String sqlStatement = "DELETE FROM " + DatabaseColumn.WORD.toString() + " WHERE ID = ?";
        try (PreparedStatement statement = connector.getConnection().prepareStatement(sqlStatement)) {
            statement.setInt(1, word.getId());
            statement.executeUpdate();
        } catch (SQLException exception) {
            handleException(exception, "Error in deleting word");
        }
    }

    public void insertWords(List<Word> words) {
        String sqlStatement = "INSERT INTO " + DatabaseColumn.WORD.toString() + " (Text,DocumentID) VALUES (?,?)";
        try (PreparedStatement statement = connector.getConnection().prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS)) {
            for (Word word : words) {
                statement.setString(1, word.getText());
                statement.setInt(2, word.getDocument().getId());
                statement.addBatch();
            }
            statement.executeBatch();
            ResultSet resultSet = statement.getGeneratedKeys();
            int wordCounter = 0;
            while (resultSet.next()){
                words.get(wordCounter++).setId(resultSet.getInt(1));
            }
        } catch (SQLException exception) {
            handleException(exception, "Error in inserting a list of words");
        }
    }
    public void updateWords(List<Word> words){
        String sqlStatement = "UPDATE " + DatabaseColumn.WORD.toString() + " SET Text = ? , DocumentID = ? WHERE ID = ?";
        try(PreparedStatement statement = connector.getConnection().prepareStatement(sqlStatement)){
            for(Word word: words){
                statement.setString(1,word.getText());
                statement.setInt(2,word.getDocument().getId());
                statement.setInt(3,word.getId());
                statement.addBatch();
            }
            statement.executeBatch();
        }
        catch (SQLException exception){
            handleException(exception,"Error in updating list of words");
        }
    }

    public void deleteWords(List<Word> words){
        String sqlStatement = "DELETE FROM " + DatabaseColumn.WORD.toString() + " WHERE ID = ?";
        try (PreparedStatement statement = connector.getConnection().prepareStatement(sqlStatement)) {
            for (Word word : words){
                statement.setInt(1,word.getId());
                statement.addBatch();
            }
            statement.executeBatch();
        }
        catch (SQLException exception){
            handleException(exception,"Error in deleting list of words");
        }
    }
    private void handleException(SQLException exception, String message) {
        System.err.println(message);
        System.err.println(exception.getSQLState());
        System.err.println(exception.getMessage());
    }

}
