package ru.tp.lingany.lingany.sdk;

import ru.tp.lingany.lingany.sdk.services.LanguageService;

/**
 * Created by anton on 30.03.18.
 */

public class Api {

    private static final Api INSTANCE = new Api();
    private static final String TAG = "API";

    private LanguageService languageService;

    private Api() {
        this.languageService = new LanguageService("http://185.143.172.57/api/v1/lingany-da/languages/");
    }

    public static Api getInstance() {
        return INSTANCE;
    }

    public LanguageService languages() {
        return languageService;
    }
}
