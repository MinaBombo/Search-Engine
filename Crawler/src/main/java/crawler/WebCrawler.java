package crawler;

import Database.ConnectionPool;
import Database.DatabaseController;
import Indexer.DynamicIndexer;
import Util.Seed;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class WebCrawler {

    public static void main(String[] args) throws SQLException {
        DatabaseController controller = new DatabaseController();
        ExecutorService pool = Executors.newFixedThreadPool(DynamicIndexer.numThreads);
        List<Seed> seeds;
        int processedURLCount = 0;
        do {
            seeds = controller.getUnprocessedSeeds(DynamicIndexer.numThreads, 0);
            List<WebCrawlingTask> tasks = new ArrayList<>();
            for (Seed seed : seeds) {
                tasks.add(new WebCrawlingTask(seed));
            }

            try {
                List<Future<List<Seed>>> taskResults = pool.invokeAll(tasks);
                for (Future<List<Seed>> taskResult : taskResults) {
                    controller.insertSeed(taskResult.get());
                    processedURLCount++;
                }
            } catch (ExecutionException | InterruptedException exception) {
                System.err.println("Error while executing tasks");
                exception.printStackTrace();
            }
            System.out.println(processedURLCount);
        } while (!seeds.isEmpty() && processedURLCount <= 5000);
        pool.shutdown();
        controller.close();
        try {
            ConnectionPool.getInstance().close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

}
