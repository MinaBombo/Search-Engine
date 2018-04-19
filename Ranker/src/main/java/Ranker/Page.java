package Ranker;

public class Page {

    private final int id;
    private final int outLinks;
    private double rank;

    public Page(int id, int outLinks, double rank){
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

    public double getRank() {
        return rank;
    }
    public void setRank(double rank) {
        this.rank = rank;
    }
}
