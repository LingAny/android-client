package ru.tp.lingany.lingany.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.tp.lingany.lingany.R;

public class MarksForTranslationFragment extends Fragment {
    final String MARKS_FOR_TRANSLATIONS_CREATED = "marksForTranslationsCreated";

    private ViewGroup markCrossContainer;
    private TextView wordToTranslate;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_marks_for_translations, null, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        try {
            markCrossContainer = getView().findViewById(R.id.containerMarkAndCross);
            wordToTranslate = getView().findViewById(R.id.wordToTranslate);

            Intent intent = new Intent(MARKS_FOR_TRANSLATIONS_CREATED);
            intent.setAction(MARKS_FOR_TRANSLATIONS_CREATED);
            getActivity().sendBroadcast(intent);
        } catch(NullPointerException ex) {

        }
    }

    public ViewGroup getMarkCrossContainer() {
        return markCrossContainer;
    }

    public TextView getWordToTranslate() {
        return wordToTranslate;
    }
}
