package Indexer;

import Database.DatabaseController;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DynamicIndexer {

    private static void print(Word word) {
        System.out.println(word.getId() + " " + word.getText());
    }
    private static void change(Word word){
        word.setText(word.getText()+word.getText());
    }

    public static void main(String[] args) throws SQLException {
        DatabaseController driver = new DatabaseController();
        Document document = new Document();
        document.setPath("ibfklqebfljkqebfkl");
        document.setUrl("akfhkfbajkbfh");
        driver.insertDocument(document);
        System.out.println(document.getId());
        document.setUrl("Ahmed");
        document.setPath("Hassan");
        driver.updateDocument(document);
        //driver.deleteDocument(document);
       Word word = new Word();
        word.setText("Hassan");
        word.setDocument(document);
        Word word1 = new Word();
        word1.setText("Mohamed1");
        word1.setDocument(document);
        Word word2 = new Word();
        word2.setText("Mohamed2");
        word2.setDocument(document);
        Word word3 = new Word();
        word3.setDocument(document);
        word3.setText("Mohamed3");
        Word word4 = new Word();
        word4.setText("Mohamed4");
        word4.setDocument(document);
        Word word5 = new Word();
        word5.setText("Mohamed5");
        word5.setDocument(document);
        Word word6 = new Word();
        word6.setText("Mohamed6");
        word6.setDocument(document);
        Word word7 = new Word();
        word7.setText("Mohamed7");
        word7.setDocument(document);
        Word word8 = new Word();
        word8.setText("Mohamed8");
        word8.setDocument(document);
        Word word9 = new Word();
        word9.setText("Mohamed9");
        word9.setDocument(document);
        List<Word> words = new ArrayList<>();
        words.add(word);
        words.add(word1);
        words.add(word2);
        words.add(word3);
        words.add(word4);
        words.add(word5);
        words.add(word6);
        words.add(word7);
        words.add(word8);
        words.add(word9);
        driver.insertWords(words);
        print(word);
        print(word1);
        print(word2);
        print(word3);
        print(word4);
        print(word5);
        print(word6);
        print(word7);
        print(word8);
        print(word9);
        change(word);
        change(word1);
        change(word2);
        change(word3);
        change(word4);
        change(word5);
        change(word6);
        change(word7);
        change(word8);
        change(word9);
        driver.updateWords(words);
        driver.deleteWords(words);
        driver.deleteDocument(document);
       driver.close();
    }
}
