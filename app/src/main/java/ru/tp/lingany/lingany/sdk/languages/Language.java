package ru.tp.lingany.lingany.sdk.languages;

import java.util.UUID;

/**
 * Created by anton on 29.03.18.
 */

public class Language {

    private UUID id;

    private String href;

    private String title;

    public Language(UUID id, String href, String title) {
        this.id = id;
        this.href = href;
        this.title = title;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
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
