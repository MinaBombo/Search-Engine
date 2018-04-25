package Tools;

import BusinessModel.Document;
import BusinessModel.Word;
import opennlp.tools.stemmer.PorterStemmer;

import java.io.*;
import java.nio.file.Files;


public class DocumentManager {
    public static void readDocumentWords(Document document) {
        PorterStemmer stemmer = new PorterStemmer();
        try {
            String text = new String(Files.readAllBytes(PathFinder.getDocumentPath(document.getName())));
            String words[] = text.replaceAll("[^\\p{L} ]", " ").toLowerCase().split("\\s+");
            String stemmedWord;
            for (String word : words) {
                stemmedWord = stemmer.stem(word.toLowerCase());
                if (stemmedWord.length() > 1) {
                    document.getWords().add(new Word(stemmedWord, document));
                }
            }
        } catch (IOException exception)

        {
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
