package ru.tp.lingany.lingany.fragments.fragmentData;


public class TranslatorData {
    private boolean isFilled;
    private String nativeLanguage;
    private String foreignLanguage;
    private String reflectionId;

    private String wordToTranslate;
    private String wordTranslation;

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

    public String getReflectionId() {
        return reflectionId;
    }

    public void setReflectionId(String reflectionId) {
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
}
