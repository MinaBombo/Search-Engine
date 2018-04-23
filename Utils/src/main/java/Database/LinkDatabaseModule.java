package Database;


import BusinessModel.Link;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class LinkDatabaseModule {
    DatabaseConnector connector;
    LinkDatabaseModule(DatabaseConnector connector){
        this.connector = connector;
    }

    public void insert(List<Link> links) throws SQLException {
        String sqlStatement = "INSERT INTO "+ DatabaseColumn.LINK.toString()+" (DocuemntID, RefrencedLink) VALUES (? , ?)";
        PreparedStatement statement = connector.getPooledConnection().prepareStatement(sqlStatement,Statement.RETURN_GENERATED_KEYS);
        for(Link link : links){
            statement.setInt(1,link.getDocument().getId());
            statement.setString(2,link.getLink());
            statement.addBatch();
        }
        statement.executeBatch();
        ResultSet resultSet = statement.getGeneratedKeys();
        int counter = 0;
        while (resultSet.next()){
            links.get(counter++).setID(resultSet.getInt(1));
        }
        resultSet.close();
        statement.close();
    }

}
