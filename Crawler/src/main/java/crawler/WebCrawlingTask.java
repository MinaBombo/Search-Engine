package crawler;

import Database.DatabaseController;
import Tools.DocumentManager;
import BusinessModel.*;

import Tools.LoggerInitializer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WebCrawlingTask implements Callable<WebCrawlerState> {

    private Seed seed;
    private Set<String> urlSet;
    private static Logger logger =Logger.getLogger(WebCrawlingTask.class.getName());
    private static Boolean loggerInitialized = false;
    WebCrawlingTask(Seed seed, Set <String> urlSet) {
        this.seed = seed;
        this.urlSet = urlSet;
        if(!loggerInitialized)
            loggerInitialized = LoggerInitializer.initLogger(logger);
    }

    @Override
    public WebCrawlerState call(){
        String documentText;
        List<Seed> seeds;
        DatabaseController controller;
        String url;
        try {
            controller = new DatabaseController();
        } catch (SQLException exception) {
            logger.log(Level.WARNING ,"Error while initializing database connection");
            //exception.printStackTrace();
            return null;
        }
        SeedState state = WebCrawler.robotsManager.isAllowed(seed.getUrl());
        if(!state.getAllowed()){
            controller.deleteSeed(seed);
            controller.close();
            return null;
        }
        // Try to get the html document from the web.
        // If for any reason you failed, delete this seed and exit the task
        Document jsoupDoc;
        try {
            jsoupDoc = Jsoup.connect(seed.getUrl()).get();
            if(urlSet.contains(jsoupDoc.location())){
                controller.deleteSeed(seed);
                return null;
            }
            url = seed.getUrl();
            seed.setUrl(jsoupDoc.location());
            Elements links = jsoupDoc.select("a[href]");
            documentText = jsoupDoc.body().text();
            seeds = new LinkedList<>();
            for (Element link : links) {
                seeds.add(new Seed(link.attr("abs:href"), false));
                
            }
        } catch (Exception  exception) {
            logger.log(Level.WARNING,"Error while downloading/parsing document from web");
            //exception.printStackTrace();
            controller.deleteSeed(seed);
            controller.close();
            return null;
        }
        // If every thing succeeded then insert the document and write it to the disk
        BusinessModel.Document indexerDoc = new BusinessModel.Document(
                String.format("%d.html", jsoupDoc.location().hashCode()),
                jsoupDoc.location(), false);
        DocumentManager.writeDocument(documentText, indexerDoc);
        controller.insertDocument(indexerDoc);
        seed.setProcessed(true);
        controller.advancedSeedUpdate(seed);
        controller.close();
        urlSet.add(seed.getUrl());
        return new WebCrawlerState(state.getRobotsTxt(),seeds,url);
    }
}
