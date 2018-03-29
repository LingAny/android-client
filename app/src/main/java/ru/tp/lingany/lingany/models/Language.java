package ru.tp.lingany.lingany.models;

import java.util.UUID;

/**
 * Created by anton on 29.03.18.
 */

public class Language {

    private UUID uid;

    private String href;

    private String title;

    public Language(UUID uid, String href, String title) {
        this.uid = uid;
        this.href = href;
        this.title = title;
    }

    public UUID getUid() {
        return uid;
    }

    public void setUid(UUID uid) {
        this.uid = uid;
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
