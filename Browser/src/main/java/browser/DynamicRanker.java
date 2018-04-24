package browser;

import BusinessModel.Document;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class DynamicRanker {

    private static Logger logger = Logger.getLogger(DynamicRanker.class.getName());
    private List<RankingDocument> rankingDocsList = null;
    private Map<String, Integer> idfMap = null;

    private void getRelevantData(){
        // TODO: populates docsList from database
        // TODO: computes idfMap: each word occurs in how many doc
        // idf[searchWord] = log(total docs number / number of docs containing searchWord)
    }

    private void processSearchWord(String searchWord){
        double wordIdf = idfMap.get(searchWord);
        for(RankingDocument rankingDocument:rankingDocsList){
            rankingDocument.addToDynamicRank(wordIdf * rankingDocument.getNormWordFreq(searchWord));
        }
    }

    private List<Document> getFinalSortedDocs(){
        // TODO: Fetch the actual documents by id from rankingDocsList
        List<Document> finalDocsList = null;
        return finalDocsList;
    }

    public List<Document> rank(String searchWords[]){
        getRelevantData();
        for(String searchWord:searchWords){
            processSearchWord(searchWord);
        }
        rankingDocsList.sort(RankingDocument::compareTo);
        return getFinalSortedDocs();
    }

}
