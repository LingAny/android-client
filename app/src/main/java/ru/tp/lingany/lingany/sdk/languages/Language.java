package ru.tp.lingany.lingany.sdk.languages;

import java.io.Serializable;
import java.util.UUID;


public class Language implements Serializable {

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

    public String getHref() {
        return href;
    }

    public String getTitle() {
        return title;
    }
}
