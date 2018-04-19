package Ranker;

import java.util.LinkedList;
import java.util.List;

public class Page {

    private final int outLinks;
    private double rank;
    private List<Page> linkingPages;

    public Page(int outLinks, double rank){
        this.outLinks = outLinks;
        this.rank = rank;
        linkingPages = new LinkedList<>();
    }

    public int getOutLinks() {return outLinks;}

    public double getRank() {
        return rank;
    }
    public void setRank(double rank) {
        this.rank = rank;
    }

    public boolean addLinkingPage(Page page){ return linkingPages.add(page); }
    public List<Page> getLinkingPages(){ return linkingPages; }
}
