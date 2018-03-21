package crawler;

import java.io.*;
import java.util.*;

public class Spider {
    private int maxNumPages;
    private int maxNumThreads;

    private Set<String> visitedPages;
    private LinkedList<String> toBeVisitedPages;

    private final static String resourcesDir  = "../../resources/";
    private final static String privateFolderName = "crawler";
    private final static String graphFileName = "graph.txt";
    private static int crawlerCount = 0;
    private BufferedWriter bufferedWriter;

    public Spider(int numPages, int numThreads){
        maxNumPages = numPages;
        maxNumThreads = numThreads;
    }

    private void seedList(String seedFilePath){
        toBeVisitedPages = new LinkedList<>();
        try {
            File file = new File(seedFilePath);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String url;
            while ((url = bufferedReader.readLine()) != null)
                toBeVisitedPages.addFirst(url);

            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createGraphFile(){
        try {
            Writer fileWriter = new FileWriter(
                    resourcesDir+privateFolderName+String.valueOf(crawlerCount++)+graphFileName);
            bufferedWriter = new BufferedWriter(fileWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeGraphFile(){
        if (bufferedWriter != null) {
            try {
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    synchronized String getNextUrl(){
        String url;
        url = toBeVisitedPages.getLast();
        toBeVisitedPages.removeLast();
        visitedPages.add(url);
        return url;
    }

    synchronized void addUrlsToVisit(String... urls){
        for (String url: urls) {
            if(!visitedPages.contains(url))
                visitedPages.add(url);
        }
    }

    synchronized void addEdgesToGraph(String urlPointing, String... urlsPointedAt){
        for (String url: urlsPointedAt){
            try {
                bufferedWriter.write(urlPointing + " " + url);
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public void crawl(String seedFilePath){
        seedList(seedFilePath);

        Leg[] legs = new Leg[maxNumThreads];
        for(int i=0; i<maxNumThreads; ++i){
            legs[i] = new Leg(this);
            legs[i].run();
        }

        for (Leg leg:legs) {
            try{
                leg.join();
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        closeGraphFile();
    }
    public static void main(String [] args)
    {
        System.out.println("Testing Crawler");
    }
}
