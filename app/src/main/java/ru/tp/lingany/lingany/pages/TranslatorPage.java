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

import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import ru.tp.lingany.lingany.R;
import ru.tp.lingany.lingany.activities.TrainingActivity;
import ru.tp.lingany.lingany.fragments.fragmentData.SprintData;
import ru.tp.lingany.lingany.fragments.fragmentData.TranslatorData;
import ru.tp.lingany.lingany.sdk.Api;
import ru.tp.lingany.lingany.sdk.api.trainings.Training;
import ru.tp.lingany.lingany.sdk.api.word.Word;
import ru.tp.lingany.lingany.utils.ListenerHandler;


public class TranslatorPage extends Fragment {
    private TextView firstLanguage;
    private TextView secondLanguage;
    private Button changeLanguage;
    private EditText wordToTranslate;
    private TextView wordTranslation;
    private Button translateButton;
    private TranslatorData translatorData;
    private ListenerHandler wordListener;


    private static final String TRANSLATOR_DATA = "TRANSLATOR_DATA";
    private static final String REFLECTION_ID_TRANSLATOR = "REFLECTION_ID_TRANSLATOR";

    public static TranslatorPage getInstance(UUID refId) {
        Bundle bundle = new Bundle();

        bundle.putSerializable(REFLECTION_ID_TRANSLATOR, refId);

        TranslatorPage fragment = new TranslatorPage();
        fragment.setArguments(bundle);
        return fragment;
    }

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

        firstLanguage.setText(translatorData.getForeignLanguage());
        secondLanguage.setText(translatorData.getNativeLanguage());

        wordListener = ListenerHandler.wrap(ParsedRequestListener.class, new ParsedRequestListener<Word>() {
            @Override
            public void onResponse(Word response) {
                System.out.println("Good");
                translatorData.setForeignLanguage(response.getTranslation());
                wordTranslation.setText(response.getTranslation());
//                loadingFragment.stopLoading();
                enableButtons();
            }

            @Override
            public void onError(ANError anError) {
//                loadingFragment.showRefresh();
                System.out.println("erorr");
                enableButtons();
            }
        });
    }

    private void processChangeLanguage(View view) {
        translatorData.changeLanguage();
        firstLanguage.setText(translatorData.getForeignLanguage());
        secondLanguage.setText(translatorData.getNativeLanguage());
    }

    @SuppressWarnings("unchecked")
    private void processTranslate(View view) {
//        disableButtons();
        translatorData.setWordToTranslate(wordToTranslate.getText().toString());
        ParsedRequestListener<Word> listener = (ParsedRequestListener<Word>) wordListener.asListener();
        Api.getInstance().word().getTranslationByText(translatorData.getReflectionId().toString(),
                translatorData.getWordToTranslate(), listener);
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
        UUID ref_uuid = UUID.fromString(reflection_id);
        String nativeLanguage = prefs.getString(getString(R.string.nativeLang), "");
        String foreignLanguage = prefs.getString(getString(R.string.foreignLang), "");

        translatorData = new TranslatorData(ref_uuid);
        translatorData.setForeignLanguage(foreignLanguage);
        translatorData.setNativeLanguage(nativeLanguage);
    }

    @Override
    public void onStart() {
        super.onStart();

        if (translatorData.isFilled()) {
            setTrainingAfterSaveInstance(translatorData);
        }
    }

    private void setTrainingAfterSaveInstance(TranslatorData translatorData) {
        if (translatorData.isLanguageChanged()) {
            firstLanguage.setText(translatorData.getNativeLanguage());
            secondLanguage.setText(translatorData.getForeignLanguage());
        } else {
            firstLanguage.setText(translatorData.getForeignLanguage());
            secondLanguage.setText(translatorData.getNativeLanguage());
        }

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
//        String reflection_id = bundle.getString(REFLECTION_ID_TRANSLATOR);
//        UUID ref_uuid = UUID.fromString(reflection_id);
//        String nativeLanguage = bundle.getString(NATIVE_LANGUAGE_TRANSLATOR);
//        String foreignLanguage = bundle.getString(FOREIGN_LANGUAGE_TRANSLATOR);
//
//        translatorData = new TranslatorData(ref_uuid);
//        translatorData.setForeignLanguage(foreignLanguage);
//        translatorData.setNativeLanguage(nativeLanguage);
    }
}
