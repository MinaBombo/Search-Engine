package BusinessModel;

import java.util.ArrayList;
import java.util.List;

public class StaticRankerDocument {
    public StaticRankerDocument(Integer id , Integer outLinks, Double rank) {
        this.id = id;
        this.rank = rank;
        this.outLinks = outLinks;
        inBoundDocuments = new ArrayList<>();
    }

    Integer id;
    Double rank;
    Integer outLinks;
    List<Integer> inBoundDocuments;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getRank() {
        return rank;
    }

    public void setRank(Double rank) {
        this.rank = rank;
    }

    public Integer getOutLinks() {
        return outLinks;
    }

    public void setOutLinks(Integer outLinks) {
        this.outLinks = outLinks;
    }

    public List<Integer> getInBoundDocuments() {
        return inBoundDocuments;
    }

    public void setInBoundDocuments(List<Integer> inBoundDocuments) {
        this.inBoundDocuments = inBoundDocuments;
    }
    public StaticRankerDocument(Integer id, Integer outLinks){
        this.id = id;
        this.outLinks = outLinks;
    }
    public StaticRankerDocument(Integer id){
        this.id = id;
        this.inBoundDocuments = new ArrayList<>();
    }
    //Note : Never use inbound docs when using this constructor
    public StaticRankerDocument(Integer id, Double rank){
        this.id = id;
        this.rank = rank;
    }
}
