package Indexer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Document {
    private Integer id;
    private String name;
    private String url;

    public Boolean getProcessed() {
        return processed;
    }

    public void setProcessed(Boolean processed) {
        this.processed = processed;
    }

    private Boolean processed;
    private List<Word> words;
    public Document(){
        words = new ArrayList<>();
    }
    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Word> getWords() {
        return words;
    }

    public Document(Integer id , String name, String url,boolean processed){
        setId(id);
        setName(name);
        setUrl(url);
        setProcessed(processed);
        words = new ArrayList<>();
    }
}
