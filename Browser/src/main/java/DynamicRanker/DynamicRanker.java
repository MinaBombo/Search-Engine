package DynamicRanker;

import BusinessModel.BrowserDocument;
import BusinessModel.DynamicRankerDocument;
import Database.DatabaseController;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Math.log;

public class DynamicRanker {

    private List<DynamicRankerDocument> rankingDocsList = null;
    private Map<String, Double> idfMap = null;
    private DatabaseController controller;

    private void getRelevantData(String [] searchWords){
        rankingDocsList = controller.getDynamicRankerDocuments(searchWords);
        Map<String,Integer> wordMap = controller.getWordMap(searchWords);
        Integer numDocuments = controller.getNumDocuments();
        for(Map.Entry<String ,Integer> entry : wordMap.entrySet()){
            idfMap.put(entry.getKey(),log((double)numDocuments/entry.getValue()));
        }
    }

    private void processSearchWord(String searchWord){
        double wordIdf = idfMap.get(searchWord);
        for(DynamicRankerDocument dynamicRankerDocument :rankingDocsList){
            dynamicRankerDocument.addToDynamicRank(wordIdf * dynamicRankerDocument.getNormWordFreq(searchWord));
        }
    }

    private List<BrowserDocument> getFinalSortedDocs(){
        return controller.getBrowserDocuments(rankingDocsList);
    }

    public List<BrowserDocument> rank(String searchWords[]){
        getRelevantData(searchWords);
        if(rankingDocsList.size() == 0 || idfMap.size() == 0) {
            return null;
        }
        else {
            for (String searchWord : searchWords) {
                processSearchWord(searchWord);
            }
            rankingDocsList.sort(DynamicRankerDocument::compareTo);
            return getFinalSortedDocs();
        }
    }

    public DynamicRanker(){
        try {
            controller = new DatabaseController();
            idfMap = new HashMap<>();
        }
        catch (SQLException exception){
            System.err.println("Error while opening controller");
        }
    }
}
