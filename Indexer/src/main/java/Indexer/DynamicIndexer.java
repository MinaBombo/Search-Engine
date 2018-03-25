package Indexer;

import Database.DatabaseController;
import Util.DocumentReader;

import java.sql.SQLException;
import java.util.ArrayList;

public class DynamicIndexer {

    public static void main(String[] args) throws SQLException {
        DatabaseController controller = new DatabaseController();
        DocumentReader reader = new DocumentReader();
        for (int i = 0; i < 25; i++) {
            Document document = reader.readDocument("TestDoc2.html");
            controller.insertDocument(document);
            controller.insertWords(new ArrayList<>(document.getWords()));
            Document document1 = reader.readDocument("TestDoc3.html");
            controller.insertDocument(document1);
            controller.insertWords(new ArrayList<>(document1.getWords()));
            Document document2 = reader.readDocument("TestDoc4.html");
            controller.insertDocument(document2);
            controller.insertWords(new ArrayList<>(document2.getWords()));
            Document document3 = reader.readDocument("TestDoc5.html");
            controller.insertDocument(document3);
            controller.insertWords(new ArrayList<>(document3.getWords()));
            Document document4 = reader.readDocument("TestDoc6.html");
            controller.insertDocument(document4);
            controller.insertWords(new ArrayList<>(document4.getWords()));
            Document document5 = reader.readDocument("TestDoc7.html");
            controller.insertDocument(document5);
            controller.insertWords(new ArrayList<>(document5.getWords()));
        }
    }
}
