package browser;

import BusinessModel.BrowserDocument;
import BusinessModel.Document;
import BusinessModel.DynamicRankerDocument;
import Database.DatabaseController;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static java.lang.Math.log;

public class DynamicRanker {

    private static Logger logger = Logger.getLogger(DynamicRanker.class.getName());
    private List<DynamicRankerDocument> rankingDocsList = null;
    private Map<String, Double> idfMap = null;
    private DatabaseController controller;

    private void getRelevantData(){
        rankingDocsList = controller.getDynamicRankerDocuments();
        Map<String,Integer> wordMap = controller.getWordMap();
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
        getRelevantData();
        for(String searchWord:searchWords){
            processSearchWord(searchWord);
        }
        rankingDocsList.sort(DynamicRankerDocument::compareTo);
        return getFinalSortedDocs();
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
