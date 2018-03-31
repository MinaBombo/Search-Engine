package Database;

import BusinessModel.Seed;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SeedDatabaseModule implements DatabaseModule<List<Seed>> {
    DatabaseConnector connector;
    SeedDatabaseModule(DatabaseConnector connector) {
        this.connector = connector;
    }


    @Override
    public void update(List<Seed> seeds) throws SQLException {

    }

    public void update(Seed seed) throws SQLException {
        String sqlStatement = "UPDATE " + DatabaseColumn.SEED + " SET URL = ?, Processed = ? , InLinks = ?  WHERE ID = ?";
        PreparedStatement statement = connector.getPooledConnection().prepareStatement(sqlStatement);
        statement.setString(1, seed.getUrl());
        statement.setBoolean(2, seed.isProcessed());
        statement.setInt(3, seed.getInLinks());
        statement.setInt(4, seed.getId());
        statement.executeUpdate();
        statement.close();
    }
    public void advancedUpdate(Seed seed) throws SQLException{
        String duplicateKeyErrorState = "23505";
        try{
            update(seed);
        }
        catch (SQLException exception){
            if(exception.getSQLState().equals(duplicateKeyErrorState)){
                //System.err.println("Duplicate Key value, resolving");
                seed.setInLinks(seed.getInLinks()+deleteSeedWithURL(seed)+1);
                delete(seed);
                insert(seed);
            }
            else throw  exception;
        }
    }
    public void insert(Seed seed) throws SQLException{
        String sqlStatement = "INSERT INTO " + DatabaseColumn.SEED + "(URL, Processed , InLinks) VALUES (?,?,?) ON CONFLICT (URL) DO UPDATE SET InLinks = EXCLUDED.InLinks+1";
        PreparedStatement statement = connector.getPooledConnection().prepareStatement(sqlStatement,Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, seed.getUrl());
        statement.setBoolean(2, seed.isProcessed());
        statement.setInt(3, seed.getInLinks());
        statement.executeUpdate();
        ResultSet resultSet =statement.getGeneratedKeys();
        if(resultSet.next()){
            seed.setId(resultSet.getInt(1));
        }
        resultSet.close();
        statement.close();

    }
    public int deleteSeedWithURL(Seed seed){
        String sqlStatement = "DELETE FROM " +DatabaseColumn.SEED + " WHERE URL = ? RETURNING InLinks";
        int inLinkCount = 0;
        try(PreparedStatement statement = connector.getPooledConnection().prepareStatement(sqlStatement)) {
            statement.setString(1, seed.getUrl());
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                inLinkCount = resultSet.getInt(1);
            }
        }
        catch (SQLException exception){
            System.err.println("Error while deleting seed with URL");
        }
        return inLinkCount;
    }

    @Override
    public void insert(List<Seed> seeds) throws SQLException {
        String sqlStatement = "INSERT INTO " + DatabaseColumn.SEED + "(URL, Processed , InLinks) VALUES (?,?,?) ON CONFLICT (URL) DO UPDATE SET InLinks = EXCLUDED.InLinks+1";
        PreparedStatement statement = connector.getPooledConnection().prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
        for (Seed seed : seeds) {
            statement.setString(1, seed.getUrl());
            statement.setBoolean(2, seed.isProcessed());
            statement.setInt(3, seed.getInLinks());
            statement.addBatch();
        }

        statement.executeBatch();
        ResultSet resultSet = statement.getGeneratedKeys();
        int counter = 0;
        while (resultSet.next()) {
            seeds.get(counter++).setId(resultSet.getInt(1));
        }
        resultSet.close();
        statement.close();
    }

    public List<Seed> select(int limit, int offset) throws SQLException {
        List<Seed> seeds = new ArrayList<>();
        String sqlStatement = "SELECT ID , URL, Processed, InLinks FROM " + DatabaseColumn.SEED + " WHERE Processed = FALSE LIMIT ? OFFSET ?";
        PreparedStatement statement = connector.getPooledConnection().prepareStatement(sqlStatement);
        statement.setInt(1, limit);
        statement.setInt(2, offset);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            seeds.add(new Seed(resultSet.getInt(1), resultSet.getString(2), resultSet.getBoolean(3), resultSet.getInt(4)));
        }
        statement.close();
        return seeds;
    }

    @Override
    public void delete(List<Seed> seeds) throws SQLException {

    }

    @Override
    public List<List<Seed>> select() throws SQLException {
        return null;
    }

    public void delete(Seed seed) throws SQLException {
        String sqlStatement = "DELETE FROM " + DatabaseColumn.SEED + " WHERE ID = ?";
        PreparedStatement statement = connector.getPooledConnection().prepareStatement(sqlStatement);
        statement.setInt(1, seed.getId());
        statement.executeUpdate();
        statement.close();
    }

    public void refresh() throws SQLException {
        String sqlStatement = "INSERT INTO TempSeedStorage (ID,URL,InLinks,Processed) SELECT  ID,URL,InLinks,FALSE FROM ((SELECT * FROM SEED s1 WHERE s1.processed = true ORDER BY RANDOM() LIMIT 2500 )UNION (SELECT * FROM SEED s2 WHERE s2.processed = false ORDER BY RANDOM() LIMIT 2500)) SeedSelection;";
        Statement statement = connector.getPooledConnection().createStatement();
        statement.execute(sqlStatement);
        sqlStatement = "DELETE FROM Seed;";
        statement.execute(sqlStatement);
        sqlStatement = "INSERT INTO Seed (URL,Processed,InLinks) Select URL,Processed,InLinks FROM TempSeedStorage;";
        statement.execute(sqlStatement);
        sqlStatement = "DELETE FROM TempSeedStorage";
        statement.execute(sqlStatement);
        statement.close();
    }
}
