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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ru.tp.lingany.lingany.R;
import ru.tp.lingany.lingany.sdk.api.trainings.Training;
import ru.tp.lingany.lingany.utils.RandArray;

public class SprintFragment extends Fragment {

    private LayoutInflater inflater;
    private ViewGroup marksContainer;
    private TextView wordToTranslate;
    private TextView wordTranslation;

    private String wordToTranslateText;
    private String wordTranslationText;
    private String realTranslationText;

    private List<Training> trainings;
    private boolean victories = false;
    private int markAndCrossLength = 0;

    List<TextView> buttons = new ArrayList<>();

    private static final String TRAININGS = "TRAININGS";

    public interface SprintListener {
        void onSprintFinished();
    }
    private SprintListener sprintListener;

    public static SprintFragment newInstance(List<Training> trainings) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(TRAININGS, (Serializable) trainings);

        SprintFragment fragment = new SprintFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @SuppressWarnings("unchecked")
    private void readBundle(Bundle bundle) {
        if (bundle != null) {
            List<Training> localTrainings = (List<Training>) bundle.getSerializable(TRAININGS);
            if (localTrainings != null) {
                trainings = new ArrayList<>(localTrainings);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.inflater = inflater;
        return inflater.inflate(R.layout.fragment_sprint, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        readBundle(getArguments());

        marksContainer = Objects.requireNonNull(getView()).findViewById(R.id.containerMarkAndCrossSprint);
        wordToTranslate = getView().findViewById(R.id.wordToTranslateSprint);
        wordTranslation = getView().findViewById(R.id.wordTranslationSprint);

        TextView agreeButton = Objects.requireNonNull(getView()).findViewById(R.id.agreeButtonSprint);
        TextView notAgreeButton = getView().findViewById(R.id.notAgreeButtonSprint);

        buttons.add(agreeButton);
        buttons.add(notAgreeButton);
        for (View button:buttons) {
            button.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            processAnswer(v);
                        }
                    });
        }
        setAll();
    }

    private void setAll() {
        if (trainings.size() < 1) {
            finish();
            return;
        }
        Training training = trainings.get(0);

        setWordToTranslate(training.getForeignWord());

        int index = RandArray.getRandIndex( 0, trainings.size() - 1);
        if (index % 2 == 0) {
            setWordTranslation(training.getNativeWord(), training.getNativeWord());
        } else {
            setWordTranslation(training.getNativeWord(), trainings.get(index).getNativeWord());
        }
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

    private void processAnswer(View view) {
        TextView textView = (TextView) view;
        if (textView.getId() == R.id.agreeButtonSprint) {
            if (getRealTranslationText().equals(getWordTranslationText())) {
                addMark();
            } else {
                addCross();
            }
        } else {
            if (getRealTranslationText().equals(getWordTranslationText())) {
                addCross();
            } else {
                addMark();
            }
        }

        disableButtons();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 2000ms
                if (trainings.size() > 0) {
                    trainings.remove(0);
                    setAll();
                    enableButtons();
                }
            }
        }, getResources().getInteger(R.integer.delayNextTraining));
    }

    private void disableButtons() {
        for (TextView button: buttons) {
            button.setClickable(false);
        }
    }

    private void enableButtons() {
        for (TextView button: buttons) {
            button.setClickable(true);
        }
    }

    public String getWordTranslationText() {
        return wordTranslationText;
    }

    public String getRealTranslationText() {
        return realTranslationText;
    }

    private void finish() {
        sprintListener.onSprintFinished();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        sprintListener = (SprintFragment.SprintListener) context;
    }
}
