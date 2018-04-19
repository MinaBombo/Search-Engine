package crawler;

import Database.ConnectionPool;
import Database.DatabaseController;
import BusinessModel.Seed;
import Tools.LoggerInitializer;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static Tools.ThreadCounter.getNumThreads;

public class WebCrawler {

    private final static int maxNumUrls = 5000;
    private static int maxNumThreads;
    private static DatabaseController controller;
    private static ExecutorService pool;
    private static final String userAgent = "CrawlerX";
    static final RobotsManager robotsManager = new RobotsManager(userAgent);
    private static Logger logger = Logger.getLogger(WebCrawler.class.getName());

    private static void init(String[] args) {
        maxNumThreads = getNumThreads(args[0]);
        try {
            controller = new DatabaseController();
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(0);
        }
        pool = Executors.newFixedThreadPool(maxNumThreads);
        ConnectionPool.getInstance().setInitialSize(maxNumThreads+1);
        Runtime.getRuntime().addShutdownHook(new Thread(WebCrawler::cleanup));
        LoggerInitializer.initLogger(logger);
    }

    private static void cleanup() {
        pool.shutdown();
        controller.close();
        try {
            ConnectionPool.getInstance().close();
        } catch (Exception exception) {
            System.err.println("Error While closing pool");
            //exception.printStackTrace();
        }
    }

    private static void crawl() {
        Set<String> urlHashSet = ConcurrentHashMap.newKeySet();
        while (true) {
            List<Seed> seeds;
            int processedURLCount = 0;
            do {
                seeds = controller.getUnprocessedSeeds(maxNumThreads, 0);
                List<WebCrawlingTask> tasks = new ArrayList<>();
                for (Seed seed : seeds) {
                    tasks.add(new WebCrawlingTask(seed, urlHashSet));
                }
                List<Future<WebCrawlerState>> taskResults;
                try {
                    taskResults = pool.invokeAll(tasks);
                } catch (InterruptedException exception) {
                    logger.log(Level.WARNING, "Error while invoking tasks");
                    continue;
                }
                for (Future<WebCrawlerState> taskResult : taskResults) {
                    try {
                        WebCrawlerState result = taskResult.get();
                        if (result != null) {
                            controller.insertSeed(result.getSeeds());
                            robotsManager.addRobotsTxt(result.robotsTxt, result.getUrl());
                            ++processedURLCount;
                        }
                    } catch (ExecutionException | InterruptedException exception) {
                        logger.log(Level.WARNING, "Error while executing tasks");
                        controller.deleteSeed(seeds.get(taskResults.indexOf(taskResult)));
                    }
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
