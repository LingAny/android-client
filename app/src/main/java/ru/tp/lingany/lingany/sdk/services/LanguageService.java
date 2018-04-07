package ru.tp.lingany.lingany.sdk.services;

import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.interfaces.ParsedRequestListener;

import java.util.List;

import ru.tp.lingany.lingany.sdk.models.Language;


public class LanguageService {

    private final String url;

    public LanguageService(String url) {
        this.url = url;
    }

    public void getAll(ParsedRequestListener<List<Language>> listener) {
        Log.i("LanguageService", "getAll  by url: " + url);
        AndroidNetworking.get(url)
                .setTag(this)
                .setPriority(Priority.HIGH)
                .build()
                .getAsObjectList(Language.class, listener);
    }

}
