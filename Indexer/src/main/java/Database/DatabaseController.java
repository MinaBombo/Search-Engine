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
    DocumentDatabaseModule documentModule;
    WordDatabaseModule wordModule;
    WordListDatabaseModule wordsModule;
    public DatabaseController() throws SQLException {
        connector = new DataBaseConnector();
        documentModule = new DocumentDatabaseModule(connector);
        wordModule = new WordDatabaseModule(connector);
        wordsModule = new WordListDatabaseModule(connector);
    }

    @Override
    public void close() {
        connector.close();
    }

    public void insertDocument(Document document) {
        try{
            documentModule.insert(document);
        }
        catch (SQLException exception) {
            handleSQLException(exception, "Error in Inserting document");
        }
    }

    public void updateDocument(Document document) {
        try{
            documentModule.update(document);
        }
        catch (SQLException exception) {
            handleSQLException(exception, "Error in updating document");
        }
    }

    public void deleteDocument(Document document) {
       try{
           documentModule.delete(document);
        } catch (SQLException exception) {
            handleSQLException(exception, "Error in deleting document");
        }
    }

    public void insertWord(Word word) {
        try{
            wordModule.insert(word);
        } catch (SQLException exception) {
            handleSQLException(exception, "Error in inserting new word");
        }
    }

    public void updateWord(Word word) {
       try{
           wordModule.update(word);
        } catch (SQLException exception) {
            handleSQLException(exception, "Error in updating word");
        }
    }

    public void deleteWord(Word word) {
        try{
            wordModule.delete(word);
        } catch (SQLException exception) {
            handleSQLException(exception, "Error in deleting word");
        }
    }

    public void insertWords(List<Word> words) {
        try{
            wordsModule.insert(words);
        } catch (SQLException exception) {
            handleSQLException(exception, "Error in inserting a list of words");
        }
    }
    public void updateWords(List<Word> words){
        try{
            wordsModule.update(words);
        }
        catch (SQLException exception){
            handleSQLException(exception,"Error in updating list of words");
        }
    }

    public void deleteWords(List<Word> words){
        try{
            wordsModule.delete(words);
        }
        catch (SQLException exception){
            handleSQLException(exception,"Error in deleting list of words");
        }
    }
    private void handleSQLException(SQLException exception, String message) {
        System.err.println(message);
        System.err.println(exception.getSQLState());
        System.err.println(exception.getMessage());
    }

}
