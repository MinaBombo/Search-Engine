package crawler;

import Database.ConnectionPool;
import Database.DatabaseController;
import BusinessModel.Seed;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static Tools.ThreadCounter.getNumThreads;

public class WebCrawler {

    private final static int maxNumUrls = 5000;
    private static int maxNumThreads;
    private static DatabaseController controller;
    private static ExecutorService pool;

    private static void init(String[] args){
        maxNumThreads = getNumThreads(args[0]);
        try {
            controller = new DatabaseController();
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(0);
        }
        pool = Executors.newFixedThreadPool(maxNumThreads);
        ConnectionPool.getInstance().setInitialSize(maxNumThreads);
        Runtime.getRuntime().addShutdownHook(new Thread (){
            @Override
            public void run() {
                System.out.println("Entered Signal Handler");
                cleanup();
            }
        });
    }

    private static void cleanup() {
        pool.shutdown();
        controller.close();
        try {
            ConnectionPool.getInstance().close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private static void crawl() {
        while (true) {
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
                        List<Seed> result = taskResult.get();
                        if (result != null) {
                            controller.insertSeed(result);
                            ++processedURLCount;
                        }
                    }
                } catch (ExecutionException | InterruptedException exception) {
                    System.err.println("Error while executing tasks");
                    exception.printStackTrace();
                }
                System.out.println(processedURLCount);
            } while (!seeds.isEmpty() && processedURLCount <= maxNumUrls);
            controller.refreshSeeds();
        }
    }

    public static void main(String[] args) {
        init(args);
        crawl();
        cleanup();
    }
}
