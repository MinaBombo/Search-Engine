package Util;

public class Seed {
    public Boolean getProcessed() {
        return processed;
    }

    public void setProcessed(Boolean processed) {
        this.processed = processed;
    }

    private String url;

    public Seed(Integer id,String url, Boolean processed) {
        this.url = url;
        this.processed = processed;
        this.id = id;
    }

    public String getUrl() {

        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private Boolean processed;

    public Integer getId() {
        return id;
    }

    public Seed(String url, Boolean processed) {
        this.url = url;
        this.processed = processed;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    private Integer id;

}
