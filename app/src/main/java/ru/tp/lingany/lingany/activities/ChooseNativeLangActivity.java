package ru.tp.lingany.lingany.activities;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;

import java.util.List;

import ru.tp.lingany.lingany.R;
import ru.tp.lingany.lingany.adapters.LanguagesAdapter;
import ru.tp.lingany.lingany.sdk.Api;
import ru.tp.lingany.lingany.sdk.languages.Language;

public class ChooseNativeLangActivity extends AppCompatActivity {

    private ProgressBar progress;
    private RecyclerView langRecyclerView;
    private TextView title;

    private final ParsedRequestListener<List<Language>> listener = new ParsedRequestListener<List<Language>>() {
        @Override
        public void onResponse(List<Language> languages) {
            title.setText(getString(R.string.chooseNativeLang));
            progress.setVisibility(View.INVISIBLE);
            langRecyclerView.setAdapter(new LanguagesAdapter(languages));
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

        title = findViewById(R.id.title);
        title.setText(getString(R.string.loadSupportedLang));

        Api.getInstance().languages().getAll(listener);
    }
}
