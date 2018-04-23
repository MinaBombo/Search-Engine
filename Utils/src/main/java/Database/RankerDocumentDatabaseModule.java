package Database;

import BusinessModel.RankerDocument;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class RankerDocumentDatabaseModule {


    DatabaseConnector connector;

    RankerDocumentDatabaseModule(DatabaseConnector connector) {
        this.connector = connector;
    }

    List<RankerDocument> getRankerDocuments() throws  SQLException{
        List<RankerDocument> rankerDocuments = getDocumentIDS();
        for(RankerDocument rankerDocument : rankerDocuments){
            getCountAndRankByDocumentID(rankerDocument);
            getOutboundDocuments(rankerDocument);
        }
        return rankerDocuments;
    }
    List<RankerDocument> getDocumentIDS() throws SQLException {
        List<RankerDocument> rankerDocuments = new ArrayList<>();
        String sqlStatement = "SELECT id from " + DatabaseColumn.DOCUMENT;
        Statement statement = connector.getPooledConnection().createStatement();
        ResultSet resultSet = statement.executeQuery(sqlStatement);
        while (resultSet.next()) {
            Integer id = resultSet.getInt(1);
            rankerDocuments.add(new RankerDocument(id));
        }
        resultSet.close();
        statement.close();
        return rankerDocuments;
    }

    void getCountAndRankByDocumentID(RankerDocument rankerDocument) throws  SQLException {
        String sqlStatement = "SELECT (SELECT COUNT(*) from " + DatabaseColumn.LINK + " WHERE docuemntid = ?) AS Count,(SELECT rank FROM " + DatabaseColumn.DOCUMENT + " WHERE id = ?) AS rank";
        PreparedStatement statement = connector.getPooledConnection().prepareStatement(sqlStatement);
        statement.setInt(1,rankerDocument.getId());
        statement.setInt(2,rankerDocument.getId());
        ResultSet resultSet = statement.executeQuery();
        if(resultSet.next()){
            rankerDocument.setOutLinks(resultSet.getInt(1));
            rankerDocument.setRank(resultSet.getDouble(2));
        }
        resultSet.close();
        statement.close();
    }

    void getOutboundDocuments(RankerDocument rankerDocument) throws SQLException{
        String sqlStatement = "SELECT docuemntid  FROM "+ DatabaseColumn.LINK+" WHERE RefrencedLink IN (SELECT url from "+ DatabaseColumn.DOCUMENT+ " WHERE id = ?)";
        PreparedStatement statement = connector.getPooledConnection().prepareStatement(sqlStatement);
        statement.setInt(1,rankerDocument.getId());
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()){
            rankerDocument.getInBoundDocuments().add(resultSet.getInt(1));
        }
        resultSet.close();
        statement.close();
    }

    void updateDocumentRank(List<RankerDocument> documents) throws SQLException{
        String sqlStatement = "UPDATE "+ DatabaseColumn.DOCUMENT+" SET Rank = ?  WHERE id = ?";
        PreparedStatement statement = connector.getPooledConnection().prepareStatement(sqlStatement);
        for(RankerDocument document : documents){
            statement.setDouble(1,document.getRank());
            statement.setInt(2,document.getId());
            statement.addBatch();
        }
        statement.executeBatch();
        statement.close();
    }
}
