package Indexer;

public class Page {

    private final int id;
    private final int outLinks;
    private int rank;

    public Page(int id, int outLinks, int rank){
        this.id = id;
        this.outLinks = outLinks;
        this.rank = rank;
    }


    public int getId() {
        return id;
    }

    public int getOutLinks() {
        return outLinks;
    }

    public int getRank() {
        return rank;
    }
    public void setRank(int rank) {
        this.rank = rank;
    }
}
