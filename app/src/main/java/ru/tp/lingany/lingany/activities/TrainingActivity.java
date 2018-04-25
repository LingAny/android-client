package ru.tp.lingany.lingany.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ru.tp.lingany.lingany.R;
import ru.tp.lingany.lingany.fragments.FindTranslationButtonsFragment;
import ru.tp.lingany.lingany.fragments.MarksForTranslationFragment;
import ru.tp.lingany.lingany.fragments.TrainingHeaderFragment;
import ru.tp.lingany.lingany.sdk.Api;
import ru.tp.lingany.lingany.sdk.categories.Category;
import ru.tp.lingany.lingany.sdk.trainings.Training;
import ru.tp.lingany.lingany.utils.RandArray;


public class TrainingActivity extends AppCompatActivity {

    public static final String EXTRA_CATEGORY = "EXTRA_CATEGORY";
    private final String TRAINING_HEADER_CREATED = "trainingHeaderCreated";
    private final String TRAINING_TITLE_FIND_TRANSLATIONS = "Find Translation";

    private Category category;
    private List<Training> trainings;
    private Training currentTraining;

    private MarksForTranslationFragment marksForTranslationFragment;
    private FindTranslationButtonsFragment translationButtonsFragment;
    private TrainingHeaderFragment trainingHeaderFragment;
    private TextView trainingTitle;


    private final ParsedRequestListener<List<Training>> getForCategoryListener = new ParsedRequestListener<List<Training>>() {
        @Override
        public void onResponse(List<Training> response) {
            trainings = response;
            setWords();

            Log.i("FindTranslationActivity", "onResponse");
        }

        @Override
        public void onError(ANError anError) {
            Log.e("tag", anError.toString());
        }
    };

    private void setWords() {
        if (trainings.size() < 4) {
//            to next game
        }
        Training training = trainings.get(0);
        currentTraining = training;

        marksForTranslationFragment.clearMarkAndCross();
        marksForTranslationFragment.setWordToTranslate(training.getForeignWord());
        setTranslationButtons(training);
    }

private void setTranslationButtons(Training training) {
    List<Integer> indexes = RandArray.getRandIdx(3, 0, trainings.size() - 1);
    List<String> words = new ArrayList<>();

    for (Integer index: indexes) {
        words.add(trainings.get(index).getNativeWord());
    }
    translationButtonsFragment.setWordsOnButtons(training.getNativeWord(), words);
}

    private void proccessAnswerTranslationButtons(View view) {
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
                setWords();
            }
        }, 2000);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerReceiver(trainingHeaderReciever, new IntentFilter(TRAINING_HEADER_CREATED));

        setContentView(R.layout.activity_training);

        Intent intent = getIntent();
        category = (Category) intent.getSerializableExtra(EXTRA_CATEGORY);

        inizializeTranslationFragments();
        updateTrainings();
    }

    private void updateTrainings() {
        Api.getInstance().training().getForCategory(this.category, getForCategoryListener);
    }

    private void inizializeTranslationFragments() {
        inizializeTrainingHeader();
        inizializeMarksForTranslation();
        inizializeTranslationButtons();
    }

    private void inizializeTranslationButtons() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        translationButtonsFragment = FindTranslationButtonsFragment.newInstance(new ButtonClickCallback());
        transaction.replace(R.id.buttonsContainer, translationButtonsFragment);
        transaction.commit();
    }

    private void inizializeMarksForTranslation() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        marksForTranslationFragment = new MarksForTranslationFragment();
        transaction.replace(R.id.marksContainer, marksForTranslationFragment);
        transaction.commit();
    }

    private void inizializeTrainingHeader() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        trainingHeaderFragment = new TrainingHeaderFragment();
        transaction.replace(R.id.trainingHeaderContainer, trainingHeaderFragment);
        transaction.commit();
    }

    private final BroadcastReceiver trainingHeaderReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            if (intent != null && TRAINING_HEADER_CREATED.equals(intent.getAction())) {
                trainingTitle = trainingHeaderFragment.getTrainingTitle();
                trainingTitle.setText(TRAINING_TITLE_FIND_TRANSLATIONS);
            }
        }
    };

    public class ButtonClickCallback implements FindTranslationButtonsFragment.OnClickCallback, Serializable {
        @Override
        public void onClick(View view) {
            proccessAnswerTranslationButtons(view);
        }
    }

}


