package ru.tp.lingany.lingany.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;

import java.util.ArrayList;
import java.util.List;

import ru.tp.lingany.lingany.R;
import ru.tp.lingany.lingany.fragments.FindTranslationButtonsFragment;
import ru.tp.lingany.lingany.fragments.MarksForTranslationFragment;
import ru.tp.lingany.lingany.fragments.SprintButtonsFragment;
import ru.tp.lingany.lingany.fragments.SprintMarksFragment;
import ru.tp.lingany.lingany.fragments.TrainingHeaderFragment;
import ru.tp.lingany.lingany.sdk.Api;
import ru.tp.lingany.lingany.sdk.categories.Category;
import ru.tp.lingany.lingany.sdk.trainings.Training;
import ru.tp.lingany.lingany.utils.RandArray;


public class TrainingActivity extends AppCompatActivity implements
        FindTranslationButtonsFragment.FindTranslationBtnListener,
        SprintButtonsFragment.SprintBtnListener {

    enum Mode { FIND_TRANSLATION, SPRINT }
    Mode mode;

    public static final String EXTRA_CATEGORY = "EXTRA_CATEGORY";
    FragmentManager fragmentManager;
    private Category category;
    private List<Training> trainings;
    private Training currentTraining;

    private MarksForTranslationFragment marksForTranslationFragment;
    private FindTranslationButtonsFragment translationButtonsFragment;

    private SprintButtonsFragment sprintButtonsFragment;
    private SprintMarksFragment sprintMarksFragment;


    private final ParsedRequestListener<List<Training>> getForCategoryListener = new ParsedRequestListener<List<Training>>() {
        @Override
        public void onResponse(List<Training> response) {
            trainings = response;
            setAll();

            Log.i("FindTranslationActivity", "onResponse");
        }

        @Override
        public void onError(ANError anError) {
            Log.e("tag", anError.toString());
        }
    };

    private void setAll() {
        if (mode.equals(Mode.FIND_TRANSLATION)) {
            setTranslationMode();
        } else if (mode.equals(Mode.SPRINT)) {
            setSprintMode();
        }
    }

    private void setSprintMode() {
        if (trainings.size() < 4) {
            changeMode(Mode.FIND_TRANSLATION);
        }
        Training training = trainings.get(0);
        currentTraining = training;

        sprintMarksFragment.setWordToTranslate(training.getForeignWord());

        int index = RandArray.getRandIndex( 0, trainings.size() - 1);
        if (index % 2 == 0) {
            sprintMarksFragment.setWordTranslation(training.getNativeWord(), training.getNativeWord());
        } else {
            sprintMarksFragment.setWordTranslation(training.getNativeWord(), trainings.get(index).getNativeWord());
        }
    }

    private void proccessAnswerSprint(View view) {
        TextView textView = (TextView) view;
        if (textView.getText() == "Yes") {
            if (sprintMarksFragment.getRealTranslationText().equals(sprintMarksFragment.getWordTranslationText())) {
                sprintMarksFragment.addMark();
            } else {
                sprintMarksFragment.addCross();
            }
        } else {
            if (sprintMarksFragment.getRealTranslationText().equals(sprintMarksFragment.getWordTranslationText())) {
                sprintMarksFragment.addCross();
            } else {
                sprintMarksFragment.addMark();

            }
        }

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 2000ms
                trainings.remove(0);
                setAll();
            }
        }, 2000);
    }

    private void setTranslationMode() {
        if (trainings.size() < 4) {
            changeMode(Mode.SPRINT);
        }
        Training training = trainings.get(0);
        currentTraining = training;

        marksForTranslationFragment.clearMarkAndCross();
        marksForTranslationFragment.setWordToTranslate(training.getForeignWord());
        setTranslationButtons(training);
    }

    private void setTranslationButtons(Training training) {
        List<Integer> indexes = RandArray.getRandIndexes(3, 0, trainings.size() - 1);
        List<String> words = new ArrayList<>();

        for (Integer index: indexes) {
            words.add(trainings.get(index).getNativeWord());
        }
        translationButtonsFragment.setWordsOnButtons(training.getNativeWord(), words);
    }

    private void proccessAnswerTranslation(View view) {
        TextView textView = (TextView) view;
        if (this.currentTraining != null && this.currentTraining.getNativeWord() == textView.getText()) {
            marksForTranslationFragment.setMark();
        } else {
            marksForTranslationFragment.setCross();
        }

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 2000ms
                trainings.remove(0);
                setAll();
            }
        }, 2000);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_training);
        fragmentManager = getSupportFragmentManager();


        Intent intent = getIntent();
        category = (Category) intent.getSerializableExtra(EXTRA_CATEGORY);

        changeMode(Mode.SPRINT);
    }

    private void updateTrainings() {
        Api.getInstance().training().getForCategory(this.category, getForCategoryListener);
    }

    private void inizializeTranslationFragments() {
        String TRAINING_FIND_TRANSLATION_TITLE = "Find Translation";
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        TrainingHeaderFragment headerFragment = TrainingHeaderFragment.newInstance(TRAINING_FIND_TRANSLATION_TITLE);
        transaction.replace(R.id.trainingHeaderContainer, headerFragment);

        marksForTranslationFragment = new MarksForTranslationFragment();
        transaction.replace(R.id.marksContainer, marksForTranslationFragment);

        translationButtonsFragment = new FindTranslationButtonsFragment();
        transaction.replace(R.id.buttonsContainer, translationButtonsFragment);

        transaction.commit();
    }

    private void inizializeSprintFragments() {
        String TRAINING_SPRINT_TITLE = "Sprint";
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        TrainingHeaderFragment headerFragment = TrainingHeaderFragment.newInstance(TRAINING_SPRINT_TITLE);
        transaction.replace(R.id.trainingHeaderContainer, headerFragment);

        sprintMarksFragment = new SprintMarksFragment();
        transaction.replace(R.id.marksContainer, sprintMarksFragment);

        sprintButtonsFragment = new SprintButtonsFragment();
        transaction.replace(R.id.buttonsContainer, sprintButtonsFragment);

        transaction.commit();
    }

    private void changeMode(Mode newMode) {
        mode = newMode;
        if (mode == Mode.FIND_TRANSLATION) {
            inizializeTranslationFragments();
        } else if (mode == Mode.SPRINT) {
            inizializeSprintFragments();
        }
        updateTrainings();
    }

    @Override
    public void onFindTranslationBtnClick(View view) {
        proccessAnswerTranslation(view);
    }

    @Override
    public void onSprintBtnClick(View view) {
        proccessAnswerSprint(view);
    }

}


