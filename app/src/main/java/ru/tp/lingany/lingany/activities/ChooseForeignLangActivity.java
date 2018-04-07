package ru.tp.lingany.lingany.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ru.tp.lingany.lingany.R;
import ru.tp.lingany.lingany.adapters.LanguagesAdapter;
import ru.tp.lingany.lingany.sdk.languages.Language;

public class ChooseForeignLangActivity extends AppCompatActivity {

    private TextView title;

    private List<Language> languages;
    private Language nativeLang;

    public static final String EXTRA_SUPPORTED_LANG = "EXTRA_SUPPORTED_LANG";
    public static final String EXTRA_EXCLUDED_LANG = "EXTRA_EXCLUDED_LANG";

    private class LangClickListener implements LanguagesAdapter.ItemClickListener {

        @Override
        public void onClick(View view, int position) {
            Log.i("tag", "[LangClickListener.onClick]");

            Language foreignLang = languages.get(position);

            Intent intent = new Intent(ChooseForeignLangActivity.this, ReflectionActivity.class);
            intent.putExtra(ReflectionActivity.EXTRA_NATIVE_LANG, (Serializable) nativeLang);
            intent.putExtra(ReflectionActivity.EXTRA_FOREIGN_LANG, (Serializable) foreignLang);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_foreign_lang);

        Intent intent = getIntent();
        List<Language> supportedLanguages = (List<Language>) intent.getSerializableExtra(EXTRA_SUPPORTED_LANG);
        int excluded = intent.getIntExtra(EXTRA_EXCLUDED_LANG, -1);

        languages = new ArrayList<>(supportedLanguages);
        nativeLang = supportedLanguages.get(excluded);
        languages.remove(excluded);


        RecyclerView langRecyclerView = findViewById(R.id.languages);
        RecyclerView.LayoutManager categoryLayoutManager = new LinearLayoutManager(this);
        langRecyclerView.setLayoutManager(categoryLayoutManager);

        title = findViewById(R.id.title);
        title.setText(getString(R.string.chooseForeignLang));
        langRecyclerView.setAdapter(new LanguagesAdapter(languages, new ChooseForeignLangActivity.LangClickListener()));
    }
}
