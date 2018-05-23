package ru.tp.lingany.lingany.sdk.api.word;

import java.io.Serializable;

public class Word implements Serializable {

    private String text;

    private String translation;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }
}
