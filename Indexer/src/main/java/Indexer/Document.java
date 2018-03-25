package Indexer;

import java.util.ArrayList;
import java.util.HashSet;

public class Document {
    private Integer id;
    private String path;
    private String url;
    private HashSet<Word> words;
    public Document(){
        words = new HashSet<Word>();
    }
    public Integer getId() {
        return id;
    }

    public String getPath() {
        return path;
    }

    public String getUrl() {
        return url;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public HashSet<Word> getWords() {
        return words;
    }
}
