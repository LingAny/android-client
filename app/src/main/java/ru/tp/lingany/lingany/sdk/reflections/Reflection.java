package ru.tp.lingany.lingany.sdk.reflections;

import java.util.UUID;

import ru.tp.lingany.lingany.sdk.languages.Language;

/**
 * Created by anton on 07.04.18.
 */

public class Reflection {

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

    public Language getNativeLanguage() {
        return nativeLanguage;
    }

    public void setNativeLanguage(Language nativeLanguage) {
        this.nativeLanguage = nativeLanguage;
    }

    public Language getForeignLanguage() {
        return foreignLanguage;
    }

    public void setForeignLanguage(Language foreignLanguage) {
        this.foreignLanguage = foreignLanguage;
    }
}
