package ru.tp.lingany.lingany.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Objects;

import ru.tp.lingany.lingany.R;

public class SprintMarksFragment extends Fragment {
    private ViewGroup marksContainer;
    private TextView wordToTranslate;
    private TextView wordTranslation;
    private LayoutInflater inflater;

    private String wordToTranslateText;
    private String wordTranslationText;
    private String realTranslationText;

    private boolean victories = false;
    private int markAndCrossLength = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.inflater = inflater;
        return inflater.inflate(R.layout.fragment_marks_sprint, null, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        marksContainer = Objects.requireNonNull(getView()).findViewById(R.id.containerMarkAndCrossSprint);
        wordToTranslate = getView().findViewById(R.id.wordToTranslateSprint);
        wordTranslation = getView().findViewById(R.id.wordTranslationSprint);
    }

    public void setWordToTranslate(String word) {
        wordToTranslateText = word;
        wordToTranslate.setText(word);
    }

    public void setWordTranslation(String realTranslation, String word) {
        realTranslationText = realTranslation;
        wordTranslationText = word;
        wordTranslation.setText(word);
    }

    public void addMark() {
        if (!victories || markAndCrossLength > 3) {
            clearMarkAndCross();
        }
        victories = true;
        markAndCrossLength += 1;
        final View view = inflater.inflate(R.layout.item_mark, marksContainer, false);
        marksContainer.addView(view);
    }

    public void addCross() {
        if (victories || markAndCrossLength > 3) {
            clearMarkAndCross();
        }
        victories = false;
        markAndCrossLength += 1;
        final View view = inflater.inflate(R.layout.item_cross, this.marksContainer, false);
        marksContainer.addView(view);
    }

    public void clearMarkAndCross() {
        markAndCrossLength = 0;
        marksContainer.removeAllViews();
    }

    public String getWordTranslationText() {
        return wordTranslationText;
    }

    public String getRealTranslationText() {
        return realTranslationText;
    }

}
