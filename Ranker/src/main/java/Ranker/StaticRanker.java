package Ranker;

import java.util.List;

public class StaticRanker {

    // TODO: decide upon initial values and if they are user given
    private final int numIterations = 100;
    private final double initialPageRank = 1.0;
    private List<Page> pagesToRank = null;

    private void setPagesToRank(){
        //TODO: implement this function
        // This function should populate the list of pages to rank
        // from the graph constructed by the crawler
        // TODO: decide upon graph construction(where and how)
    }

    private void rankIteration(){
        for(Page pageToRank : pagesToRank){
            List<Page> linkingPages= pageToRank.getLinkingPages();
            double newRank = 0.0;
            for(Page linkingPage : linkingPages){
                newRank += linkingPage.getRank() / linkingPage.getOutLinks();
            }
            pageToRank.setRank(newRank);
        }
    }

    private void saveUpdatedRanks(){
        //TODO: implement this function
        // This function should save back the updated pages ranks into database
        // TODO: decide upon database edition for adapting to ranker
    }

    public void rank(){
        setPagesToRank();
        for(int i=0; i<numIterations; ++i){
            rankIteration();
        }
        saveUpdatedRanks();
    }

}
