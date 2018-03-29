import Database.DatabaseColumn;
import Database.DatabaseConnector;
import Database.DatabaseController;
import Database.DatabaseModule;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class SeedDatabaseModule implements DatabaseModule<List<String>> {
    DatabaseConnector connector;
    SeedDatabaseModule(DatabaseConnector connector){
        this.connector = connector;
    }
    @Override
    public void insert(List<String> strings) throws SQLException {
        String sqlStatement = "INSERT INTO "+ DatabaseColumn.SEED + "(URL) VALUES (?)";
        PreparedStatement statement = connector.getPooledConnection().prepareStatement(sqlStatement);
        for(String s : strings) {
            statement.setString(1, s);
            statement.addBatch();
        }
        statement.executeBatch();
        statement.close();
    }

    @Override
    public void update(List<String> strings) throws SQLException {
    }

    @Override
    public void delete(List<String> strings) throws SQLException {

    }

    @Override
    public List<List<String>> select() throws SQLException {
        return null;
    }
}
