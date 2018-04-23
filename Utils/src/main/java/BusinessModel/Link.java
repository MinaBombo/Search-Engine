package BusinessModel;

import java.util.List;

public class Link {
    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    private Document document;
    private String link;
    private Integer ID;

    public Link(Document document, String link, Integer ID) {
        this.document = document;
        this.link = link;
        this.ID = ID;
    }
    public Link(Document document, String link) {
        this.document = document;
        this.link = link;
    }
}
