package ru.tp.lingany.lingany.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;

import java.util.List;

import ru.tp.lingany.lingany.R;
import ru.tp.lingany.lingany.fragments.FindTranslationFragment;
import ru.tp.lingany.lingany.fragments.LoadingFragment;
import ru.tp.lingany.lingany.fragments.SprintFragment;
import ru.tp.lingany.lingany.fragments.TeachingFragment;
import ru.tp.lingany.lingany.fragments.TypingFragment;
import ru.tp.lingany.lingany.fragments.fragmentData.FragmentData;
import ru.tp.lingany.lingany.fragments.fragmentData.SprintData;
import ru.tp.lingany.lingany.fragments.fragmentData.TeachingData;
import ru.tp.lingany.lingany.fragments.fragmentData.TranslationData;
import ru.tp.lingany.lingany.fragments.fragmentData.TypingData;
import ru.tp.lingany.lingany.sdk.Api;
import ru.tp.lingany.lingany.sdk.api.categories.Category;
import ru.tp.lingany.lingany.sdk.api.trainings.Training;
import ru.tp.lingany.lingany.utils.ListenerHandler;


public class TrainingActivity extends AppCompatActivity implements
        FindTranslationFragment.FindTranslationListener,
        LoadingFragment.RefreshListener,
        SprintFragment.SprintListener,
        TeachingFragment.TeachingListener,
        TypingFragment.TypingListener {

    enum Mode { TEACHING, FIND_TRANSLATION, SPRINT, TYPING }
    private Mode mode;
    private SprintFragment sprintFragment;
    private FindTranslationFragment translationFragment;
    private TeachingFragment teachingFragment;
    private TypingFragment typingFragment;

    public static final String EXTRA_CATEGORY = "EXTRA_CATEGORY";
    public static final String TRAINING_MODE = "TRAINING_MODE";
    public static final String SPRINT_DATA = "SPRINT_DATA";
    public static final String TRAINING_DATA = "TRAINING_DATA";
    public static final String TEACHING_DATA = "TEACHING_DATA";
    public static final String TYPING_DATA = "TYPING_DATA";

    private List<Training> trainings;
    private LoadingFragment loadingFragment;
    private Category category;

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        if (mode != null) {
            switch (mode) {
                case TEACHING:
                    TeachingData teachingData = teachingFragment.getTeachingData();
                    savedInstanceState.putSerializable(TEACHING_DATA, teachingData);
                case FIND_TRANSLATION:
                    TranslationData translationData =  translationFragment.getTranslationData();
                    savedInstanceState.putSerializable(TRAINING_DATA, translationData);
                    break;
                case SPRINT:
                    SprintData sprintData = sprintFragment.getSprintData();
                    savedInstanceState.putSerializable(SPRINT_DATA, sprintData);
                    break;
                case TYPING:
                    TypingData typingData = typingFragment.getTypingData();
                    savedInstanceState.putSerializable(TYPING_DATA, typingData);
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
            changeMode(Mode.TEACHING, new TeachingData(trainings));
//            changeMode(Mode.TYPING, new TypingData(trainings));
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

            mode = (Mode) savedInstanceState.getSerializable(TRAINING_MODE);
            if (mode == Mode.TEACHING) {
                TeachingData teachingData = (TeachingData) savedInstanceState.getSerializable(TEACHING_DATA);
                changeMode(mode, teachingData);
                return;
            } else if (mode == Mode.SPRINT) {
                SprintData sprintData = (SprintData) savedInstanceState.getSerializable(SPRINT_DATA);
                changeMode(mode, sprintData);
                return;
            } else if (mode == Mode.FIND_TRANSLATION) {
                TranslationData translationData = (TranslationData) savedInstanceState.getSerializable(TRAINING_DATA);
                changeMode(mode, translationData);
                return;
            } else if (mode == Mode.TYPING) {
                TypingData typingData = (TypingData) savedInstanceState.getSerializable(TYPING_DATA);
                changeMode(mode, typingData);
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

    private void initializeTranslationFragments(TranslationData data) {
        translationFragment = FindTranslationFragment.newInstance(data);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.trainingContainer, translationFragment)
                .commit();
    }

    private void initializeSprintFragments(SprintData sprintData) {
        sprintFragment = SprintFragment.newInstance(sprintData);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.trainingContainer, sprintFragment)
                .commit();
    }

    private void initializeTeachingFragment(TeachingData teachingData) {
        teachingFragment = TeachingFragment.newInstance(teachingData);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.trainingContainer, teachingFragment)
                .commit();
    }

    private void initializeTypingFragment(TypingData typingData) {
        typingFragment = TypingFragment.newInstance(typingData);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.trainingContainer, typingFragment)
                .commit();
    }

    private void inflateLoadingFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.trainingContainer, loadingFragment)
                .commit();
    }

    private void changeMode(Mode newMode, FragmentData data) {
        mode = newMode;
        if (newMode == Mode.TEACHING) {
            initializeTeachingFragment((TeachingData) data);
        } else if (newMode == Mode.FIND_TRANSLATION) {
            initializeTranslationFragments((TranslationData) data);
        } else if (newMode == Mode.SPRINT) {
            initializeSprintFragments((SprintData) data);
        } else if (newMode == Mode.TYPING) {
            initializeTypingFragment((TypingData) data);
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
        changeMode(Mode.SPRINT, new SprintData(trainings));
    }

    @Override
    public void onSprintFinished() {
        changeMode(Mode.FIND_TRANSLATION, new TranslationData(trainings));
    }

    @Override
    public void onTeachingFinished() {
        changeMode(Mode.TYPING, new TypingData(trainings));
    }

    @Override
    public void onTypingFinished() {
        changeMode(Mode.FIND_TRANSLATION, new TranslationData(trainings));
    }

}
