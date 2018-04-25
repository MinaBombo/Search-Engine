package DynamicRanker;

import BusinessModel.BrowserDocument;

import java.util.List;

public class test {

    public static void main(String [] args){
        DynamicRanker ranker = new DynamicRanker();
        ranker.rank(new String[] {"Hassan".toLowerCase()});
        System.out.println(ranker.getBrowserDocuments(1).get(0).getUrl());
    }
}
