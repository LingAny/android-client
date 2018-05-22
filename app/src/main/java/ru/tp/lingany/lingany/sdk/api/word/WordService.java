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
        String string21 = url + "text/{refId}/{text}";
        AndroidNetworking.get(url + "text/{text}/{refId}")
                .addPathParameter("text", text)
                .addPathParameter("refId", refId)
                .setTag(this)
                .setPriority(Priority.HIGH)
                .build()
                .getAsObject(Word.class, listener);
    }
}


