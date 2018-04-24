package browser;

import com.sun.istack.internal.NotNull;

import java.util.HashMap;
import java.util.Map;

public class RankingDocument implements Comparable<RankingDocument>{

    private final Integer id;
    private final int length;
    private final double staticRank;
    private Map<String, Double> wordNormFreqMap;
    private double dynamicRank;

    public RankingDocument(Integer id, Integer length, double staticRank){
        this.id = id;
        this.length = length;
        this.staticRank = staticRank;
        wordNormFreqMap = new HashMap<>();
        dynamicRank = 0.0;
    }

    Double setNormWordFreq(String word, int freq){
        return wordNormFreqMap.put(word, (double)freq/length);
    }
    Double getNormWordFreq(String word){return this.wordNormFreqMap.get(word);}

    void addToDynamicRank(double rank){this.dynamicRank += rank;}

    private double finalRank(){return this.staticRank*this.dynamicRank;}

    @Override
    public int compareTo(RankingDocument rankingDocument) {
        return Double.compare(this.finalRank(), rankingDocument.finalRank());
    }
}
