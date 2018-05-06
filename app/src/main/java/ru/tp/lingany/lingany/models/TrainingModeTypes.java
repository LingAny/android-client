package ru.tp.lingany.lingany.models;

import java.util.HashMap;

public class TrainingModeTypes {

    public static enum Type {SPRINT, TRANSLATION, TYPING_MODE, STUDY_MODE}

    private static HashMap<Type, String> type2TitleMap;
    static {
        type2TitleMap = new HashMap<>();
        type2TitleMap.put(Type.SPRINT, "Sprint");
        type2TitleMap.put(Type.TRANSLATION, "Translation");
        type2TitleMap.put(Type.TYPING_MODE, "Type");
        type2TitleMap.put(Type.STUDY_MODE, "Study");
    }

    public static String getTitle(Type type) {
        return type2TitleMap.get(type);
    }
}
