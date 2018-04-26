package ru.tp.lingany.lingany.activities;


import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;

import java.util.ArrayList;
import java.util.List;

import ru.tp.lingany.lingany.R;
import ru.tp.lingany.lingany.fragments.LoadingFragment;
import ru.tp.lingany.lingany.fragments.SelectForeignLangFragment;
import ru.tp.lingany.lingany.fragments.SelectNativeLangFragment;
import ru.tp.lingany.lingany.sdk.Api;
import ru.tp.lingany.lingany.sdk.languages.Language;


public class SelectReflectionActivity extends AppCompatActivity implements
        SelectNativeLangFragment.NativeLangClickListener,
        SelectForeignLangFragment.ForeignLangClickListener,
        LoadingFragment.RefreshListener {

    private Language nativeLang;

    private Language foreignLang;

    private List<Language> supportedLanguages;

    private LoadingFragment loadingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_reflection);

        loadingFragment = new LoadingFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, loadingFragment)
                .commit();

        Api.getInstance().languages().getAll(listener);
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

    @Override
    public void onRefresh() {
        loadingFragment.startLoading();
        Api.getInstance().languages().getAll(listener);
    }

    private final ParsedRequestListener<List<Language>> listener = new ParsedRequestListener<List<Language>>() {
        @Override
        public void onResponse(List<Language> languages) {
            supportedLanguages = languages;

            loadingFragment.stopLoading();

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.container, SelectNativeLangFragment.getInstance(supportedLanguages))
                            .commit();
                }
            }, 300);
        }

        @Override
        public void onError(ANError anError) {
            loadingFragment.showRefresh();
            Toast.makeText(SelectReflectionActivity.this, "can't load", Toast.LENGTH_SHORT).show();
        }
    };
}
