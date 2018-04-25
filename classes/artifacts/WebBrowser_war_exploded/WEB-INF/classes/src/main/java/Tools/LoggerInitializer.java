package Tools;

import java.io.IOException;
import java.util.logging.*;

public class LoggerInitializer {
    public static Boolean initLogger(Logger logger){
        try{
            FileHandler fileHandler = new FileHandler(logger.getName()+".log");
            fileHandler.setLevel(Level.WARNING);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.setLevel(Level.WARNING);
            logger.addHandler(fileHandler);
            logger.setUseParentHandlers(false);
        }
        catch (IOException exception){
            System.err.println("Couldn't create log file");
        }
        return true;
    }
}
