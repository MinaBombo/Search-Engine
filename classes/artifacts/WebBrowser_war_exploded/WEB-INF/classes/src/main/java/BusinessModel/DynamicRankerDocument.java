package BusinessModel;

import java.util.HashMap;
import java.util.Map;

public class DynamicRankerDocument implements Comparable<DynamicRankerDocument>{
    public Integer getId() {
        return id;
    }

    private final Integer id;
    private int length;
    private final double staticRank;
    private Map<String, Double> wordNormFreqMap;
    private double dynamicRank;

    public DynamicRankerDocument(Integer id, Integer length, double staticRank){
        this.id = id;
        this.length = length;
        this.staticRank = staticRank;
        wordNormFreqMap = new HashMap<>();
        dynamicRank = 0.0;
    }

    public Double setNormWordFreq(String word, int freq){
        return wordNormFreqMap.put(word, (double)freq/length);
    }
    public Double getNormWordFreq(String word){return this.wordNormFreqMap.get(word);}

    public void addToDynamicRank(double rank){this.dynamicRank += rank;}

    private double finalRank(){return this.staticRank*this.dynamicRank;}

    @Override
    public int compareTo(DynamicRankerDocument dynamicRankerDocument) {
        return Double.compare(this.finalRank(), dynamicRankerDocument.finalRank());
    }
}
