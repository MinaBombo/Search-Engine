package Database;

import BusinessModel.Document;
import org.postgresql.core.SqlCommandType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DocumentDatabaseModule implements DatabaseModule<Document> {

    DatabaseConnector connector;

    DocumentDatabaseModule(DatabaseConnector connector) {
        this.connector = connector;
    }

    @Override
    public void insert(Document document) throws SQLException {
        String sqlStatement = "INSERT INTO " + DatabaseColumn.DOCUMENT.toString() + "(Name,URL,Processed) VALUES (?,?,?)";
        PreparedStatement statement = connector.getPooledConnection().prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, document.getName());
        statement.setString(2, document.getUrl());
        statement.setBoolean(3,document.isProcessed());
        int affectedRows = statement.executeUpdate();
        if (affectedRows > 0) {
            ResultSet resultSet = statement.getGeneratedKeys();
            while (resultSet.next()) {
                document.setId(resultSet.getInt(1));
            }
        }
        statement.close();
    }

    @Override
    public void update(Document document) throws SQLException {
        String sqlStatement = "UPDATE " + DatabaseColumn.DOCUMENT.toString() + " SET Name = ? ,URL = ? ,Processed = ? WHERE ID = ?";
        PreparedStatement statement = connector.getPooledConnection().prepareStatement(sqlStatement);
        statement.setString(1, document.getName());
        statement.setString(2, document.getUrl());
        statement.setBoolean(3,document.isProcessed());
        statement.setInt(4, document.getId());
        statement.executeUpdate();
        statement.close();
    }

    @Override
    public void delete(Document document) throws SQLException {
        String sqlStatement = "DELETE FROM " + DatabaseColumn.DOCUMENT.toString() + " WHERE ID = ?";
        PreparedStatement statement = connector.getPooledConnection().prepareStatement(sqlStatement);
        statement.setInt(1, document.getId());
        statement.executeUpdate();
        statement.close();
    }

    @Override
    public List<Document> select() throws SQLException {
       String sqlStatement = "SELECT ID, Name, URL , Processed FROM "+DatabaseColumn.DOCUMENT.toString()+" WHERE Processed = False";
       Statement statement = connector.getPooledConnection().createStatement();
       ResultSet resultSet = statement.executeQuery(sqlStatement);
       List<Document> documents = new ArrayList<>();
       while (resultSet.next()){
           documents.add(new Document(resultSet.getInt(1),resultSet.getString(2),resultSet.getString(3),resultSet.getBoolean(4)));
       }
       resultSet.close();
       statement.close();
       return documents;

    }

    public Integer getNumDocuments()throws SQLException{
        String sqlStatement = "SELECT COUNT(*) FROM "+DatabaseColumn.DOCUMENT;
        Statement statement = connector.getPooledConnection().createStatement();
        ResultSet resultSet = statement.executeQuery(sqlStatement);
        Integer numDocuments;
        if(resultSet.next())
        {
            numDocuments = resultSet.getInt(1);
        }
        else
        {
            //Should never reach here
            throw new SQLException("No Documents Yet");
        }
        resultSet.close();
        statement.close();
        return numDocuments;
    }
}
