package BusinessModel;

public class Seed {

    private Boolean processed;
    private String url;
    private Integer id;

    public Seed(Integer id,String url, Boolean processed) {
        this.url = url;
        this.processed = processed;
        this.id = id;
    }
    public Seed(String url, Boolean processed) {
        this.url = url;
        this.processed = processed;
    }

    public Boolean isProcessed() {
        return processed;
    }
    public void setProcessed(Boolean processed) {
        this.processed = processed;
    }

    public String getUrl(){return url;}
    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
}
