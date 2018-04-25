package Database;

import BusinessModel.*;

import java.io.Closeable;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class DatabaseController implements Closeable {
    DatabaseConnector connector;
    DocumentDatabaseModule documentModule;
    WordDatabaseModule wordModule;
    WordListDatabaseModule wordsModule;
    SeedDatabaseModule seedModule;
    LinkDatabaseModule linkModule;
    RankerDocumentDatabaseModule rankerDocumentDatabaseModule;
    public DatabaseController() throws SQLException {
        connector = new DatabaseConnector();
        documentModule = new DocumentDatabaseModule(connector);
        wordModule = new WordDatabaseModule(connector);
        wordsModule = new WordListDatabaseModule(connector);
        seedModule = new SeedDatabaseModule(connector);
        linkModule = new LinkDatabaseModule(connector);
        rankerDocumentDatabaseModule = new RankerDocumentDatabaseModule(connector);
    }

    @Override
    public void close() {
        connector.close();
    }

    public void insertDocument(Document document) {
        try {
            documentModule.insert(document);
        } catch (SQLException exception) {
            handleSQLException(exception, "Error in Inserting document");
        }
    }

    public void updateDocument(Document document) {
        try {
            documentModule.update(document);
        } catch (SQLException exception) {
            handleSQLException(exception, "Error in updating document");
        }
    }

    public void deleteDocument(Document document) {
        try {
            documentModule.delete(document);
        } catch (SQLException exception) {
            handleSQLException(exception, "Error in deleting document");
        }
    }

    public void insertWord(Word word) {
        try {
            wordModule.insert(word);
        } catch (SQLException exception) {
            handleSQLException(exception, "Error in inserting new word");
        }
    }

    public void updateWord(Word word) {
        try {
            wordModule.update(word);
        } catch (SQLException exception) {
            handleSQLException(exception, "Error in updating word");
        }
    }

    public void deleteWord(Word word) {
        try {
            wordModule.delete(word);
        } catch (SQLException exception) {
            handleSQLException(exception, "Error in deleting word");
        }
    }

    public void insertWords(List<Word> words) {
        try{
            wordsModule.insert(words);
        } catch (SQLException exception ) {
            handleSQLException(exception, "Error in inserting a list of words");
        }
    }

    public void updateWords(List<Word> words) {
        try {
            wordsModule.update(words);
        } catch (SQLException exception) {
            handleSQLException(exception, "Error in updating list of words");
        }
    }

    public void deleteWords(List<Word> words) {
        try {
            wordsModule.delete(words);
        } catch (SQLException exception) {
            handleSQLException(exception, "Error in deleting list of words");
        }
    }

    public List<Document> getUnprocessedDocuments() {
        try {
            return documentModule.select();
        } catch (SQLException exception) {
            handleSQLException(exception, "Error in getting unprocessed documents");
        }
        return null;
    }
    public void insertSeed (List<Seed> seeds){
        try{
            seedModule.insert(seeds);
        }
        catch (SQLException exception){
            handleSQLException(exception,"Error in inserting Seeds");
        }
    }
    public List<Seed> getUnprocessedSeeds(int limit, int offset){
        try{
            return  seedModule.select(limit,offset);
        }
        catch (SQLException exception){
            handleSQLException(exception,"Error in getting unprocessed seeds");
        }
        return null;
    }
    public void updateSeed(Seed seed){
        try{
            seedModule.update(seed);
        }
        catch (SQLException exception){
            handleSQLException(exception,"Error in updating Seed");
        }
    }
    public void advancedSeedUpdate(Seed seed){
        try{
            seedModule.advancedUpdate(seed);
        }
        catch (SQLException exception){
            handleSQLException(exception,"Error in advanced seed update");
        }
    }
    public void deleteSeed(Seed seed){
        try {
            seedModule.delete(seed);
        }
        catch (SQLException exception){
            handleSQLException(exception,"Error in deleting Seed");
        }
    }
    public void insertLinks(List<Link> links){
        try{
            linkModule.insert(links);
        }
        catch (SQLException exception){
            handleSQLException(exception,"Error in inserting links");
        }
    }
    public void refreshSeeds(){
        try{
            seedModule.refresh();
        }
        catch (SQLException exception){
            handleSQLException(exception,"Error while refreshing seeds");
        }
    }
    public List<StaticRankerDocument> getStaticRankerDocuments(){
        try{
            return rankerDocumentDatabaseModule.getStaticRankerDocuments();
        }
        catch (SQLException excption){
            handleSQLException(excption,"Error while getting Static ranker documents");
        }
        return null;
    }    public void updateStaticRankerDocuments(List<StaticRankerDocument> staticRankerDocuments){
        try{
            rankerDocumentDatabaseModule.updateDocumentRank(staticRankerDocuments);
        }catch (SQLException exception){
            handleSQLException(exception,"Error in updating static rank for documents");
        }
    }
    public List<DynamicRankerDocument> getDynamicRankerDocuments(String [] searchWords){
        try{
            return rankerDocumentDatabaseModule.getDynamicRankerDocuments(searchWords);
        }
        catch (SQLException exception){
            handleSQLException(exception,"Error While getting Dynamic Ranker documents");
        }
        return null;
    }
    public Map<String,Integer> getWordMap(String [] searchWords){
        try{
            return rankerDocumentDatabaseModule.getWordMap(searchWords);
        }
        catch (SQLException exception){
            handleSQLException(exception,"Error while getting word map");
        }
        return null;
    }
    public Integer getNumDocuments(){
        try{
            return documentModule.getNumDocuments();
        }
        catch (SQLException exception){
            handleSQLException(exception,"Error while getting num docs");
        }
        return null;
    }
    public List<BrowserDocument> getBrowserDocuments(List<DynamicRankerDocument> rankerDocuments){
        try {
            return rankerDocumentDatabaseModule.getSortedBrowserDocuments(rankerDocuments);
        }
        catch (SQLException exception){
            handleSQLException(exception,"Error while getting browser documents");
        }
        return null;
    }
    private void handleSQLException(SQLException exception, String message) {
        System.err.println(message);
        System.err.println(exception.getSQLState());
        System.err.println(exception.getMessage());
    }

}
