package Ranker;

import java.util.List;

public class StaticRanker {

    private final int numIterations = 100;
    private final double initialPageRank = 1.0;
    private List<Page> pagesToRank = null;

    private void setPagesToRank(){
        //TODO: implement this function
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
    }

    public void rank(){
        setPagesToRank();
        for(int i=0; i<numIterations; ++i){
            rankIteration();
        }
        saveUpdatedRanks();
    }

}
