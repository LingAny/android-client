package ru.tp.lingany.lingany.activities;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
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
import ru.tp.lingany.lingany.sdk.reflections.Reflection;


public class SelectReflectionActivity extends AppCompatActivity implements
        SelectNativeLangFragment.NativeLangClickListener,
        SelectForeignLangFragment.ForeignLangClickListener,
        LoadingFragment.RefreshListener {

    private Language nativeLang;

    private Language foreignLang;

    private Reflection reflection;

    private List<Language> supportedLanguages;
    private List<Language> foreignLanguages;

    private LoadingFragment loadingFragment;

    private enum Request { API_GET_ALL_LANGUAGES, API_GET_REFLECTION_FOR_LANGUAGES, NONE}

    private Request currentRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_reflection);

        loadingFragment = new LoadingFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, loadingFragment)
                .commit();

        currentRequest = Request.API_GET_ALL_LANGUAGES;
        Api.getInstance().languages().getAll(listener);
    }

    @Override
    public void onNativeLangClick(View view, List<Language> supportedLanguages, int position) {
        Toast.makeText(SelectReflectionActivity.this, "onNativeLangClick", Toast.LENGTH_SHORT).show();

        this.supportedLanguages = supportedLanguages;
        nativeLang = supportedLanguages.get(position);

        foreignLanguages = new ArrayList<>(supportedLanguages);
        foreignLanguages.remove(position);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, SelectForeignLangFragment.getInstance(foreignLanguages))
                .commit();
    }

    @Override
    public void onForeignLangClick(View view, int position) {
        Toast.makeText(SelectReflectionActivity.this, "onForeignLangClick", Toast.LENGTH_SHORT).show();
        foreignLang = foreignLanguages.get(position);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, loadingFragment)
                .commit();

        currentRequest = Request.API_GET_REFLECTION_FOR_LANGUAGES;
        Api.getInstance().reflections().getByLanguages(nativeLang, foreignLang, getByLangListener);
    }

    @Override
    public void onRefresh() {
        loadingFragment.startLoading();
        refresh();
    }

    private void refresh() {
        switch (currentRequest) {
            case API_GET_ALL_LANGUAGES:
                Api.getInstance().languages().getAll(listener);
                break;
            case API_GET_REFLECTION_FOR_LANGUAGES:
                Api.getInstance().reflections().getByLanguages(nativeLang, foreignLang, getByLangListener);
                break;
        }
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

            currentRequest = Request.NONE;
        }

        @Override
        public void onError(ANError anError) {
            loadingFragment.showRefresh();
            Toast.makeText(SelectReflectionActivity.this, "can't load", Toast.LENGTH_SHORT).show();
        }
    };

    private final ParsedRequestListener<Reflection> getByLangListener = new ParsedRequestListener<Reflection>() {
        @Override
        public void onResponse(Reflection response) {

            reflection = response;

            loadingFragment.stopLoading();

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = prefs.edit();

            editor.putString(getString(R.string.reflectionId), reflection.getId().toString());
            editor.putBoolean(getString(R.string.isInitRef), true);
            editor.apply();

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SelectReflectionActivity.this, CategoryActivity.class);
                    intent.putExtra(CategoryActivity.EXTRA_REFLECTION, reflection.getId().toString());
                    startActivity(intent);
                }
            }, 300);

            currentRequest = Request.NONE;
        }

        @Override
        public void onError(ANError anError) {
            loadingFragment.showRefresh();
            Toast.makeText(SelectReflectionActivity.this, "can't load", Toast.LENGTH_SHORT).show();
        }
    };
}
