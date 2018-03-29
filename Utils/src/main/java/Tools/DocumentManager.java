package Tools;

import BusinessModel.Document;
import BusinessModel.Word;
import opennlp.tools.stemmer.PorterStemmer;

import java.io.*;
import java.util.HashSet;
import java.util.Scanner;

public class DocumentManager {
    public static void readDocumentWords(Document document) {
        PorterStemmer stemmer = new PorterStemmer();
        HashSet<String> words = new HashSet<>();
        try (Scanner scanner = new Scanner(new BufferedReader(new FileReader(PathFinder.getDocumentPath(document.getName()).toFile())))) {
            while (scanner.hasNext()) {
                words.add(stemmer.stem(scanner.next()));
            }
            for (String text : words) {
                document.getWords().add(new Word(text, document));
            }
        } catch (IOException exception) {
            System.err.println("Error in reading document");
            System.err.println(exception.getMessage());
        }
    }

    public static void writeDocument(String text, Document document) {

        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(PathFinder.getDocumentPath(document.getName()).toFile())))) {
            writer.write(text);
        } catch (IOException exception) {
            System.err.println("Error Writing Document");
            System.err.println(exception.getMessage());
        }
    }

}
