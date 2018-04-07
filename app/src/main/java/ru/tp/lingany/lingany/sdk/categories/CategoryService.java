package ru.tp.lingany.lingany.sdk.categories;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.interfaces.ParsedRequestListener;

import java.util.List;
import java.util.UUID;


public class CategoryService {
    private final String url;

    public CategoryService(String url) {
        this.url = url;
    }

    public void getAll(ParsedRequestListener<List<Category>> listener) {
        AndroidNetworking.get(url)
                .setTag(this)
                .setPriority(Priority.HIGH)
                .build()
                .getAsObjectList(Category.class, listener);
    }

    public void getById(UUID uid, ParsedRequestListener<Category> listener) {
        AndroidNetworking.get(url + "{uid}")
                .addPathParameter("uid", uid.toString())
                .setTag(this)
                .setPriority(Priority.HIGH)
                .build()
                .getAsObject(Category.class, listener);
    }

    public void getForReflection(UUID refId, ParsedRequestListener<Category> listener) {
        AndroidNetworking.get(url + "reflection/{uid}")
                .addPathParameter("uid", refId.toString())
                .setTag(this)
                .setPriority(Priority.HIGH)
                .build()
                .getAsObjectList(Category.class, listener);
    }
}
