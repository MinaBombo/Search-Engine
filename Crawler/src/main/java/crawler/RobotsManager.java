package crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class RobotsManager {

    private static ConcurrentHashMap<String, List<Pattern>> rulesMap;

    RobotsManager() {
        rulesMap = new ConcurrentHashMap<>();
    }

    private static String getBaseUrl(String stringUrl) throws MalformedURLException {
        URL url = new URL(stringUrl);
        return url.getProtocol() + "://" + url.getHost()
                + (url.getPort() > -1 ? ":" + url.getPort() : "");
    }

    private static List<Pattern> getRules(String baseUrl) {
        Document robotsTxt;
        try {
            robotsTxt = Jsoup.connect(baseUrl + "/robots.txt").get();
        } catch (IOException e) {
            System.err.println("Error while downloading robots file");
            e.printStackTrace();
            return null;
        }

        List<Pattern> rules = new LinkedList<>();
        Scanner robotsScanner = new Scanner(robotsTxt.toString());
        while(!robotsScanner.next().equals("User-agent:")){
            if(robotsScanner.next().equals("*")){
                while(robotsScanner.next().equals("Disallow:")){
                    rules.add(Pattern.compile(robotsScanner.next()));
                }
                break;
            }
        }
        robotsScanner.close();
        return rules;
    }

    static Boolean isAllowed(String url) {
        String baseUrl;
        try {
            baseUrl = getBaseUrl(url);
        } catch (MalformedURLException e) {
            System.err.println("Error while getting base url");
            e.printStackTrace();
            return false;
        }

        List<Pattern> rules = rulesMap.computeIfAbsent(baseUrl, RobotsManager::getRules);
        if (rules == null) {
            return true;
        }

        for(Pattern rule : rules){
            Matcher matcher = rule.matcher(url);
            if(matcher.matches()){
                return false;
            }
        }
        return true;
    }

    static void resetRules(){
        rulesMap.clear();
    }
}
