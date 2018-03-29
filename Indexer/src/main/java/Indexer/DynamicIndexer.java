package Indexer;

import BusinessModel.Document;
import Database.ConnectionPool;
import Database.DatabaseController;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static Tools.ThreadCounter.getNumThreads;

public class DynamicIndexer {


    public static void main(String[] args) throws SQLException, InterruptedException {
        int numThreads = getNumThreads(args[0]);
        ExecutorService pool =  Executors.newFixedThreadPool(numThreads);
        ConnectionPool.getInstance().setInitialSize(numThreads);
        DatabaseController controller = new DatabaseController();
        List<Document> documents;
        do{
            documents = controller.getUnprocessedDocuments();
            List<DocumentProcessorTask> tasks = new ArrayList<>();
            for(Document document :documents){
                tasks.add(new DocumentProcessorTask(document));
            }
            try {
                List<Future<Boolean>> taskResults = pool.invokeAll(tasks);
                for(Future<Boolean> result : taskResults){
                    result.get();
                }
            } catch (ExecutionException exception){
                exception.printStackTrace();
            }

        }while(!documents.isEmpty());
        pool.shutdown();
        controller.close();
        try {
            ConnectionPool.getInstance().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
