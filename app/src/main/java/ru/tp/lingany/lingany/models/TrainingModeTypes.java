package ru.tp.lingany.lingany.models;

import java.util.HashMap;

public class TrainingModeTypes {

    public static enum Type { SPRINT_N2F, SPRINT_F2N, TRANSLATION_N2F, TRANSLATION_F2N}

    private static HashMap<Type, String> type2TitleMap;
    static {
        type2TitleMap = new HashMap<>();
        type2TitleMap.put(Type.SPRINT_N2F, "Sprint, Native to Foreign");
        type2TitleMap.put(Type.SPRINT_F2N, "Sprint, Foreign to Native");
        type2TitleMap.put(Type.TRANSLATION_N2F, "Translation, Native to Foreign");
        type2TitleMap.put(Type.TRANSLATION_F2N, "Translation, Foreign to Native");
    }

    public static String getTitle(Type type) {
        return type2TitleMap.get(type);
    }
}
