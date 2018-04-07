package ru.tp.lingany.lingany.activities;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;

import java.util.List;

import ru.tp.lingany.lingany.R;
import ru.tp.lingany.lingany.adapters.LanguagesAdapter;
import ru.tp.lingany.lingany.sdk.Api;
import ru.tp.lingany.lingany.sdk.languages.Language;
import ru.tp.lingany.lingany.sdk.reflections.Reflection;

public class LanguagesActivity extends AppCompatActivity {

    private ProgressBar progress;
    private RecyclerView langRecyclerView;

    private final ParsedRequestListener<List<Language>> listener = new ParsedRequestListener<List<Language>>() {
        @Override
        public void onResponse(List<Language> languages) {
            progress.setVisibility(View.INVISIBLE);
            langRecyclerView.setAdapter(new LanguagesAdapter(languages));
        }

        @Override
        public void onError(ANError anError) {
            Log.e("tag", anError.toString());
        }
    };

    private final ParsedRequestListener<List<Reflection>> test = new ParsedRequestListener<List<Reflection>>() {
        @Override
        public void onResponse(List<Reflection> response) {
            Log.i("tag", "onResponse");
        }

        @Override
        public void onError(ANError anError) {
            Log.e("tag", anError.toString());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_lang);
        progress = findViewById(R.id.progress);
        progress.setVisibility(View.VISIBLE);

        langRecyclerView = findViewById(R.id.languages);

        RecyclerView.LayoutManager categoryLayoutManager = new LinearLayoutManager(this);
        langRecyclerView.setLayoutManager(categoryLayoutManager);

//        Api.getInstance().languages().getAll(listener);

        Api api = Api.getInstance();
        api.reflections().getAll(test);


//        NetworkManager.getInstance().get("http://185.143.172.57/api/v1/lingany-da/languages/", listener);
    }
}
