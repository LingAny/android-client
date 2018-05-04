package ru.tp.lingany.lingany.sdk;

import ru.tp.lingany.lingany.sdk.api.categories.CategoryService;
import ru.tp.lingany.lingany.sdk.api.languages.LanguageService;
import ru.tp.lingany.lingany.sdk.api.reflections.ReflectionService;
import ru.tp.lingany.lingany.sdk.api.trainings.TrainingService;


public class Api {

    private static final Api INSTANCE = new Api();

    private LanguageService languageService;

    private ReflectionService reflectionService;

    private CategoryService categoryService;

    private TrainingService trainingService;

    private Api() {
        languageService = new LanguageService("http://185.143.172.57/api/v1/lingany-da/languages/");
        reflectionService = new ReflectionService("http://185.143.172.57/api/v1/lingany-da/reflections/");
        categoryService = new CategoryService("http://185.143.172.57/api/v1/lingany-da/categories/");
        trainingService = new TrainingService("http://185.143.172.57/api/v1/lingany-da/training/");
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

    public CategoryService categories() {
        return categoryService;
    }

    public TrainingService training() {
        return trainingService;
    }
}
