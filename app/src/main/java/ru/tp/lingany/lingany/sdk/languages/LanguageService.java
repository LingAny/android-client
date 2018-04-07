package ru.tp.lingany.lingany.sdk.languages;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.interfaces.ParsedRequestListener;

import java.util.List;
import java.util.UUID;


public class LanguageService {

    private final String url;

    public LanguageService(String url) {
        this.url = url;
    }

    public void getAll(ParsedRequestListener<List<Language>> listener) {
        AndroidNetworking.get(url)
                .setTag(this)
                .setPriority(Priority.HIGH)
                .build()
                .getAsObjectList(Language.class, listener);
    }

    public void getById(UUID uid, ParsedRequestListener<Language> listener) {
        AndroidNetworking.get(url + "{uid}")
                .addPathParameter("uid", uid.toString())
                .setTag(this)
                .setPriority(Priority.HIGH)
                .build()
                .getAsObject(Language.class, listener);
    }
}
