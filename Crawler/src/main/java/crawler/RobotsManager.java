package crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class RobotsManager {

    private class RobotRule{
        Pattern pattern;
        boolean isAllowed;

        RobotRule(Pattern p, boolean a){
            pattern = p;
            isAllowed = a;
        }
    }
    private static ConcurrentHashMap<String, List<RobotRule>> rulesMap;

    RobotsManager() {
        rulesMap = new ConcurrentHashMap<>();
    }

    private static String getBaseUrl(String stringUrl) throws MalformedURLException {
        try {
            URL url = new URL(stringUrl);
            return url.getProtocol() + "://" + url.getHost()
                    + (url.getPort() > -1 ? ":" + url.getPort() : "");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    private static List<RobotRule> getRules(String baseUrl) {
        try {
            Document robotsTxt = Jsoup.connect(baseUrl + "/robots.txt").get();
            List<RobotRule> rules = new LinkedList<>();
            // TODO: parse robots.txt
            return rules;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    static Boolean isAllowed(String url) {
        String baseUrl;
        try {
            baseUrl = getBaseUrl(url);
        } catch (MalformedURLException e) {
            return false;
        }

        List<RobotRule> rules = rulesMap.computeIfAbsent(baseUrl, RobotsManager::getRules);
        if (rules == null) {
            return true;
        }

        for(RobotRule rule : rules){
            Matcher matcher = rule.pattern.matcher(url);
            if(matcher.matches()){
                return rule.isAllowed;
            }
        }
        return true;
    }

    static void resetRules(){
        rulesMap.clear();
    }
}
