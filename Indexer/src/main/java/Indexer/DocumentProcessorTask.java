package Indexer;


import Database.DatabaseController;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.Callable;

import static Util.DocumentReader.*;

public class DocumentProcessorTask implements Callable<Boolean> {

    private Document document;

    public DocumentProcessorTask(Document document) {
        this.document = document;

    }

    @Override
    public Boolean call() {
        try {
            DatabaseController controller = new DatabaseController();
            readDocumentWords(document);
            controller.insertWords(document.getWords());
            document.setProcessed(true);
            controller.updateDocument(document);
            controller.close();
        } catch (SQLException exception) {
            System.err.println("Couldn't open connection");
            System.err.println(exception.getMessage());
            return false;
        }
        return true;
    }
}
