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


public class TrainingActivity extends AppCompatActivity {

    public static final String EXTRA_CATEGORY = "EXTRA_CATEGORY";
    private final String MARKS_FOR_TRANSLATIONS_CREATED = "marksForTranslationsCreated";
    private final String TRAINING_HEADER_CREATED = "trainingHeaderCreated";
    private final String TRAINING_TITLE_FIND_TRANSLATIONS = "Find Translation";

    private final String MODE_FIND_TRANSLATION = "modeFindTranslation";
    private final String MODE_SPRINT = "modeSprint";
    private String currentMode;

    private Category category;
    private List<Training> trainings;
    private Training currentTraining;

    private LayoutInflater inflater;

    private MarksForTranslationFragment marksForTranslationFragment;
    private ViewGroup markCrossContainer;
    private TextView wordToTranslate;
    private FindTranslationButtonsFragment translationButtonsFragment;
    private TrainingHeaderFragment trainingHeaderFragment;
    private TextView trainingTitle;



    private final ParsedRequestListener<List<Training>> getForCategoryListener = new ParsedRequestListener<List<Training>>() {
        @Override
        public void onResponse(List<Training> response) {
            trainings = response;
//            setWords();

            Log.i("FindTranslationActivity", "onResponse");
        }

        @Override
        public void onError(ANError anError) {
            Log.e("tag", anError.toString());
        }
    };

    private void setWords() {

        Training training = trainings.get(0);
        currentTraining = training;

        if (currentMode.equals(MODE_FIND_TRANSLATION)) {
            clearMarkAndCross();
            wordToTranslate.setText(training.getForeignWord());
            setFindTranslationButtons(training);
        } else if (currentMode.equals(MODE_SPRINT)) {

        }
    }

    private void setFindTranslationButtons(Training training) {
        int translationPosition = (int) (Math.random() * 3);

        RandArray randArr = new RandArray(trainings.size());
        List<Integer> indexes = randArr.getRandIdx();

        for (int i = 0; i < 4; ++i) {
//            TextView textView = (TextView) translationButtons.get(i);
            TextView textView = null;
            if (i == translationPosition) {
                textView.setText(training.getNativeWord());
            } else {
                Integer idx = indexes.get(i);
                textView.setText(trainings.get(idx).getNativeWord());
            }
        }
    }

    private List<String> getNewWords() {
//        Training training = trainings.get(0);
//        currentTraining = training;
        List<String> words = new ArrayList<>();
        words.add("fsuhbfeshi");
        words.add("fe");
        words.add("fswaf");
        words.add("seaf");
//        words.add(training.getNativeWord());
//        words.add(training.getForeignWord());
//        words.add(training.getForeignWord());
//        words.add(training.getForeignWord());
        return words;
    }

    private void proccessAnswerTranslationButtons(View view) {
        TextView textView = (TextView) view;
        if (this.currentTraining != null && this.currentTraining.getNativeWord() == textView.getText()) {
            this.inflateMarkOrCross(R.layout.item_mark);
        } else {
            this.inflateMarkOrCross(R.layout.item_cross);
        }
//        do smth after delay
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

    private void inflateMarkOrCross(int layout) {
        clearMarkAndCross();
        final View view = inflater.inflate(layout, this.markCrossContainer, false);
        this.markCrossContainer.addView(view);
    }

    private void clearMarkAndCross() {
        this.markCrossContainer.removeAllViews();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerReceiver(marksForTranslationReciever, new IntentFilter(MARKS_FOR_TRANSLATIONS_CREATED));
        registerReceiver(trainingHeaderReciever, new IntentFilter(TRAINING_HEADER_CREATED));

        setContentView(R.layout.activity_training);

        Intent intent = getIntent();
        category = (Category) intent.getSerializableExtra(EXTRA_CATEGORY);
        inflater = LayoutInflater.from(this);
        currentMode = MODE_FIND_TRANSLATION;

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
        translationButtonsFragment = FindTranslationButtonsFragment.newInstance(new ButtonClickCallback(), getNewWords());
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

    public class RandArray {

        private int size;

        private Random rand = new Random();

        public RandArray(int size){
            this.size = size;
        }

        public List<Integer> getRandIdx() {
            if (size < 4) {
                return null;
            }
//            HashSet<Integer> arr = new HashSet<>();
            List<Integer> arr = new ArrayList<>();
            while (arr.size() < 4) {
                Integer idx = rand.nextInt(size);
                if (!arr.contains(idx)) {
                    arr.add(idx);
                }
            }

            return arr;
        }
    }

    private final BroadcastReceiver marksForTranslationReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            if (intent != null && MARKS_FOR_TRANSLATIONS_CREATED.equals(intent.getAction())) {
                markCrossContainer = marksForTranslationFragment.getMarkCrossContainer();
                wordToTranslate = marksForTranslationFragment.getWordToTranslate();
            }
        }
    };

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


