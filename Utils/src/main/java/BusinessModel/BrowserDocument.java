package BusinessModel;

public class BrowserDocument {
    String name;
    String url;
    Integer id;
    String description;

    public BrowserDocument(String name, String url, Integer id, String description) {
        this.name = name;
        this.url = url;
        this.id = id;
        this.description = description;
    }

    public String getDescription() {

        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BrowserDocument(Integer id, String name, String url) {
        this.name = name;
        this.url = url;
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
}
