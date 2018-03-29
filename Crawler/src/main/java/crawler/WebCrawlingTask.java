package crawler;

import Database.DatabaseController;
import Util.DocumentManager;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class WebCrawlingTask implements Callable<List<String>> {

    private String url;
    public WebCrawlingTask(String url){
        this.url = url;
    }
    @Override
    public List<String> call() throws Exception {
        try{
            Document doc = Jsoup.connect(url).get();
            Elements links = doc.select("a[href]");
            String text = doc.body().text();
            List<String> linkText = new ArrayList<>();
            for(Element link : links){
                linkText.add(link.attr("abs:href"));
            }
            Indexer.Document document = new Indexer.Document();
            document.setUrl(doc.location());
            document.setName(String.format("%d",doc.location().hashCode()));
            document.setProcessed(false);
            DocumentManager.writeDocument(text,document);
            DatabaseController controller = new DatabaseController();
            controller.insertDocument(document);
            return linkText;
        }
        catch (IOException exception){
            System.err.println("An error happened while crawling");
            System.err.println(exception.getMessage());
            exception.printStackTrace();
        }
        return null;
    }
}
