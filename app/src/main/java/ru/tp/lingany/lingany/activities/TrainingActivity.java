package ru.tp.lingany.lingany.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;

import java.util.List;

import ru.tp.lingany.lingany.R;
import ru.tp.lingany.lingany.fragments.FindTranslationFragment;
import ru.tp.lingany.lingany.fragments.SprintFragment;
import ru.tp.lingany.lingany.fragments.TrainingHeaderFragment;
import ru.tp.lingany.lingany.sdk.Api;
import ru.tp.lingany.lingany.sdk.categories.Category;
import ru.tp.lingany.lingany.sdk.trainings.Training;


public class TrainingActivity extends AppCompatActivity implements
        FindTranslationFragment.FindTranslationListener,
        SprintFragment.SprintListener {

    enum Mode { FIND_TRANSLATION, SPRINT }

    public static final String EXTRA_CATEGORY = "EXTRA_CATEGORY";
    private FragmentManager fragmentManager;
    private List<Training> trainings;


    private final ParsedRequestListener<List<Training>> getForCategoryListener = new ParsedRequestListener<List<Training>>() {
        @Override
        public void onResponse(List<Training> response) {
            trainings = response;
            changeMode(Mode.SPRINT);

            Log.i("FindTranslationActivity", "onResponse");
        }

        @Override
        public void onError(ANError anError) {
            Log.e("tag", anError.toString());
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_training);
        fragmentManager = getSupportFragmentManager();

        Intent intent = getIntent();
        Category category = (Category) intent.getSerializableExtra(EXTRA_CATEGORY);
        Api.getInstance().training().getForCategory(category, getForCategoryListener);
    }

    private void inizializeTranslationFragments() {
        String TRAINING_FIND_TRANSLATION_TITLE = "Find Translation";
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        TrainingHeaderFragment headerFragment = TrainingHeaderFragment.newInstance(TRAINING_FIND_TRANSLATION_TITLE);
        FindTranslationFragment translationButtonsFragment = FindTranslationFragment.newInstance(trainings);

        transaction.replace(R.id.trainingHeaderContainer, headerFragment);
        transaction.replace(R.id.trainingBodyContainer, translationButtonsFragment);

        transaction.commit();
    }

    private void inizializeSprintFragments() {
        String TRAINING_SPRINT_TITLE = "Sprint";
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        TrainingHeaderFragment headerFragment = TrainingHeaderFragment.newInstance(TRAINING_SPRINT_TITLE);
        SprintFragment sprintFragment = SprintFragment.newInstance(trainings);

        transaction.replace(R.id.trainingHeaderContainer, headerFragment);
        transaction.replace(R.id.trainingBodyContainer, sprintFragment);

        transaction.commit();
    }

    private void changeMode(Mode newMode) {
        if (newMode == Mode.FIND_TRANSLATION) {
            inizializeTranslationFragments();
        } else if (newMode == Mode.SPRINT) {
            inizializeSprintFragments();
        }
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
