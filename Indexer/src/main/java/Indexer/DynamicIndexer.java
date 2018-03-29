package Indexer;

import Database.ConnectionPool;
import Database.DatabaseController;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class DynamicIndexer {

    public static final int numThreads = 5;
    public static void main(String[] args) throws SQLException, InterruptedException {
        DatabaseController controller = new DatabaseController();
        ExecutorService pool =  Executors.newFixedThreadPool(numThreads);
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
