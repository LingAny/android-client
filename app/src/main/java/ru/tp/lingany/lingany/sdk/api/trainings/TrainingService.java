package ru.tp.lingany.lingany.sdk.api.trainings;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.interfaces.ParsedRequestListener;

import java.util.List;
import java.util.UUID;

import ru.tp.lingany.lingany.sdk.api.categories.Category;


public class TrainingService {

    private final String url;

    public TrainingService(String url) {
        this.url = url;
    }

    public void getAll(ParsedRequestListener<List<Training>> listener) {
        AndroidNetworking.get(url)
                .setTag(this)
                .setPriority(Priority.HIGH)
                .build()
                .getAsObjectList(Training.class, listener);
    }

    public void getById(UUID uid, ParsedRequestListener<Training> listener) {
        AndroidNetworking.get(url + "{uid}")
                .addPathParameter("uid", uid.toString())
                .setTag(this)
                .setPriority(Priority.HIGH)
                .build()
                .getAsObject(Training.class, listener);
    }

    public void getForCategory(UUID catId, ParsedRequestListener<List<Training>> listener) {
        AndroidNetworking.get(url + "category/{uid}")
                .addPathParameter("uid", catId.toString())
                .setTag(this)
                .setPriority(Priority.HIGH)
                .build()
                .getAsObjectList(Training.class, listener);
    }

    public void getForCategory(Category category, ParsedRequestListener<List<Training>> listener) {
        getForCategory(category.getId(), listener);
    }
}
