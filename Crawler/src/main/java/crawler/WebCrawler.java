package crawler;

import Database.DatabaseController;
import Indexer.DynamicIndexer;

import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WebCrawler  {

    public static void main(String [] args) throws SQLException{
        DatabaseController controller = new DatabaseController();
        ExecutorService pool = Executors.newFixedThreadPool(DynamicIndexer.numThreads);

    }

}
