package ru.tp.lingany.lingany.fragments.fragmentData;


import java.util.UUID;

public class TranslatorData {
    private boolean isFilled;
    private String nativeLanguage;
    private String foreignLanguage;
    private UUID reflectionId;

    private String wordToTranslate;
    private String wordTranslation;
    private boolean isLanguageChanged;

    public TranslatorData(UUID reflectionId) {
        this.nativeLanguage = nativeLanguage;
    }

    public boolean isFilled() {
        return isFilled;
    }

    public String getNativeLanguage() {
        return nativeLanguage;
    }

    public void setNativeLanguage(String nativeLanguage) {
        this.nativeLanguage = nativeLanguage;
    }

    public String getForeignLanguage() {
        return foreignLanguage;
    }

    public void setForeignLanguage(String foreignLanguage) {
        this.foreignLanguage = foreignLanguage;
    }

    public UUID getReflectionId() {
        return reflectionId;
    }

    public void setReflectionId(UUID reflectionId) {
        this.reflectionId = reflectionId;
    }

    public String getWordToTranslate() {
        return wordToTranslate;
    }

    public void setWordToTranslate(String wordToTranslate) {
        this.wordToTranslate = wordToTranslate;
    }

    public String getWordTranslation() {
        return wordTranslation;
    }

    public void setWordTranslation(String wordTranslation) {
        this.wordTranslation = wordTranslation;
    }

    public boolean isLanguageChanged() {
        return isLanguageChanged;
    }

    public void setLanguageChanged(boolean languageChanged) {
        isLanguageChanged = languageChanged;
    }

    public void changeLanguage() {
        boolean oldIsLanguageChanged = isLanguageChanged;
        String oldNativeLanguage = nativeLanguage;
        String oldForeignLanguage = foreignLanguage;

        if (oldIsLanguageChanged) {
            isLanguageChanged = false;
        } else {
            isLanguageChanged = true;
        }

        foreignLanguage = oldNativeLanguage;
        nativeLanguage = oldForeignLanguage;
    }
}
