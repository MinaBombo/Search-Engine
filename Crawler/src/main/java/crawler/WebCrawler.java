package crawler;

import Database.ConnectionPool;
import Database.DatabaseController;
import Util.Seed;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class WebCrawler {

    private final static int maxNumUrls = 5000;
    private static int maxNumThreads;

    public static void main(String[] args) throws SQLException {
        try{
            maxNumThreads = Integer.parseInt(args[0]);
        } catch (Exception e){
            System.err.println("Error while parsing arguments");
            System.err.println(e.toString());
            System.err.println("Setting parameters to default values");
            maxNumThreads = Runtime.getRuntime().availableProcessors();
        }

        DatabaseController controller = new DatabaseController();
        ExecutorService pool = Executors.newFixedThreadPool(maxNumThreads);
        List<Seed> seeds;
        int processedURLCount = 0;
        do {
            seeds = controller.getUnprocessedSeeds(maxNumThreads, 0);
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
        } while (!seeds.isEmpty() && processedURLCount <= maxNumUrls);
        pool.shutdown();
        controller.close();
        try {
            ConnectionPool.getInstance().close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

}
