package ru.tp.lingany.lingany.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;

import java.io.Serializable;
import java.util.List;

import ru.tp.lingany.lingany.R;
import ru.tp.lingany.lingany.fragments.FindTranslationFragment;
import ru.tp.lingany.lingany.fragments.LoadingFragment;
import ru.tp.lingany.lingany.fragments.SprintFragment;
import ru.tp.lingany.lingany.fragments.fragmentData.SprintData;
import ru.tp.lingany.lingany.sdk.Api;
import ru.tp.lingany.lingany.sdk.api.categories.Category;
import ru.tp.lingany.lingany.sdk.api.trainings.Training;
import ru.tp.lingany.lingany.utils.ListenerHandler;


public class TrainingActivity extends AppCompatActivity implements
        FindTranslationFragment.FindTranslationListener,
        LoadingFragment.RefreshListener,
        SprintFragment.SprintListener {

    enum Mode { FIND_TRANSLATION, SPRINT }
    private Mode mode;
    SprintFragment sprintFragment;
    FindTranslationFragment translationFragment;

    public static final String EXTRA_CATEGORY = "EXTRA_CATEGORY";
    public static final String TRAININGS = "TRAININGS";
    public static final String TRAINING_MODE = "TRAINING_MODE";
    public static final String TRAINING_NUMBER = "TRAINING_NUMBER";
    public static final String SPRINT_DATA = "SPRINT_DATA";

    private List<Training> trainings;
    private LoadingFragment loadingFragment;
    private Category category;

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        if (trainings != null) {
            savedInstanceState.putSerializable(TRAININGS, (Serializable) trainings);
        }
        int trainingNumber;
        if (mode != null) {
            switch (mode) {
                case FIND_TRANSLATION:
                    trainingNumber = translationFragment.getCurrentTrainingNumber();
                    savedInstanceState.putSerializable(TRAINING_NUMBER, trainingNumber);
                    break;
                case SPRINT:
                    SprintData sprintData = sprintFragment.getSprintData();
                    savedInstanceState.putSerializable(SPRINT_DATA, sprintData);
                    break;
                default:
                    break;
            }
            savedInstanceState.putSerializable(TRAINING_MODE, mode);
        }

        super.onSaveInstanceState(savedInstanceState);
    }

    private ListenerHandler getForCategoryListenerHandler = ListenerHandler.wrap(ParsedRequestListener.class, new ParsedRequestListener<List<Training>>() {
        @Override
        public void onResponse(List<Training> response) {
            trainings = response;
            changeMode(Mode.SPRINT, 0);
            loadingFragment.stopLoading();
        }

        @Override
        public void onError(ANError anError) {
            loadingFragment.showRefresh();
        }
    });

    @Override
    @SuppressWarnings("unchecked")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);
        loadingFragment = new LoadingFragment();
        Intent intent = getIntent();
        category = (Category) intent.getSerializableExtra(EXTRA_CATEGORY);

        if (savedInstanceState != null) {
            trainings = (List<Training>) savedInstanceState.getSerializable(TRAININGS);
            mode = (Mode) savedInstanceState.getSerializable(TRAINING_MODE);
            int currentTraining = (int) savedInstanceState.getSerializable(TRAINING_NUMBER);

            if (mode == null) {
                mode = Mode.FIND_TRANSLATION;
            }
            if (trainings != null) {
                changeMode(mode, currentTraining);
            }
            return;
        }

        inflateLoadingFragment();
        getTrainingsForCategory();
    }

    @SuppressWarnings("unchecked")
    private void getTrainingsForCategory() {
        ParsedRequestListener<List<Training>> listener = (ParsedRequestListener<List<Training>>) getForCategoryListenerHandler.asListener();
        Api.getInstance().training().getForCategory(category, listener);
    }

    private void initializeTranslationFragments(int currentTraining) {
        translationFragment = FindTranslationFragment.newInstance(trainings, currentTraining);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.trainingContainer, translationFragment)
                .commit();
    }

    private void initializeSprintFragments(int currentTraining) {
        sprintFragment = SprintFragment.newInstance(new SprintData(trainings));

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.trainingContainer, sprintFragment)
                .commit();
    }

    private void inflateLoadingFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.trainingContainer, loadingFragment)
                .commit();
    }

    private void changeMode(Mode newMode, int currentTraining) {
        mode = newMode;
        if (newMode == Mode.FIND_TRANSLATION) {
            initializeTranslationFragments(currentTraining);
        } else if (newMode == Mode.SPRINT) {
            initializeSprintFragments(currentTraining);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (getForCategoryListenerHandler != null) {
            getForCategoryListenerHandler.unregister();
        }
    }

    @Override
    public void onRefresh() {
        loadingFragment.startLoading();
        getTrainingsForCategory();
    }

    @Override
    public void onFindTranslationFinished() {
        changeMode(Mode.SPRINT, 0);
    }

    @Override
    public void onSprintFinished() {
        changeMode(Mode.FIND_TRANSLATION, 0);
    }
}
