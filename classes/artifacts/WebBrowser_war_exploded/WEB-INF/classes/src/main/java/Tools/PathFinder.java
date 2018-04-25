package Tools;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PathFinder {

    private static final String documentDirectoryPath = "../Output/HTMLDocuments";
    public static Path getDocumentPath(String documentName){
        Path path = Paths.get(documentDirectoryPath);
        try{
            Files.createDirectories(path);
        }
        catch (IOException exception){
            System.err.println("Failure to create the docs folder");
            System.err.println(exception.getMessage());
        }
        return path.resolve(documentName);

    }
}
