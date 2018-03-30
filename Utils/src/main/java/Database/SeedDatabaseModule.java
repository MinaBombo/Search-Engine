package Database;

import BusinessModel.Seed;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SeedDatabaseModule implements DatabaseModule<List<Seed>> {
    DatabaseConnector connector;

    SeedDatabaseModule(DatabaseConnector connector){
        this.connector = connector;
    }



    @Override
    public void update(List<Seed> seeds) throws SQLException {

    }
    public void update(Seed seed) throws SQLException{
        String sqlStatement = "UPDATE "+DatabaseColumn.SEED +" SET URL = ?, Processed = ? WHERE ID = ?";
        PreparedStatement statement = connector.getPooledConnection().prepareStatement(sqlStatement);
        statement.setString(1,seed.getUrl());
        statement.setBoolean(2,seed.isProcessed());
        statement.setInt(3,seed.getId());
        statement.executeUpdate();
        statement.close();
    }
    @Override
    public void insert(List<Seed> seeds) throws SQLException {
        String sqlStatement = "INSERT INTO "+ DatabaseColumn.SEED + "(URL, Processed) VALUES (?,?) ON CONFLICT (URL) DO NOTHING";
        PreparedStatement statement = connector.getPooledConnection().prepareStatement(sqlStatement);
        for(Seed seed : seeds) {
            statement.setString(1, seed.getUrl());
            statement.setBoolean(2,seed.isProcessed());
            statement.addBatch();
        }

        statement.executeBatch();
        ResultSet resultSet = statement.getGeneratedKeys();
        int counter = 0;
        while (resultSet.next()){
            seeds.get(counter++).setId(resultSet.getInt(1));
        }
        statement.close();
    }

    public List<Seed> select(int limit,int offset) throws SQLException{
        List<Seed> seeds = new ArrayList<>();
        String sqlStatement = "SELECT ID , URL, Processed FROM "+DatabaseColumn.SEED+" WHERE Processed = FALSE LIMIT ? OFFSET ?";
        PreparedStatement statement = connector.getPooledConnection().prepareStatement(sqlStatement);
        statement.setInt(1,limit);
        statement.setInt(2,offset);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()){
            seeds.add(new Seed(resultSet.getInt(1),resultSet.getString(2),resultSet.getBoolean(3)));
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
    public void delete(Seed seed) throws SQLException{
        String sqlStatement = "DELETE FROM "+ DatabaseColumn.SEED + " WHERE ID = ?";
        PreparedStatement statement = connector.getPooledConnection().prepareStatement(sqlStatement);
        statement.setInt(1,seed.getId());
        statement.executeUpdate();
        statement.close();
    }
}
