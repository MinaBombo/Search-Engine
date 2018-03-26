package Util;

import Indexer.Document;
import Indexer.Word;
import opennlp.tools.stemmer.PorterStemmer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;

public class DocumentReader {
    public static void readDocumentWords(Document document) {
        PorterStemmer stemmer = new PorterStemmer();
        HashSet<String> words = new HashSet<>();
        try(Scanner scanner = new Scanner(new BufferedReader(new FileReader(PathFinder.getDocumentPath(document.getName()).toFile())))){
            while (scanner.hasNext()){
                words.add(stemmer.stem(scanner.next()));
            }
            for(String text : words){
                document.getWords().add(new Word(text,document));
            }
        }
        catch (IOException exception){
            System.err.println("Error in reading document");
            System.err.println(exception.getMessage());
        }
    }

}
