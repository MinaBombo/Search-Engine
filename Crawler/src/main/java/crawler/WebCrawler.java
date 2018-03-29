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
    private static DatabaseController controller;
    private static ExecutorService pool;

    private static void init(String[] args){
        try{
            maxNumThreads = Integer.parseInt(args[0]);
        } catch (Exception e){
            System.err.println("Error while parsing arguments");
            System.err.println(e.toString());
            System.err.println("Setting parameters to default values");
            maxNumThreads = Runtime.getRuntime().availableProcessors();
        }
        try{
            controller = new DatabaseController();
        } catch (SQLException e){
            e.printStackTrace();
            System.exit(0);
        }
        pool = Executors.newFixedThreadPool(maxNumThreads);
    }
    private static void cleanup(){
        pool.shutdown();
        controller.close();
        try {
            ConnectionPool.getInstance().close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private static void crawl(){
        List<Seed> seeds;
        int processedURLCount = 0;
        do {
            seeds = controller.getUnprocessedSeeds(maxNumThreads, 0);
            List<WebCrawlingTask> tasks = new ArrayList<>();
            for (Seed seed : seeds) {
                tasks.add(new WebCrawlingTask(seed));
            }
            try{
                WebCrawlingTask.initializeDbController();
            } catch (SQLException e){
                System.exit(0);
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
                WebCrawlingTask.closeDbController();
            }
            System.out.println(processedURLCount);
        } while (!seeds.isEmpty() && processedURLCount <= maxNumUrls);
        WebCrawlingTask.closeDbController();
    }

    public static void main(String[] args) {
        init(args);
        crawl();
        cleanup();
    }
}
