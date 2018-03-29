package crawler;

import Database.DatabaseController;
import Util.DocumentManager;
import Util.Seed;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class WebCrawlingTask implements Callable<List<Seed>> {

    private Seed seed;

    public WebCrawlingTask(Seed seed) {
        this.seed = seed;
    }

    @Override
    public List<Seed> call() throws Exception {
        DatabaseController controller;
        String documentText;
        List<Seed> seeds;
        //First try taking a connection, if can't do then simply exit the task
        try {
            controller = new DatabaseController();
        } catch (SQLException exception) {
            System.err.println("An error happened while getting the connection");
            System.err.println(exception.getMessage());
            exception.printStackTrace();
            throw exception;
        }
        //Now try to get the html document from the web, if for any reason you failed, delete this seed and exit the task
        Document doc;
        try {
            doc = Jsoup.connect(seed.getUrl()).get();

            Elements links = doc.select("a[href]");
            documentText = doc.body().text();
            seeds = new ArrayList<>();
            for (Element link : links) {
                seeds.add(new Seed(link.attr("abs:href"), false));
            }
        } catch (Exception exception) {
            System.err.println("Error happened while getting the document from the web");
            exception.printStackTrace();
            controller.deleteSeed(seed);
            controller.close();
            throw exception;
        }
        //If every thing succedded then insert the document and write it to the disk
        Indexer.Document document = new Indexer.Document();
        document.setUrl(doc.location());
        document.setName(String.format("%d.html", doc.location().hashCode()));
        document.setProcessed(false);
        DocumentManager.writeDocument(documentText, document);
        controller.insertDocument(document);
        seed.setProcessed(true);
        controller.updateSeed(seed);
        controller.close();
        return seeds;
    }
}
