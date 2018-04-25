package Tools;

public class ThreadCounter {
    public static int getNumThreads(String args){
        try{
            return Integer.parseInt(args);
        } catch (Exception e){
            System.err.println("Error while parsing arguments");
            System.err.println(e.toString());
            System.err.println("Setting parameters to default values");
            return  Runtime.getRuntime().availableProcessors()*2;
        }
    }
}
