package ru.tp.lingany.lingany.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ru.tp.lingany.lingany.R;
import ru.tp.lingany.lingany.fragments.fragmentData.SprintData;
import ru.tp.lingany.lingany.sdk.api.trainings.Training;
import ru.tp.lingany.lingany.utils.RandArray;

public class SprintFragment extends Fragment {

    private LayoutInflater inflater;
    private ViewGroup marksContainer;
    private TextView wordToTranslate;
    private TextView wordTranslation;
    private SprintData sprintData;

    private TextView timerView;
    private CountDownTimer timer;

    List<TextView> buttons = new ArrayList<>();
    private static final String SPRINT_DATA = "SPRINT_DATA";

    public interface SprintListener {
        void onSprintFinished();
    }

    private SprintListener sprintListener;

    public static SprintFragment newInstance(SprintData sprintData) {
        Bundle bundle = new Bundle();
        if (sprintData != null) {
            bundle.putSerializable(SPRINT_DATA, sprintData);
        }

        SprintFragment fragment = new SprintFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @SuppressWarnings("unchecked")
    private void readBundle(Bundle bundle) {
        if (bundle != null) {
            sprintData = (SprintData) bundle.getSerializable(SPRINT_DATA);
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
        timerView = getView().findViewById(R.id.timerSprint);

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

        long maxTimerValue = 5000;
        long timerInterval = 1000;
        timer = new CountDownTimer(maxTimerValue, timerInterval) {

            public void onTick(long millisUntilFinished) {
                long timeEstimates = millisUntilFinished / 1000;
                sprintData.setCurrentTime(timeEstimates);
                timerView.setText(String.valueOf(timeEstimates));
            }

            public void onFinish() {
                setNewTraining(sprintData);
                timer.start();
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();

        if (sprintData.isFilled()) {
            setTrainingAfterSaveInstance(sprintData);
        } else {
            setNewTraining(sprintData);
        }
    }

    private void setTrainingAfterSaveInstance(SprintData sprintData) {
        sprintData.setMarkAndCrossLength(0);
        if (sprintData.isVictories()) {
            for (int i = 0; i < sprintData.getMarkAndCrossLength(); ++i) {
                final View view = inflater.inflate(R.layout.item_mark, marksContainer, false);
                marksContainer.addView(view);
            }
        } else {
            for (int i = 0; i < sprintData.getMarkAndCrossLength(); ++i) {
                final View view = inflater.inflate(R.layout.item_cross, this.marksContainer, false);
                marksContainer.addView(view);
            }
        }

        Training currentTraining = sprintData.getTrainings().get(sprintData.getCurrentTrainingNumber());
        setWordToTranslate(currentTraining.getForeignWord());
        setWordTranslation(sprintData.getVisibleTranslation());
        timer.start();
    }

    private void setNewTraining(SprintData sprintData) {
        sprintData.setCurrentTrainingNumber(sprintData.getCurrentTrainingNumber() + 1);
        if (sprintData.getCurrentTrainingNumber() >= sprintData.getTrainings().size() - 1) {
            finish();
            return;
        }

        Training training = sprintData.getTrainings().get(sprintData.getCurrentTrainingNumber());

        int index = RandArray.getRandIndex( 0, sprintData.getTrainings().size() - 1);
        if (index % 2 == 0) {
            sprintData.setVisibleTranslation(training.getNativeWord());
        } else {
            sprintData.setVisibleTranslation(sprintData.getTrainings().get(index).getNativeWord());
        }

        setWordToTranslate(training.getForeignWord());
        setWordTranslation(sprintData.getVisibleTranslation());
        timer.start();
    }

    public void setWordToTranslate(String word) {
        wordToTranslate.setText(word);
    }

    public void setWordTranslation(String word) {
        wordTranslation.setText(word);
    }

    public void addMark() {
        if (!sprintData.isVictories() || sprintData.getMarkAndCrossLength() > 2) {
            clearMarkAndCross();
        }
        sprintData.setVictories(true);
        sprintData.setMarkAndCrossLength(sprintData.getMarkAndCrossLength() + 1);
        final View view = inflater.inflate(R.layout.item_mark, marksContainer, false);
        marksContainer.addView(view);
    }

    public void addCross() {
        if (sprintData.isVictories() || sprintData.getMarkAndCrossLength() > 2) {
            clearMarkAndCross();
        }
        sprintData.setVictories(false);
        sprintData.setMarkAndCrossLength(sprintData.getMarkAndCrossLength() + 1);
        final View view = inflater.inflate(R.layout.item_cross, this.marksContainer, false);
        marksContainer.addView(view);
    }

    public void clearMarkAndCross() {
        sprintData.setMarkAndCrossLength(0);
        marksContainer.removeAllViews();
    }

    private void processAnswer(View view) {
        Training currentTraining = sprintData.getTrainings().get(sprintData.getCurrentTrainingNumber());
        TextView textView = (TextView) view;

        if (textView.getId() == R.id.agreeButtonSprint) {
            if (currentTraining.getNativeWord().equals(sprintData.getVisibleTranslation())) {
                addMark();
            } else {
                addCross();
            }
        } else {
            if (!currentTraining.getNativeWord().equals(sprintData.getVisibleTranslation())) {
                addMark();
            } else {
                addCross();
            }
        }

        disableButtons();
        timer.cancel();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 2000ms
                setNewTraining(sprintData);
                enableButtons();
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

    private void finish() {
        sprintListener.onSprintFinished();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        sprintListener = (SprintFragment.SprintListener) context;
    }

    public SprintData getSprintData() {
        return sprintData;
    }

    public void stopTimer() {
        timer.cancel();
    }
}
