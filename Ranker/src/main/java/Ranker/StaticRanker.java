package Ranker;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.logging.Logger;

import BusinessModel.RankerDocument;
import Database.ConnectionPool;
import Database.DatabaseController;
import Tools.LoggerInitializer;
import Tools.Pair;

public class StaticRanker {

    private static Logger logger = Logger.getLogger(StaticRanker.class.getName());
    private static int numIterations;
    private static final int defaultNumIterations = 100;
    private static double dampingFactor;
    private static final double defaultDampingFactor = 0.85;
    private static List<Page> pagesToRank = null;
    private static DatabaseController controller;

    private static void setPagesToRank() {
        logger.info("Fetching pages to rank");
        System.out.println("Fetching Pages to rank");
        pagesToRank = new ArrayList<>();
        List<RankerDocument> rankerDocuments = getPages();
        Map<Integer, Pair<Page, RankerDocument>> pageMap = new HashMap<>();
        for (RankerDocument document : rankerDocuments) {
            pageMap.put(document.getId(), new Pair<>(new Page(document.getOutLinks(), document.getRank()), document));
        }
        for (Pair<Page, RankerDocument> pageRankerDocumentPair : pageMap.values()) {
            for (Integer pageID : pageRankerDocumentPair.second.getInBoundDocuments()) {
                pageRankerDocumentPair.first.getLinkingPages().add(pageMap.get(pageID).first);
            }
            pageRankerDocumentPair.first.setDocumentID(pageRankerDocumentPair.second.getId());
            pagesToRank.add(pageRankerDocumentPair.first);
        }
        rankerDocuments.clear();
        pageMap.clear();
        logger.info("Pages fetched: " + pagesToRank.size());
        System.out.println("Pages fetched: " + pagesToRank.size());
    }

    private static List<RankerDocument> getPages() {
        ExecutorService pool = Executors.newFixedThreadPool(20);
        ConnectionPool.getInstance().setInitialSize(20);
        List<RankerDocument> rankerDocuments = controller.getRankerDocuments();
        for (RankerDocument document : rankerDocuments) {
            pool.submit(() -> {
                        try {
                            DatabaseController currentController = new DatabaseController();
                            currentController.populateRankerDocument(document);
                            System.out.println(document.getId());
                            currentController.close();

                        } catch (SQLException exception) {
                            System.err.println("Couldn't open A controller for this task");
                        }
                    }
            );
        }
        pool.shutdown();
        try {
            pool.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            System.err.println("Error in await termination");
        }
        return rankerDocuments;

    }

    private static void rankIteration() {
        for (Page pageToRank : pagesToRank) {
            List<Page> linkingPages = pageToRank.getLinkingPages();
            double newRank = 0.0;
            for (Page linkingPage : linkingPages) {
                newRank += linkingPage.getRank() / linkingPage.getOutLinks();
            }
            newRank = (1 - dampingFactor) + dampingFactor * newRank;
            pageToRank.setRank(newRank);
        }
    }

    private static void saveUpdatedRanks() {
        logger.info("Saving updated pages");
        List<RankerDocument> rankerDocuments = new ArrayList<>();
        for (Page page : pagesToRank) {
            rankerDocuments.add(new RankerDocument(page.getDocumentID(),page.getRank()));
        }
        controller.updateRankerDocuments(rankerDocuments);
    }

    private static void rank() {
        setPagesToRank();
        for (int i = 0; i < numIterations; ++i) {
            rankIteration();
            logger.info("Iteration: " + i);
        }
        saveUpdatedRanks();
    }

    public static void main(String args[]) {
        LoggerInitializer.initLogger(logger);
        try {
            numIterations = Integer.parseInt(args[0]);
        } catch (Exception e) {
            logger.warning("Error while parsing number of iterations");
            e.printStackTrace();
            logger.warning("Setting to default: " + String.valueOf(defaultNumIterations));
            numIterations = defaultNumIterations;
        }
        try {
            dampingFactor = Double.parseDouble(args[1]);
        } catch (Exception e) {
            logger.warning("Error while parsing damping factor");
            e.printStackTrace();
            logger.warning("Setting to default: " + String.valueOf(defaultDampingFactor));
            dampingFactor = defaultDampingFactor;
        }
        try {
            controller = new DatabaseController();
        } catch (SQLException exception) {
            logger.warning("Couldn't get Database controller, exiting");
            return;
        }
        rank();
    }

}
