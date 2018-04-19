package Ranker;

import java.util.List;
import java.util.logging.Logger;
import Tools.LoggerInitializer;

public class StaticRanker {

    private static Logger logger = Logger.getLogger(StaticRanker.class.getName());
    private static int numIterations;
    private static final int defaultNumIterations = 100;
    private static List<Page> pagesToRank = null;

    private static void setPagesToRank(){
        logger.info("Fetching pages to rank");
        //TODO: implement this function
        // This function should populate the list of pages to rank
        // from the graph constructed by the crawler
        // TODO: decide upon graph construction(where and how)
        logger.info("Pages fetched: " + pagesToRank.size());
    }

    private static void rankIteration(){
        for(Page pageToRank : pagesToRank){
            List<Page> linkingPages= pageToRank.getLinkingPages();
            double newRank = 0.0;
            for(Page linkingPage : linkingPages){
                newRank += linkingPage.getRank() / linkingPage.getOutLinks();
            }
            pageToRank.setRank(newRank);
        }
    }

    private static void saveUpdatedRanks(){
        logger.info("Saving updated pages");
        //TODO: implement this function
        // This function should save back the updated pages ranks into database
        // TODO: decide upon database edition for adapting to ranker
    }

    private static void rank(){
        setPagesToRank();
        for(int i=0; i<numIterations; ++i){
            rankIteration();
            logger.info("Iteration: "+ i);
        }
        saveUpdatedRanks();
    }

    public static void main(String args[]){
        LoggerInitializer.initLogger(logger);
        try{
            numIterations = Integer.parseInt(args[0]);
        } catch (Exception e){
            logger.warning("Error while parsing number of iterations");
            e.printStackTrace();
            logger.warning("Setting to default: " + String.valueOf(defaultNumIterations));
            numIterations = defaultNumIterations;
        }
        rank();
    }

}
