package Database;

import BusinessModel.BrowserDocument;
import BusinessModel.DynamicRankerDocument;
import BusinessModel.StaticRankerDocument;

import java.sql.*;
import java.util.*;

public class RankerDocumentDatabaseModule {


    DatabaseConnector connector;

    RankerDocumentDatabaseModule(DatabaseConnector connector) {
        this.connector = connector;
    }

    void updateDocumentRank(List<StaticRankerDocument> documents) throws SQLException {
        String sqlStatement = "UPDATE " + DatabaseColumn.DOCUMENT + " SET Rank = ?  WHERE id = ?";
        PreparedStatement statement = connector.getPooledConnection().prepareStatement(sqlStatement);
        for (StaticRankerDocument document : documents) {
            statement.setDouble(1, document.getRank());
            statement.setInt(2, document.getId());
            statement.addBatch();
        }
        statement.executeBatch();
        statement.close();
    }

    List<StaticRankerDocument> getStaticRankerDocuments() throws SQLException {
        String sqlStatement = "WITH Count AS (SELECT docuemntid , COUNT(*) FROM " + DatabaseColumn.LINK + " GROUP BY docuemntid ORDER BY docuemntid)," +
                " Rank AS (SELECT  id, rank from " + DatabaseColumn.DOCUMENT + " ORDER BY id)," +
                " OutBoundLinks AS (SELECT d.id, array_agg(l.docuemntid) FROM " + DatabaseColumn.LINK + " l INNER JOIN " + DatabaseColumn.DOCUMENT + " d on l.refrencedlink = d.url GROUP BY d.id ORDER BY d.id)" +
                " SELECT Count.docuemntid,Count.Count,Rank.Rank,OutBoundLinks.array_agg FROM Count INNER JOIN Rank ON Count.docuemntid = Rank.id INNER JOIN OutBoundLinks ON Count.docuemntid = OutBoundLinks.id";
        Statement statement = connector.getPooledConnection().createStatement();
        ResultSet resultSet = statement.executeQuery(sqlStatement);
        List<StaticRankerDocument> staticRankerDocuments = new ArrayList<>();
        while (resultSet.next()) {
            StaticRankerDocument currentStaticRankerDocument = new StaticRankerDocument(resultSet.getInt(1), resultSet.getInt(2), resultSet.getDouble(3));
            Integer[] outBoundLinks = (Integer[]) resultSet.getArray(4).getArray();
            for (Integer link : outBoundLinks) {
                currentStaticRankerDocument.getInBoundDocuments().add(link);
            }
            staticRankerDocuments.add(currentStaticRankerDocument);
        }
        resultSet.close();
        statement.close();
        return staticRankerDocuments;
    }

    List<DynamicRankerDocument> getDynamicRankerDocuments(String [] searchWords) throws SQLException {
        String sqlStatement = "WITH word AS (SELECT documentid AS id, sum(count) AS length, array_agg(text) AS Words, array_agg(count) AS Count FROM " + DatabaseColumn.WORD + " WHERE Text = ANY (?) GROUP BY documentid ORDER BY documentid),\n" +
                " Rank AS (SELECT id, rank from " + DatabaseColumn.DOCUMENT + " ORDER BY id)\n" +
                " SELECT word.id, Rank.rank, word.length, word.words, word.Count FROM word INNER JOIN Rank on word.id = Rank.id;";
        Connection connection = connector.getPooledConnection();
        PreparedStatement statement = connection.prepareStatement(sqlStatement);
        statement.setArray(1,connection.createArrayOf("TEXT",searchWords));
        ResultSet resultSet = statement.executeQuery();
        List<DynamicRankerDocument> documents = new ArrayList<>();
        while (resultSet.next()) {
            DynamicRankerDocument dynamicRankerDocument = new DynamicRankerDocument(resultSet.getInt(1), resultSet.getInt(3), resultSet.getDouble(2));
            String[] words = (String[]) resultSet.getArray(4).getArray();
            Integer[] count = (Integer[]) resultSet.getArray(5).getArray();
            if (words.length != count.length)
                System.err.println("Impossible, The query must be wrong");
            for (int i = 0; i < words.length; ++i) {
                dynamicRankerDocument.setNormWordFreq(words[i], count[i]);
            }
            documents.add(dynamicRankerDocument);
        }
        resultSet.close();
        statement.close();
        return documents;
    }

    Map<String, Integer> getWordMap(String [] searchWords) throws SQLException{
        Map<String,Integer> wordMap = new HashMap<>();
        String sqlStatement = "SELECT text,COUNT(*) FROM " +DatabaseColumn.WORD+" WHERE Text = ANY (?) GROUP BY text";
        Connection connection = connector.getPooledConnection();
        PreparedStatement statement = connection.prepareStatement(sqlStatement);
        statement.setArray(1,connection.createArrayOf("TEXT",searchWords));
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()){
            wordMap.put(resultSet.getString(1),resultSet.getInt(2));
        }
        resultSet.close();
        statement.close();
        return wordMap;
    }
    List<BrowserDocument> getSortedBrowserDocuments(List<DynamicRankerDocument> rankerDocuments) throws SQLException{
        String sqlStatement = "SELECT id, url, name , Description from document WHERE id = ANY (?)";
        Connection connection = connector.getPooledConnection();
        PreparedStatement statement =  connection.prepareStatement(sqlStatement);
        Integer documentIDS[] = new Integer[rankerDocuments.size()];
        int counter = 0;
        for(DynamicRankerDocument dynamicRankerDocument : rankerDocuments){
            documentIDS[counter++] = dynamicRankerDocument.getId();
        }
        statement.setArray(1,connection.createArrayOf("INTEGER",documentIDS));
        ResultSet resultSet = statement.executeQuery();
        Map<Integer,BrowserDocument> browserDocumentHashMap = new HashMap<>(rankerDocuments.size());
        while (resultSet.next()){
            browserDocumentHashMap.put(resultSet.getInt(1),new BrowserDocument(resultSet.getString(3),resultSet.getString(2),resultSet.getInt(1),resultSet.getString(4)));
        }
        resultSet.close();
        statement.close();
        List<BrowserDocument> browserDocuments = new ArrayList<>();
        for(DynamicRankerDocument rankerDocument: rankerDocuments){
            browserDocuments.add(browserDocumentHashMap.get(rankerDocument.getId()));
        }
        return browserDocuments;

    }
}
