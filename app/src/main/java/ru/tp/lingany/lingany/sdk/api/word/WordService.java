package ru.tp.lingany.lingany.sdk.api.word;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.interfaces.ParsedRequestListener;

import ru.tp.lingany.lingany.sdk.api.reflections.Reflection;

public class WordService {
    private final String url;

    public WordService(String url) {
        this.url = url;
    }

    public void getTranslationByText(String refId, String text, ParsedRequestListener<Word> listener) {
        AndroidNetworking.get(url + "{refId}" + "{text}")
                .addPathParameter("refId", refId)
                .addPathParameter("text", text)
                .setTag(this)
                .setPriority(Priority.HIGH)
                .build()
                .getAsObject(Reflection.class, listener);
    }
}


