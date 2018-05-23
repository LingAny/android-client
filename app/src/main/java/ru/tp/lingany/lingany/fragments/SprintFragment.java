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

import static android.view.View.INVISIBLE;

public class SprintFragment extends Fragment {

    private LayoutInflater inflater;
    private ViewGroup marksContainer;
    private TextView wordToTranslate;
    private TextView wordTranslation;
    private TextView wordCounter;
    private SprintData sprintData;

    private TextView timerView;
    private CountDownTimer timer;

    List<TextView> buttons = new ArrayList<>();
    private static final String SPRINT_DATA = "SPRINT_DATA";

    private Integer currentTrainingNumber;
    private Integer maxTrainingNumber;
    private Integer traningScore = 0;

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
        wordCounter = getView().findViewById(R.id.containerFooterContextSprintCounter);
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
        currentTrainingNumber = sprintData.getCurrentTrainingNumber() + 1;
        maxTrainingNumber = sprintData.getTrainings().size();

        sprintData.setCurrentTrainingNumber(sprintData.getCurrentTrainingNumber() + 1);

        if (currentTrainingNumber.equals(maxTrainingNumber)) {
            setScore();
            return;
        }

        Training training = sprintData.getTrainings().get(currentTrainingNumber);

        int index = RandArray.getRandIndex( 0, maxTrainingNumber - 1);
        if (index % 2 == 0) {
            sprintData.setVisibleTranslation(training.getNativeWord());
        } else {
            sprintData.setVisibleTranslation(sprintData.getTrainings().get(index).getNativeWord());
        }

        setWordToTranslate(training.getForeignWord());
        setWordTranslation(sprintData.getVisibleTranslation());
        wordCounter.setText(makeFractionString(currentTrainingNumber + 1, maxTrainingNumber));
        timer.start();
    }

    private void setScore() {
        Integer maxTrainingNumber = sprintData.getTrainings().size();
        sprintData.setCurrentTrainingNumber(sprintData.getCurrentTrainingNumber() + 1);

        for (View button:buttons) {
            button.setVisibility(INVISIBLE);
        }

        wordToTranslate.setText(getResources().getString(R.string.scoreString));
        wordTranslation.setText(makeFractionString(traningScore, maxTrainingNumber));
        wordCounter.setText(null);
        processAnswer(getView());
    }

    private String makeFractionString(Integer currentTrainingNumber, Integer maxTrainingNumber) {
        return currentTrainingNumber.toString() + "/" + maxTrainingNumber.toString();
    }

    public void setWordCounter(Integer currentTrainingNumber, Integer maxTrainingNumber) {
        String resultString = currentTrainingNumber.toString() + "/" + maxTrainingNumber.toString();
        wordCounter.setText(resultString);
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

    private void showTraningWords(Handler handler, Integer timeout) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setNewTraining(sprintData);
                enableButtons();
            }
        }, timeout);
    }

    private void finishTraningDelay(Handler handler, Integer timeout) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, timeout);
    }

    private void processAnswer(View view) {
        disableButtons();
        timer.cancel();
        final Handler handler = new Handler();

        if (sprintData.getCurrentTrainingNumber() + 1 <= sprintData.getTrainings().size()) {
            Training currentTraining = sprintData.getTrainings().get(sprintData.getCurrentTrainingNumber());
            TextView textView = (TextView) view;

            if (textView.getId() == R.id.agreeButtonSprint) {
                if (currentTraining.getNativeWord().equals(sprintData.getVisibleTranslation())) {
                    addMark();
                    traningScore++;
                } else {
                    addCross();
                }
            } else {
                if (!currentTraining.getNativeWord().equals(sprintData.getVisibleTranslation())) {
                    addMark();
                    traningScore++;
                } else {
                    addCross();
                }
            }

            showTraningWords(handler, getResources().getInteger(R.integer.delayNextTraining));
        } else {
            finishTraningDelay(handler, getResources().getInteger(R.integer.delayFinishTraining));
        }
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
