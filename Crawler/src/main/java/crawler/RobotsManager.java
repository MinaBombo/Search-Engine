package crawler;

import Tools.LoggerInitializer;
import com.panforge.robotstxt.RobotsTxt;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

class RobotsManager {

    private ConcurrentHashMap<String, RobotsTxt> rulesMap;
    private String userAgent;
    private static final byte[] defaultRobots = ("User-agent: *\n" +
                                                "Allow: /").getBytes();
    private static Logger logger = Logger.getLogger(RobotsManager.class.getName());
    private static Boolean loggerInitialized = false;
    RobotsManager(String agent) {
        userAgent = agent;
        rulesMap = new ConcurrentHashMap<>();
        if(!loggerInitialized)
             loggerInitialized = LoggerInitializer.initLogger(logger);

    }

    private String getBaseUrl(String stringUrl) throws MalformedURLException {
        URL url = new URL(stringUrl);
        return url.getProtocol() + "://" + url.getHost()
                + (url.getPort() > -1 ? ":" + url.getPort() : "");
    }

    RobotsTxt getRules(String baseUrl) {
        Document robotsTxtDoc;
        try {
            robotsTxtDoc = Jsoup.connect(baseUrl + "/robots.txt").get();
            return RobotsTxt.read(new ByteArrayInputStream(robotsTxtDoc.body().text().getBytes()));
        } catch (IOException e){
            logger.log(Level.WARNING,"Error while download/parsing robots.txt");
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

    SeedState isAllowed(String url) {
        String baseUrl;
        try {
            baseUrl = getBaseUrl(url);
        } catch (MalformedURLException e) {
            logger.log(Level.WARNING,"Error while getting base url");
            //e.printStackTrace();
            return new SeedState(null,false);
        }
        RobotsTxt robotsTxt;
        if(rulesMap.containsKey(baseUrl)){
             robotsTxt = rulesMap.get(baseUrl);

        }
        else{
            robotsTxt = getRules(baseUrl);
        }
        return new SeedState(robotsTxt,robotsTxt.query(userAgent,baseUrl));
    }

    void resetRules(){
        rulesMap.clear();
    }
    void addRobotsTxt(RobotsTxt robotsTxt,String url){
        try{
            rulesMap.putIfAbsent(getBaseUrl(url),robotsTxt);
        }
        catch (MalformedURLException exception){
            System.err.println("Error while inserting robotsTxt");
        }
    }
}
