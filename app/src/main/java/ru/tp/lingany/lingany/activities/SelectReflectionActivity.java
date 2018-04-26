package ru.tp.lingany.lingany.activities;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ru.tp.lingany.lingany.R;
import ru.tp.lingany.lingany.fragments.SelectNativeLangFragment;
import ru.tp.lingany.lingany.fragments.SelectForeignLangFragment;
import ru.tp.lingany.lingany.sdk.languages.Language;


public class SelectReflectionActivity extends AppCompatActivity implements
        SelectNativeLangFragment.NativeLangClickListener,
        SelectForeignLangFragment.ForeignLangClickListener {

    private Language nativeLang;

    private Language foreignLang;

    private List<Language> supportedLanguages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_reflection);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, new SelectNativeLangFragment())
                .commit();
    }

    @Override
    public void onNativeLangClick(View view, List<Language> supportedLanguages, int position) {
        Toast.makeText(SelectReflectionActivity.this, "onNativeLangClick", Toast.LENGTH_SHORT).show();

        this.supportedLanguages = supportedLanguages;

        List<Language> foreignLanguages = new ArrayList<>(supportedLanguages);
        nativeLang = supportedLanguages.get(position);
        foreignLanguages.remove(position);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, SelectForeignLangFragment.getInstance(foreignLanguages))
                .commit();
    }

    @Override
    public void onForeignLangClick(View view, int position) {
        Toast.makeText(SelectReflectionActivity.this, "onForeignLangClick", Toast.LENGTH_SHORT).show();
        foreignLang = supportedLanguages.get(position);
    }
}
