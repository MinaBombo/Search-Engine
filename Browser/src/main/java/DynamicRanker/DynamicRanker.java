package DynamicRanker;

import BusinessModel.BrowserDocument;
import BusinessModel.DynamicRankerDocument;
import Database.DatabaseController;

import java.sql.SQLException;
import java.util.*;

import static java.lang.Integer.max;
import static java.lang.Integer.min;
import static java.lang.Math.ceil;
import static java.lang.Math.log;

public class DynamicRanker {

    private List<DynamicRankerDocument> rankingDocsList = null;
    private Map<String, Double> idfMap = null;
    private List<BrowserDocument> browserDocuments;
    private DatabaseController controller;
    private Integer numPages;

    private void getRelevantData(String[] searchWords) {
        rankingDocsList = controller.getDynamicRankerDocuments(searchWords);
        Map<String, Integer> wordMap = controller.getWordMap(searchWords);
        Integer numDocuments = controller.getNumDocuments();
        for (Map.Entry<String, Integer> entry : wordMap.entrySet()) {
            idfMap.put(entry.getKey(), log((double) numDocuments / entry.getValue()));
        }
    }

    private void processSearchWord(String searchWord) {
        double wordIdf = idfMap.get(searchWord);
        for (DynamicRankerDocument dynamicRankerDocument : rankingDocsList) {
            dynamicRankerDocument.addToDynamicRank(wordIdf * dynamicRankerDocument.getNormWordFreq(searchWord));
        }
    }

    private List<BrowserDocument> getFinalSortedDocs() {
        return controller.getBrowserDocuments(rankingDocsList);
    }
    public Integer getNumPages(){
        return numPages;
    }

    public void rank(String searchWords[]) {
        getRelevantData(searchWords);
        if (rankingDocsList.size() == 0 || idfMap.size() == 0) {
            throw new NoSuchElementException("Couldn't Find any results for your query");
        } else {
            for (String searchWord : searchWords) {
                processSearchWord(searchWord);
            }
            rankingDocsList.sort(DynamicRankerDocument::compareTo);
            browserDocuments = getFinalSortedDocs();
            numPages = (int)ceil(browserDocuments.size()/10.0);
        }
    }

    public List<BrowserDocument> getBrowserDocuments(Integer pageNumber) {
        if(pageNumber > numPages)
            return null;
        List<BrowserDocument> currentPageBrowserDocs = new ArrayList<>(10);
        for (int i = 0; i < min(10,browserDocuments.size()-((pageNumber-1)*10)); ++i){
            currentPageBrowserDocs.add(browserDocuments.get((pageNumber-1)*10+i));
        }
        return currentPageBrowserDocs;

    }

    public DynamicRanker() {
        try {
            controller = new DatabaseController();
            idfMap = new HashMap<>();
        } catch (SQLException exception) {
            System.err.println("Error while opening controller");
        }
    }
}
