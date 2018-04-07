package ru.tp.lingany.lingany.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import ru.tp.lingany.lingany.R;
import ru.tp.lingany.lingany.sdk.languages.Language;

public class ChooseForeignLangActivity extends AppCompatActivity {

    private ProgressBar progress;
    private RecyclerView langRecyclerView;
    private TextView title;

    public static final String EXTRA_SUPPORTED_LANG = "EXTRA_SUPPORTED_LANG";
    public static final String EXTRA_EXCLUDED_LANG = "EXTRA_EXCLUDED_LANG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        String s = getIntent().getStringExtra("EXTRA_SESSION_ID");
        Intent intent = getIntent();
        List<Language> languages = (List<Language>) intent.getSerializableExtra(EXTRA_SUPPORTED_LANG);

        setContentView(R.layout.activity_lang);

//        progress = findViewById(R.id.progress);
//        progress.setVisibility(View.VISIBLE);
//
//        langRecyclerView = findViewById(R.id.languages);
//
//        RecyclerView.LayoutManager categoryLayoutManager = new LinearLayoutManager(this);
//        langRecyclerView.setLayoutManager(categoryLayoutManager);
//
//        title = findViewById(R.id.title);
//        title.setText(getString(R.string.loadSupportedLang));
    }
}
