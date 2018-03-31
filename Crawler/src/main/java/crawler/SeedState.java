package crawler;

import com.panforge.robotstxt.RobotsTxt;

public class SeedState {

    public RobotsTxt getRobotsTxt() {
        return robotsTxt;
    }

    public void setRobotsTxt(RobotsTxt robotsTxt) {
        this.robotsTxt = robotsTxt;
    }

    public Boolean getAllowed() {
        return allowed;
    }

    public void setAllowed(Boolean allowed) {
        this.allowed = allowed;
    }
    public Boolean isexist(){
        return robotsTxt!=null;
    }

    public SeedState(RobotsTxt robotsTxt, Boolean allowed) {
        this.robotsTxt = robotsTxt;
        this.allowed = allowed;
    }

    private RobotsTxt robotsTxt;
    private Boolean allowed;

}
