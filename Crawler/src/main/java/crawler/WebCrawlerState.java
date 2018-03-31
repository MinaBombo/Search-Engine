package crawler;

import BusinessModel.Seed;
import com.panforge.robotstxt.RobotsTxt;

import java.util.List;

public class WebCrawlerState {
    public WebCrawlerState(RobotsTxt robotsTxt, List<Seed> seeds,String url) {
        this.robotsTxt = robotsTxt;
        this.seeds = seeds;
        this.url = url;
    }

    public RobotsTxt getRobotsTxt() {

        return robotsTxt;
    }

    public void setRobotsTxt(RobotsTxt robotsTxt) {
        this.robotsTxt = robotsTxt;
    }

    public List<Seed> getSeeds() {
        return seeds;
    }

    public void setSeeds(List<Seed> seeds) {
        this.seeds = seeds;
    }

    RobotsTxt robotsTxt;
    List<Seed> seeds;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private String url;



}
