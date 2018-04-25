package DynamicRanker;

import BusinessModel.BrowserDocument;

import java.util.List;

public class test {

    public static void main(String [] args){
        DynamicRanker ranker = new DynamicRanker();
        List<BrowserDocument> documents =  ranker.rank(new String[] {"Hassan".toLowerCase()});
        System.out.println(documents.get(0).getUrl());
    }
}
