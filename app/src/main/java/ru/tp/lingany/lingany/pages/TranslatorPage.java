package ru.tp.lingany.lingany.pages;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import ru.tp.lingany.lingany.R;
import ru.tp.lingany.lingany.fragments.TypingFragment;
import ru.tp.lingany.lingany.fragments.fragmentData.TranslatorData;
import ru.tp.lingany.lingany.fragments.fragmentData.TypingData;


public class TranslatorPage extends Fragment {
    private TextView firstLanguage;
    private TextView secondLanguage;
    private Button changeLanguage;
    private EditText wordToTranslate;
    private TextView wordTranslation;
    private Button translateButton;
    private TranslatorData translatorData;

    private static final String TRANSLATOR_DATA = "TRANSLATOR_DATA";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {;
        return inflater.inflate(R.layout.page_translator, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        readBundle(getArguments());

        firstLanguage = (TextView) Objects.requireNonNull(getView()).findViewById(R.id.firstLanguage);
        secondLanguage = (TextView) Objects.requireNonNull(getView()).findViewById(R.id.secondLanguage);
        changeLanguage = (Button) Objects.requireNonNull(getView()).findViewById(R.id.changeLanguage);
        wordToTranslate = (EditText) getView().findViewById(R.id.typeWordToTranslateTranslator);
        wordTranslation = (TextView) Objects.requireNonNull(getView()).findViewById(R.id.wordTranslationTranslator);
        translateButton = (Button) Objects.requireNonNull(getView()).findViewById(R.id.translateButton);

        changeLanguage.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        processChangeLanguage(view);
                    }
                }
        );

        translateButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        processTranslate(view);
                    }
                }
        );
    }

    private void processChangeLanguage(View view) {

    }

    private void processTranslate(View view) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        boolean isInitRef = prefs.getBoolean(getString(R.string.isInitRef), false);
        if (!isInitRef) {
            return;
        }
        String reflection_id = prefs.getString(getString(R.string.reflectionId), "");
        String nativeLanguage = prefs.getString(getString(R.string.nativeLang), "");
        String foreignLanguage = prefs.getString(getString(R.string.foreignLang), "");

        translatorData.setForeignLanguage(foreignLanguage);
        translatorData.setNativeLanguage(nativeLanguage);
        translatorData.setReflectionId(reflection_id);
    }

    @Override
    public void onStart() {
        super.onStart();

        if (translatorData.isFilled()) {
            setTrainingAfterSaveInstance(translatorData);
        }
    }

    private void setTrainingAfterSaveInstance(TranslatorData translatorData) {
        firstLanguage.setText(translatorData.getForeignLanguage());
        secondLanguage.setText(translatorData.getNativeLanguage());
        wordToTranslate.setText(translatorData.getWordToTranslate());
        wordTranslation.setText(translatorData.getWordTranslation());
    }

    private void disableButtons() {
        changeLanguage.setClickable(false);
        translateButton.setClickable(false);
    }

    private void enableButtons() {
        changeLanguage.setClickable(true);
        translateButton.setClickable(true);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        if (translatorData != null) {
            outState.putSerializable(TRANSLATOR_DATA, (Serializable) translatorData);
        }
        super.onSaveInstanceState(outState);
    }

    @SuppressWarnings("unchecked")
    private void readBundle(Bundle bundle) {
        translatorData = (TranslatorData) bundle.getSerializable(TRANSLATOR_DATA);
    }
}
