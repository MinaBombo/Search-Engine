package BusinessModel;

import java.util.ArrayList;
import java.util.List;

public class RankerDocument {
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
    public RankerDocument(Integer id,Integer outLinks){
        this.id = id;
        this.outLinks = outLinks;
    }
    public RankerDocument(Integer id){
        this.id = id;
        this.inBoundDocuments = new ArrayList<>();
    }
    //Note : Never use inbound docs when using this constructor
    public RankerDocument(Integer id,Double rank){
        this.id = id;
        this.rank = rank;
    }
}
