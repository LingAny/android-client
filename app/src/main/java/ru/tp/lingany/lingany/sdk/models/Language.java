package ru.tp.lingany.lingany.sdk.models;

/**
 * Created by anton on 29.03.18.
 */

public class Language {

    private String id;

    private String href;

    private String title;

    public Language(String id, String href, String title) {
        this.id = id;
        this.href = href;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
