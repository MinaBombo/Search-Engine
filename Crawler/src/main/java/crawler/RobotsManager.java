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

    private class RobotRule{
        private Pattern pattern;
        private boolean isAllowed;
        private RobotRule(Pattern p, boolean iA){
            pattern = p;
            isAllowed = iA;
        }
    }
    private ConcurrentHashMap<String, List<RobotRule>> rulesMap;

    RobotsManager() {
        rulesMap = new ConcurrentHashMap<>();
    }

    private static String getBaseUrl(String stringUrl) throws MalformedURLException {
        URL url = new URL(stringUrl);
        return url.getProtocol() + "://" + url.getHost()
                + (url.getPort() > -1 ? ":" + url.getPort() : "");
    }

    private static Pattern parseRule(String rule){
        // TODO: correctly implement this function
        Pattern pattern = Pattern.compile(rule);
        return pattern;
    }

    private List<RobotRule> getRules(String baseUrl) {
        Document robotsTxt;
        try {
            robotsTxt = Jsoup.connect(baseUrl + "/robots.txt").get();
        } catch (IOException e) {
            System.err.println("Error while downloading robots file");
            e.printStackTrace();
            return null;
        }

        List<RobotRule> rules = new LinkedList<>();
        Scanner robotsScanner = new Scanner(robotsTxt.toString().toLowerCase());
        while(robotsScanner.hasNext() && !robotsScanner.next().equals("user-agent:")){
            if(robotsScanner.next().equals("*")){
                String allowance;
                while(robotsScanner.hasNext() && !(allowance = robotsScanner.next()).equals("user-agent:")){
                    rules.add(new RobotRule(parseRule(robotsScanner.next()), allowance.equals("allow")));
                }
                break;
            }
        }
        robotsScanner.close();
        return rules;
    }

    Boolean isAllowed(String url) {
        String baseUrl;
        try {
            baseUrl = getBaseUrl(url);
        } catch (MalformedURLException e) {
            System.err.println("Error while getting base url");
            e.printStackTrace();
            return false;
        }

        List<RobotRule> rules = rulesMap.computeIfAbsent(baseUrl, this::getRules);
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

    void resetRules(){
        rulesMap.clear();
    }
}
