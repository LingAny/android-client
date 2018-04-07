package ru.tp.lingany.lingany.sdk.categories;

import java.util.UUID;

import ru.tp.lingany.lingany.sdk.reflections.Reflection;


public class Category {

    private UUID id;

    private String href;

    private String title;

    private Reflection reflection;

    public Category(UUID id, String href, String title, Reflection reflection) {
        this.id = id;
        this.href = href;
        this.title = title;
        this.reflection = reflection;
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

    public Reflection getReflection() {
        return reflection;
    }
}
