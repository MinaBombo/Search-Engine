package DynamicRanker;

import BusinessModel.BrowserDocument;

import java.util.List;

public class test {

    public static void main(String [] args){
        DynamicRanker ranker = new DynamicRanker();
        List<BrowserDocument> documents =  ranker.rank(new String[] {"Wikipedia".toLowerCase()});
        System.out.println(documents.size());
    }
}
