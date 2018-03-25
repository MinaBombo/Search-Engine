package Util;

import Indexer.Document;
import Indexer.Word;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;

public class DocumentReader {

    public String getDocumentPath(String documentName){
        return ClassLoader.getSystemClassLoader().getResource(documentName).getFile();
    }
    public Document readDocument(String documentName) {
        Document document = new Document();
        document.setPath(documentName);
        document.setUrl("Test URL");
        HashSet<Word> words = document.getWords();
        try(Scanner scanner = new Scanner(new BufferedReader(new FileReader(getDocumentPath(documentName))))){
            while (scanner.hasNext()){
                words.add(new Word(scanner.next(),document));
            }
        }
        catch (IOException exception){
            System.err.println("Error in reading document");
            System.err.println(exception.getMessage());
        }
        return document;
    }

}
