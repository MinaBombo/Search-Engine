package crawler;

public class Leg extends Thread{

    private final Spider parentSpider;

    Leg(Spider spider){
        parentSpider = spider;
    }


    @Override
    public void run() {
        String url = parentSpider.getNextUrl();
        // TODO: Download page, extract urls and add to graph
        // TODO: Add newly extracted urls to parentSpider
    }
}
