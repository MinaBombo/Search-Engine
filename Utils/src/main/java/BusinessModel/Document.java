package BusinessModel;

import java.util.ArrayList;
import java.util.List;

public class Document {
    private Integer id;
    private String name;
    private String url;
    private Boolean processed;
    private List<Word> words;

    public Document(){
        words = new ArrayList<>();
    }
    public Document(Integer id , String name, String url,boolean processed){
        setId(id);
        setName(name);
        setUrl(url);
        setProcessed(processed);
        words = new ArrayList<>();
    }
    public Document(String name, String url,boolean processed){
        setName(name);
        setUrl(url);
        setProcessed(processed);
        words = new ArrayList<>();
    }

    public Boolean isProcessed() {
        return processed;
    }
    public void setProcessed(Boolean processed) {
        this.processed = processed;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    public List<Word> getWords() {
        return words;
    }
}
