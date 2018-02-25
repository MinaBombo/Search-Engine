package crawler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Spider {
    private int maxNumPages;
    private int maxNumThreads;

    private Set<String>  visitedPages;
    private List<String> toBeVisitedPages;

    private Map<String, String> graphBuilder;
    private final static String resourcesDir  = "../../resources/";
    private final static String graphFileName = "Graph.txt";

    public Spider(int numPages, int numThreads){
        maxNumPages = numPages;
        maxNumThreads = numThreads;
    }

    private void seedList(String seedFilePath){
        toBeVisitedPages = new ArrayList<>();
        try {
            File file = new File(seedFilePath);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String url;
            while ((url = bufferedReader.readLine()) != null)
                toBeVisitedPages.add(url);

            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void crawl(String seedFilePath){
        seedList(seedFilePath);
        Leg[] legs = new Leg[maxNumThreads];
        for(int i=0; i<maxNumThreads; ++i){
            legs[i] = new Leg();
            legs[i].run();
        }
    }

}
