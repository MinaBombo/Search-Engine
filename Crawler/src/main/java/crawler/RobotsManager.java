package crawler;

import com.panforge.robotstxt.RobotsTxt;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;

class RobotsManager {

    private ConcurrentHashMap<String, RobotsTxt> rulesMap;
    private String userAgent;
    private static final byte[] defaultRobots = ("User-agent: *\n" +
                                                "Allow: /").getBytes();

    RobotsManager(String agent) {
        userAgent = agent;
        rulesMap = new ConcurrentHashMap<>();
    }

    private String getBaseUrl(String stringUrl) throws MalformedURLException {
        URL url = new URL(stringUrl);
        return url.getProtocol() + "://" + url.getHost()
                + (url.getPort() > -1 ? ":" + url.getPort() : "");
    }

    private RobotsTxt getRules(String baseUrl) {
        Document robotsTxtDoc;
        try {
            robotsTxtDoc = Jsoup.connect(baseUrl + "/robots.txt").get();
            return RobotsTxt.read(new ByteArrayInputStream(robotsTxtDoc.body().text().getBytes()));
        } catch (IOException e){
            System.err.println("Error while download/parsing robots.txt");
            //e.printStackTrace();
            try{
                return RobotsTxt.read(new ByteArrayInputStream(defaultRobots));
            }catch (IOException ioe){
                System.err.println("Error while setting default robots.txt");
                //ioe.printStackTrace();
                return null;
            }
        }
    }

    Boolean isAllowed(String url) {
        String baseUrl;
        try {
            baseUrl = getBaseUrl(url);
        } catch (MalformedURLException e) {
            System.err.println("Error while getting base url");
            //e.printStackTrace();
            return false;
        }

        RobotsTxt robotsTxt = rulesMap.computeIfAbsent(baseUrl, this::getRules);
        if(robotsTxt != null) {
            return (robotsTxt.query(userAgent, url));
        }
        return true;
    }

    void resetRules(){
        rulesMap.clear();
    }
}
