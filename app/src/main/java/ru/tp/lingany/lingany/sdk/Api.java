package ru.tp.lingany.lingany.sdk;

import ru.tp.lingany.lingany.sdk.languages.LanguageService;
import ru.tp.lingany.lingany.sdk.reflections.ReflectionService;


public class Api {

    private static final Api INSTANCE = new Api();
    private static final String TAG = "API";

    private LanguageService languageService;
    private ReflectionService reflectionService;

    private Api() {
        this.languageService = new LanguageService("http://185.143.172.57/api/v1/lingany-da/languages/");
        this.reflectionService = new ReflectionService("http://185.143.172.57/api/v1/lingany-da/reflections/");
    }

    public static Api getInstance() {
        return INSTANCE;
    }

    public LanguageService languages() {
        return languageService;
    }

    public ReflectionService reflections() {
        return reflectionService;
    }
}
