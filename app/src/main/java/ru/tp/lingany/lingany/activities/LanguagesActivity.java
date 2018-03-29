package ru.tp.lingany.lingany.activities;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import java.util.List;

import ru.tp.lingany.lingany.R;
import ru.tp.lingany.lingany.managers.JsonManager;
import ru.tp.lingany.lingany.managers.NetworkManager;
import ru.tp.lingany.lingany.models.Language;

public class LanguagesActivity extends AppCompatActivity {

    private ProgressBar progress;
    private RecyclerView langRecyclerView;

    private final NetworkManager.OnRequestCompleteListener listener =
            new NetworkManager.OnRequestCompleteListener() {
                @Override
                public void onRequestComplete(final String body) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            JsonManager<Language> manager = new JsonManager<>(body);
                            List<Language> languages = manager.toObjectList(Language[].class);
                            progress.setVisibility(View.INVISIBLE);
                            categoryAdapter = new LanguageAdapter(categories);
                            categoryRecyclerView.setAdapter(categoryAdapter);
                        }
                    });
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

        NetworkManager.getInstance().get("http://185.143.172.57/api/v1/lingany-da/languages/", listener);
    }
}
