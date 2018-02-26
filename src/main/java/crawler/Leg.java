package crawler;

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
    }
}
