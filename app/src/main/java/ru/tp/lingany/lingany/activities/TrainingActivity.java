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
import ru.tp.lingany.lingany.fragments.TrainingHeaderFragment;
import ru.tp.lingany.lingany.sdk.Api;
import ru.tp.lingany.lingany.sdk.categories.Category;
import ru.tp.lingany.lingany.sdk.trainings.Training;
import ru.tp.lingany.lingany.utils.ListenerHandler;


public class TrainingActivity extends AppCompatActivity implements
        FindTranslationFragment.FindTranslationListener,
        LoadingFragment.RefreshListener,
        SprintFragment.SprintListener {

    enum Mode { FIND_TRANSLATION, SPRINT }
    private Mode mode;

    public static final String EXTRA_CATEGORY = "EXTRA_CATEGORY";
    public static final String TRAININGS = "TRAININGS";
    public static final String TRAINING_MODE = "TRAINING_MODE";

    private List<Training> trainings;
    private LoadingFragment loadingFragment;
    private Category category;

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        if (trainings != null) {
            savedInstanceState.putSerializable(TRAININGS, (Serializable) trainings);
        }
        if (mode != null) {
            savedInstanceState.putSerializable(TRAINING_MODE, mode);
        }
        super.onSaveInstanceState(savedInstanceState);
    }

    private ListenerHandler getForCategoryListenerHandler = ListenerHandler.wrap(ParsedRequestListener.class, new ParsedRequestListener<List<Training>>() {
        @Override
        public void onResponse(List<Training> response) {
            trainings = response;
            changeMode(Mode.FIND_TRANSLATION);
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
            if (mode == null) {
                mode = Mode.FIND_TRANSLATION;
            }
            if (trainings != null) {
                changeMode(mode);
                return;
            }
        }

        inflateLoadingFragment();
        getTrainingsForCategory();
    }

    @SuppressWarnings("unchecked")
    private void getTrainingsForCategory() {
        ParsedRequestListener<List<Training>> listener = (ParsedRequestListener<List<Training>>) getForCategoryListenerHandler.asListener();
        Api.getInstance().training().getForCategory(category, listener);
    }

    private void initializeTranslationFragments() {
        String TRAINING_FIND_TRANSLATION_TITLE = getString(R.string.headerTrainingFindTranslation);

        TrainingHeaderFragment headerFragment = TrainingHeaderFragment.newInstance(TRAINING_FIND_TRANSLATION_TITLE);
        FindTranslationFragment translationButtonsFragment = FindTranslationFragment.newInstance(trainings);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.trainingHeaderContainer, headerFragment)
                .replace(R.id.trainingBodyContainer, translationButtonsFragment)
                .commit();
    }

    private void initializeSprintFragments() {
        String TRAINING_SPRINT_TITLE = getString(R.string.headerTrainingSprint);

        TrainingHeaderFragment headerFragment = TrainingHeaderFragment.newInstance(TRAINING_SPRINT_TITLE);
        SprintFragment sprintFragment = SprintFragment.newInstance(trainings);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.trainingHeaderContainer, headerFragment)
                .replace(R.id.trainingBodyContainer, sprintFragment)
                .commit();
    }

    private void inflateLoadingFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.trainingHeaderContainer, loadingFragment)
                .commit();
    }

    private void changeMode(Mode newMode) {
        mode = newMode;
        if (newMode == Mode.FIND_TRANSLATION) {
            initializeTranslationFragments();
        } else if (newMode == Mode.SPRINT) {
            initializeSprintFragments();
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
        changeMode(Mode.SPRINT);
    }

    @Override
    public void onSprintFinished() {
        changeMode(Mode.FIND_TRANSLATION);
    }
}
