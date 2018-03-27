package crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Leg extends Thread{

    private final Spider parentSpider;


    Leg(Spider spider){
        parentSpider = spider;

    }

    private void downloadUrl(String urlString){

    }

    private void processUrl(String url){

    }

    @Override
    public void run() {
        String url = parentSpider.getNextUrl();
        // TODO: Download page, extract urls and add to graph
        // TODO: Add newly extracted urls to parentSpider

        try{
            Document doc = Jsoup.connect(url).get();
            Elements links = doc.select("a[href]");
            String text = doc.body().text();
            List<String> linkText = new ArrayList<>();
            for(Element link : links){
                linkText.add(link.attr("abs:href"));
            }
        }
        catch (IOException exception){
            System.err.println("An error happened while crawling");
            System.err.println(exception.getMessage());
            exception.printStackTrace();
        }
    }
}
