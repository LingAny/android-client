package ru.tp.lingany.lingany.sdk.api.trainings;


import java.util.UUID;

import ru.tp.lingany.lingany.sdk.api.categories.Category;


public class Training {

    private UUID id;

    private String href;

    private String title;

    private Category category;

    private String nativeWord;

    private String foreignWord;

    public Training(UUID id, String href, String title, Category category, String nativeWord) {
        this.id = id;
        this.href = href;
        this.title = title;
        this.category = category;
        this.nativeWord = nativeWord;
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

    public Category getCategory() {
        return category;
    }

    public String getNativeWord() {
        return nativeWord;
    }

    public String getForeignWord() {
        return foreignWord;
    }
}
