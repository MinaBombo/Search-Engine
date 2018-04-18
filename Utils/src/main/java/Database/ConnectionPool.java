package Database;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionPool implements AutoCloseable {
    private BasicDataSource dataSource;


    private static ConnectionPool ourInstance = new ConnectionPool();

    public static ConnectionPool getInstance() {
        return ourInstance;
    }

    private ConnectionPool() {
        dataSource = new BasicDataSource();
        dataSource.setUrl(DatabaseConnector.url);
        dataSource.setUsername(DatabaseConnector.user);
        dataSource.setPassword(DatabaseConnector.password);
        dataSource.setMaxWaitMillis(1000);
        dataSource.setRemoveAbandonedOnBorrow(true);
        dataSource.setRemoveAbandonedOnMaintenance(true);
        dataSource.setRemoveAbandonedTimeout(60);
        //dataSource.setInitialSize(DynamicIndexer.numThreads+1);
    }
    public  Connection getConnection() throws SQLException{
        return dataSource.getConnection();
    }
    public void setInitialSize(int initialSize){
        dataSource.setInitialSize(initialSize);
        dataSource.setMinIdle(initialSize);
        dataSource.setMaxTotal(2*initialSize);
        dataSource.setMaxIdle(2*initialSize);
    }

    @Override
    public void close() throws Exception {
        dataSource.close();
    }
}
