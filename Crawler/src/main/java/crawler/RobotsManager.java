package crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

class RobotsManager {
    private static ConcurrentHashMap<String, List<Pattern>> rulesMap;

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

    private static List<Pattern> getRules(String baseUrl) {
        try {
            Document robotsTxt = Jsoup.connect(baseUrl + "/robots.txt").get();
            List<Pattern> rules = new LinkedList<>();
            // TODO: parse robots.txt
            return rules;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static boolean checkUrlToPatterns(List<Pattern> rules, String url) {
        // TODO: compare url to patterns
        return true;
    }

    static Boolean isAllowed(String url) {
        try {
            String baseUrl = getBaseUrl(url);
            List<Pattern> rules = rulesMap.computeIfAbsent(baseUrl, RobotsManager::getRules);
            if (rules == null) {
                rulesMap.remove(baseUrl);
                // TODO: Check what is best to do
                return false;
            }
            return checkUrlToPatterns(rules, url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        }

    }
}
