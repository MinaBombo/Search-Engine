package Database;

import java.sql.SQLException;

public interface DatabaseModule<T> {
    public void insert(T t) throws SQLException;
    public void update(T t) throws SQLException;
    public void delete(T t) throws SQLException;
}
