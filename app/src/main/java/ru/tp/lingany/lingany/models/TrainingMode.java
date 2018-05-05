package ru.tp.lingany.lingany.models;

import java.io.Serializable;


public class TrainingMode implements Serializable {

    private TrainingModeTypes.Type type;
    private String title;

    public TrainingMode(TrainingModeTypes.Type type) {
        this.type = type;
        title = TrainingModeTypes.getTitle(type);
    }

    public TrainingModeTypes.Type getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }
}
