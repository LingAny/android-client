package ru.tp.lingany.lingany.sdk.api.reflections;

import java.io.Serializable;
import java.util.UUID;

import ru.tp.lingany.lingany.sdk.api.languages.Language;


public class Reflection implements Serializable {

    private UUID id;

    private String href;

    private String title;

    private Language nativeLanguage;

    private Language foreignLanguage;

    public Reflection(UUID id, String href, String title, Language foreignLanguage) {
        this.id = id;
        this.href = href;
        this.title = title;
        this.foreignLanguage = foreignLanguage;
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

    public Language getNativeLanguage() {
        return nativeLanguage;
    }

    public Language getForeignLanguage() {
        return foreignLanguage;
    }
}
