package Database;

import Indexer.Document;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DocumentDatabaseModule implements DatabaseModule<Document> {

    DataBaseConnector connector;

    DocumentDatabaseModule(DataBaseConnector connector) {
        this.connector = connector;
    }

    @Override
    public void insert(Document document) throws SQLException {
        String sqlStatement = "INSERT INTO " + DatabaseColumn.DOCUMENT.toString() + "(Path,URL) VALUES (?,?)";
        PreparedStatement statement = connector.getConnection().prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, document.getPath());
        statement.setString(2, document.getUrl());
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
        String sqlStatement = "UPDATE " + DatabaseColumn.DOCUMENT.toString() + " SET Path = ? ,URL = ? WHERE ID = ?";
        PreparedStatement statement = connector.getConnection().prepareStatement(sqlStatement);
        statement.setString(1, document.getPath());
        statement.setString(2, document.getUrl());
        statement.setInt(3, document.getId());
        statement.executeUpdate();
        statement.close();
    }

    @Override
    public void delete(Document document) throws SQLException {
        String sqlStatement = "DELETE FROM " + DatabaseColumn.DOCUMENT.toString() + " WHERE ID = ?";
        PreparedStatement statement = connector.getConnection().prepareStatement(sqlStatement);
        statement.setInt(1, document.getId());
        statement.executeUpdate();
        statement.close();
    }
}
