package ru.tp.lingany.lingany.activities;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;

import java.util.ArrayList;
import java.util.List;

import ru.tp.lingany.lingany.R;
import ru.tp.lingany.lingany.fragments.LoadingFragment;
import ru.tp.lingany.lingany.fragments.SelectLangFragment;
import ru.tp.lingany.lingany.sdk.Api;
import ru.tp.lingany.lingany.sdk.languages.Language;
import ru.tp.lingany.lingany.sdk.reflections.Reflection;


public class SelectReflectionActivity extends AppCompatActivity implements
        SelectLangFragment.LangClickListener,
        LoadingFragment.RefreshListener {

    private Language nativeLang;

    private Language foreignLang;

    private Reflection reflection;

    private List<Language> supportedLanguages;
    private List<Language> foreignLanguages;

    private LoadingFragment loadingFragment;

    private enum Requests { API_GET_ALL_LANGUAGES, API_GET_REFLECTION_FOR_LANGUAGES, NONE }
    private enum LangTypes { NATIVE, FOREIGN, NONE }

    private Requests request = Requests.NONE;
    private LangTypes langType = LangTypes.NONE;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_reflection);

        loadingFragment = new LoadingFragment();
        inflateLoadingFragment();

        request = Requests.API_GET_ALL_LANGUAGES;
        Api.getInstance().languages().getAll(getAllLangListener);
    }

    @Override
    public void onClickLanguage(int position) {
        switch (langType) {
            case NATIVE:
                selectNativeLang(position);
                break;
            case FOREIGN:
                selectForeignLang(position);
                break;
        }
    }

    @Override
    public void onRefresh() {
        loadingFragment.startLoading();
        switch (request) {
            case API_GET_ALL_LANGUAGES:
                Api.getInstance().languages().getAll(getAllLangListener);
                break;
            case API_GET_REFLECTION_FOR_LANGUAGES:
                Api.getInstance().reflections().getByLanguages(nativeLang, foreignLang, getByLangListener);
                break;
        }
    }

    private final ParsedRequestListener<List<Language>> getAllLangListener = new ParsedRequestListener<List<Language>>() {
        @Override
        public void onResponse(List<Language> response) {
            supportedLanguages = response;
            loadingFragment.stopLoading();
            inflateNativeLangFragment(getResources().getInteger(R.integer.delayInflateAfterLoading));
            request = Requests.NONE;
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

            saveReflection();
            startCategoryActivity();
            request = Requests.NONE;
        }

        @Override
        public void onError(ANError anError) {
            loadingFragment.showRefresh();
            Toast.makeText(SelectReflectionActivity.this, "can't load", Toast.LENGTH_SHORT).show();
        }
    };

    private void selectNativeLang(int position) {
        nativeLang = supportedLanguages.get(position);
        foreignLanguages = new ArrayList<>(supportedLanguages);
        foreignLanguages.remove(position);
        inflateForeignLangFragment();
    }

    private void selectForeignLang(int position) {
        foreignLang = foreignLanguages.get(position);
        inflateLoadingFragment();
        request = Requests.API_GET_REFLECTION_FOR_LANGUAGES;
        Api.getInstance().reflections().getByLanguages(nativeLang, foreignLang, getByLangListener);
    }

    private void inflateNativeLangFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, SelectLangFragment.getInstance(getString(R.string.chooseNativeLang), supportedLanguages))
                .commit();
        langType = LangTypes.NATIVE;
    }

    private void inflateForeignLangFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, SelectLangFragment.getInstance(getString(R.string.chooseForeignLang), foreignLanguages))
                .commit();
        langType = LangTypes.FOREIGN;
    }

    private void inflateLoadingFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, loadingFragment)
                .commit();
    }

    private void inflateNativeLangFragment(int delayMillis) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                inflateNativeLangFragment();
            }
        }, delayMillis);
    }

    private void saveReflection() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString(getString(R.string.reflectionId), reflection.getId().toString());
        editor.putBoolean(getString(R.string.isInitRef), true);
        editor.apply();
    }

    private void startCategoryActivity() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SelectReflectionActivity.this, CategoryActivity.class);
                intent.putExtra(CategoryActivity.EXTRA_REFLECTION, reflection.getId().toString());
                startActivity(intent);
            }
        }, getResources().getInteger(R.integer.delayInflateAfterLoading));
    }
}
