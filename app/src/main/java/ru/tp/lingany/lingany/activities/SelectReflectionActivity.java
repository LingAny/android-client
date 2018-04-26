package ru.tp.lingany.lingany.activities;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import ru.tp.lingany.lingany.R;
import ru.tp.lingany.lingany.fragments.SelectNativeLangFragment;
import ru.tp.lingany.lingany.fragments.SelectForeignLangFragment;
import ru.tp.lingany.lingany.sdk.languages.Language;

public class SelectReflectionActivity extends AppCompatActivity
        implements SelectNativeLangFragment.NativeLangClickListener {

    private List<Language> supportedLanguages;
    private Integer nativeLangIdx;

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
        this.supportedLanguages = supportedLanguages;
        this.nativeLangIdx = position;
        Toast.makeText(SelectReflectionActivity.this, "Hello", Toast.LENGTH_SHORT).show();
    }
}
