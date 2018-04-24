package Ranker;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import BusinessModel.StaticRankerDocument;
import Database.ConnectionPool;
import Database.DatabaseController;
import Tools.LoggerInitializer;
import Tools.Pair;

public class StaticRanker {

    private static Logger logger = Logger.getLogger(StaticRanker.class.getName());
    private static int numIterations;
    private static final int defaultNumIterations = 100;
    private static double dampingFactor;
    private static final double defaultDampingFactor = 0.5;
    private static List<Page> pagesToRank = null;
    private static DatabaseController controller;

    private static void setPagesToRank() {
        logger.info("Fetching pages to rank");
        pagesToRank = new ArrayList<>();
        List<StaticRankerDocument> staticRankerDocuments = controller.getStaticRankerDocuments();
        Map<Integer, Pair<Page, StaticRankerDocument>> pageMap = new HashMap<>();
        for (StaticRankerDocument document : staticRankerDocuments) {
            pageMap.put(document.getId(), new Pair<>(new Page(document.getOutLinks(), document.getRank()), document));
        }
        for (Pair<Page, StaticRankerDocument> pageRankerDocumentPair : pageMap.values()) {
            for (Integer pageID : pageRankerDocumentPair.second.getInBoundDocuments()) {
                if (pageMap.containsKey(pageID)) //This happens because I change the URL to the location, 5% max not gonna affect
                    pageRankerDocumentPair.first.getLinkingPages().add(pageMap.get(pageID).first);
            }
            pageRankerDocumentPair.first.setDocumentID(pageRankerDocumentPair.second.getId());
            pagesToRank.add(pageRankerDocumentPair.first);
        }
        staticRankerDocuments.clear();
        pageMap.clear();
        logger.info("Pages fetched: " + pagesToRank.size());
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
        List<StaticRankerDocument> staticRankerDocuments = new ArrayList<>();
        for (Page page : pagesToRank) {
            staticRankerDocuments.add(new StaticRankerDocument(page.getDocumentID(), page.getRank()));
        }
        controller.updateStaticRankerDocuments(staticRankerDocuments);
    }

    private static void rank() {
        setPagesToRank();
        for (int i = 0; i < numIterations; ++i) {
            rankIteration();
            logger.info("Iteration: " + i);
        }
        saveUpdatedRanks();
    }

    public static void main(String args[]) throws Exception {
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
        ConnectionPool.getInstance().close();
    }

}
