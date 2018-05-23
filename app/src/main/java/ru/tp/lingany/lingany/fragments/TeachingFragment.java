package ru.tp.lingany.lingany.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.Serializable;
import java.util.Objects;

import ru.tp.lingany.lingany.R;
import ru.tp.lingany.lingany.fragments.fragmentData.TeachingData;

public class TeachingFragment extends Fragment {
    private TextView wordToTranslate;
    private TextView wordTranslation;
    private TeachingData teachingData;
    private TextView rememberButton;
    private TextView wordCounter;
    private static final String TEACHING_DATA = "TEACHING_DATA";

    private Integer currentTrainingNumber;
    private Integer maxTrainingNumber;

    public interface TeachingListener {
        void onTeachingFinished();
    }

    private TeachingListener teachingListener;

    public static TeachingFragment newInstance(TeachingData teachingData) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(TEACHING_DATA, (Serializable) teachingData);

        TeachingFragment fragment = new TeachingFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @SuppressWarnings("unchecked")
    private void readBundle(Bundle bundle) {
        if (bundle != null) {
            teachingData = (TeachingData) bundle.getSerializable(TEACHING_DATA);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {;
        return inflater.inflate(R.layout.fragment_teaching, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        readBundle(getArguments());

        wordToTranslate = (TextView) Objects.requireNonNull(getView()).findViewById(R.id.wordToTranslateTeaching);
        wordTranslation = (TextView) getView().findViewById(R.id.wordTranslationTeaching);
        rememberButton = (TextView) Objects.requireNonNull(getView()).findViewById(R.id.rememberButton);
        wordCounter = getView().findViewById(R.id.containerContextWordCounterStudyWord);
        rememberButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        processAnswer(view);
                    }
                }
        );
    }

    @Override
    public void onStart() {
        super.onStart();

        if (teachingData.isFilled()) {
            setTrainingAfterSaveInstance(teachingData);
        } else {
            setNewWords(teachingData);
        }
    }

    private void setTrainingAfterSaveInstance(TeachingData teachingData) {
        wordToTranslate.setText(teachingData.getTrainings().get(teachingData.getTrainingNumber()).getForeignWord());
        wordTranslation.setText(teachingData.getTrainings().get(teachingData.getTrainingNumber()).getNativeWord());
    }


    private void setNewWords(TeachingData teachingData) {
        currentTrainingNumber = teachingData.getTrainingNumber() + 1;
        maxTrainingNumber = teachingData.getTrainings().size();

        teachingData.setTrainingNumber(teachingData.getTrainingNumber() + 1);

        if (teachingData.getTrainingNumber() >= teachingData.getTrainings().size()) {
            finish();
            return;
        }

        wordToTranslate.setText(teachingData.getTrainings().get(teachingData.getTrainingNumber()).getForeignWord());
        wordTranslation.setText(teachingData.getTrainings().get(teachingData.getTrainingNumber()).getNativeWord());
        setWordCounter(currentTrainingNumber + 1, maxTrainingNumber);
    }

    public void setWordCounter(Integer currentTrainingNumber, Integer maxTrainingNumber) {
        String resultString = currentTrainingNumber.toString() + "/" + maxTrainingNumber.toString();
        wordCounter.setText(resultString);
    }


    private void processAnswer(View view) {
        TextView textView = (TextView) view;

        disableButtons();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setNewWords(teachingData);
                enableButtons();
            }
        }, getResources().getInteger(R.integer.delayNextTraining));
    }

    private void disableButtons() {
        rememberButton.setClickable(false);
    }

    private void enableButtons() {
        rememberButton.setClickable(true);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        teachingListener = (TeachingListener) context;
    }

    private void finish() {
        teachingListener.onTeachingFinished();
    }

    public TeachingData getTeachingData() {
        return teachingData;
    }
}