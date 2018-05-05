package ru.tp.lingany.lingany.activities;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ru.tp.lingany.lingany.R;
import ru.tp.lingany.lingany.fragments.LoadingFragment;
import ru.tp.lingany.lingany.fragments.SelectLangFragment;
import ru.tp.lingany.lingany.sdk.Api;
import ru.tp.lingany.lingany.sdk.api.languages.Language;
import ru.tp.lingany.lingany.sdk.api.reflections.Reflection;
import ru.tp.lingany.lingany.utils.ListenerHandler;


public class SelectReflectionActivity extends AppCompatActivity implements
        LoadingFragment.RefreshListener,
        SelectLangFragment.LangClickListener {

    private Language nativeLang;

    private Language foreignLang;

    private Reflection reflection;

    private List<Language> foreignLanguages;

    private List<Language> supportedLanguages;

    private LoadingFragment loadingFragment;

    private enum LangTypes {NATIVE, FOREIGN, NONE}

    private enum Requests {API_GET_ALL_LANGUAGES, API_GET_REFLECTION_BY_LANGUAGES, NONE}

    private Requests request = Requests.NONE;

    private LangTypes langType = LangTypes.NONE;

    private static final String SUPPORTED_LANGUAGES = "SUPPORTED_LANGUAGES";

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        if (supportedLanguages != null) {
            savedInstanceState.putSerializable(SUPPORTED_LANGUAGES, (Serializable) supportedLanguages);
        }
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_reflection);

        loadingFragment = new LoadingFragment();

        if (savedInstanceState != null) {
            supportedLanguages = (List<Language>) savedInstanceState.getSerializable(SUPPORTED_LANGUAGES);
            if (supportedLanguages != null) {
                inflateNativeLangFragment(getResources().getInteger(R.integer.delayInflateAfterLoading));
                return;
            }
        }

        inflateLoadingFragment();
        getAllLanguages();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (getAllLangListenerHandler != null) {
            getAllLangListenerHandler.unregister();
        }
        if (getReflectionByLangListener != null) {
            getReflectionByLangListener.unregister();
        }
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
                getAllLanguages();
                break;
            case API_GET_REFLECTION_BY_LANGUAGES:
                getReflectionByLanguages();
                break;
        }
    }

    private ListenerHandler getAllLangListenerHandler = ListenerHandler.wrap(ParsedRequestListener.class, new ParsedRequestListener<List<Language>>() {
        @Override
        public void onResponse(List<Language> response) {
            supportedLanguages = response;
            loadingFragment.stopLoading();
            inflateNativeLangFragment(getResources().getInteger(R.integer.delayInflateAfterLoading));
            dropRequestType();
        }

        @Override
        public void onError(ANError anError) {
            loadingFragment.showRefresh();
        }
    });

    private ListenerHandler getReflectionByLangListener = ListenerHandler.wrap(ParsedRequestListener.class, new ParsedRequestListener<Reflection>() {
        @Override
        public void onResponse(Reflection response) {
            reflection = response;
            loadingFragment.stopLoading();

            saveReflection();
            startCategoryActivity();
            dropRequestType();
            dropLangType();
        }

        @Override
        public void onError(ANError anError) {
            loadingFragment.showRefresh();
        }
    });

    private void selectNativeLang(int position) {
        nativeLang = supportedLanguages.get(position);
        foreignLanguages = new ArrayList<>(supportedLanguages);
        foreignLanguages.remove(position);
        inflateForeignLangFragment();
    }

    private void selectForeignLang(int position) {
        foreignLang = foreignLanguages.get(position);
        inflateLoadingFragment();
        getReflectionByLanguages();
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
                Intent intent = new Intent(SelectReflectionActivity.this, MenuActivity.class);
                intent.putExtra(MenuActivity.EXTRA_REFLECTION, reflection.getId().toString());
                startActivity(intent);
            }
        }, getResources().getInteger(R.integer.delayInflateAfterLoading));
    }

    @SuppressWarnings("unchecked")
    private void getAllLanguages() {
        request = Requests.API_GET_ALL_LANGUAGES;
        ParsedRequestListener<List<Language>> listener = (ParsedRequestListener<List<Language>>) getAllLangListenerHandler.asListener();
        Api.getInstance().languages().getAll(listener);
    }

    @SuppressWarnings("unchecked")
    private void getReflectionByLanguages() {
        request = Requests.API_GET_REFLECTION_BY_LANGUAGES;
        ParsedRequestListener<Reflection> listener = (ParsedRequestListener<Reflection>) getReflectionByLangListener.asListener();
        Api.getInstance().reflections().getByLanguages(nativeLang, foreignLang, listener);
    }

    private void dropRequestType() {
        request = Requests.NONE;
    }

    private void dropLangType() {
        langType = LangTypes.NONE;
    }
}
