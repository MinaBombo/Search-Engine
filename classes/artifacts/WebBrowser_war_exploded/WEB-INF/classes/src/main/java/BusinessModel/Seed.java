package BusinessModel;

public class Seed {

    private Boolean processed;
    private String url;
    private Integer id;

    public Integer getInLinks() {
        return inLinks;
    }

    public void setInLinks(Integer inLinks) {
        this.inLinks = inLinks;
    }

    private Integer inLinks;
    public Seed(Integer id,String url, Boolean processed,Integer inLinks) {
        this.url = url;
        this.processed = processed;
        this.id = id;
        this.inLinks = inLinks;
    }
    public Seed(String url, Boolean processed) {
        this.url = url;
        this.processed = processed;
        inLinks = 0;
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
