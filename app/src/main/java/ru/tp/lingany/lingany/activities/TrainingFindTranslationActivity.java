package ru.tp.lingany.lingany.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ru.tp.lingany.lingany.R;
import ru.tp.lingany.lingany.fragments.FindTranslationButtonsFragment;
import ru.tp.lingany.lingany.fragments.MarksForTranslationFragment;
import ru.tp.lingany.lingany.sdk.Api;
import ru.tp.lingany.lingany.sdk.categories.Category;
import ru.tp.lingany.lingany.sdk.trainings.Training;


public class TrainingFindTranslationActivity extends AppCompatActivity {

    public static final String EXTRA_CATEGORY = "EXTRA_CATEGORY";
    final String TRANSLATION_BUTTONS_CREATED = "translationButtonsCreated";
    final String MARKS_FOR_TRANSLATIONS_CREATED = "marksForTranslationsCreated";

    private ProgressBar progress;

    private Category category;
    private List<Training> trainings;
    private Training currentTraining;

    private LayoutInflater inflater;

    private MarksForTranslationFragment marksForTranslationFragment;
    private ViewGroup markCrossContainer;
    private TextView wordToTranslate;
    private FindTranslationButtonsFragment translationButtonsFragment;
    private List<View> translationButtons;

    private final ParsedRequestListener<List<Training>> getForCategoryListener = new ParsedRequestListener<List<Training>>() {
        @Override
        public void onResponse(List<Training> response) {
            trainings = response;
            progress.setVisibility(View.INVISIBLE);
            setWords();

            Log.i("FindTranslationActivity", "onResponse");
        }

        @Override
        public void onError(ANError anError) {
            Log.e("tag", anError.toString());
        }
    };

    private void setWords() {
//        <4 to have time
        if (trainings.size() < 4) {
            updateTrainings();
        }

        Training training = trainings.get(0);
        currentTraining = training;
        clearMarkAndCross();
        wordToTranslate.setText(training.getForeignWord());
        setFindTranslationButtons(training);
    }

    private void setFindTranslationButtons(Training training) {
        int translationPosition = (int) (Math.random() * 3);

        RandArray randArr = new RandArray(trainings.size());
        List<Integer> indexes = randArr.getRandIdx();

        for (int i = 0; i < translationButtons.size(); ++i) {
            TextView textView = (TextView) translationButtons.get(i);

            if (i == translationPosition) {
                textView.setText(training.getNativeWord());
            } else {
                Integer idx = indexes.get(i);
                textView.setText(trainings.get(idx).getNativeWord());
            }
        }
    }

    private void proccessAnswer(View view) {
        TextView textView = (TextView) view;
        if (this.currentTraining != null && this.currentTraining.getNativeWord() == textView.getText()) {
            System.out.println("YES");
            this.inflateMarkOrCross(R.layout.item_mark);
        } else {
            System.out.println("NO");
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
        this.markCrossContainer.removeAllViews();
        final View view = inflater.inflate(layout, this.markCrossContainer, false);
        this.markCrossContainer.addView(view);
    }

    private void clearMarkAndCross() {
        this.markCrossContainer.removeAllViews();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerReceiver(translationButtonsReciever, new IntentFilter(TRANSLATION_BUTTONS_CREATED));
        registerReceiver(marksForTranslationReciever, new IntentFilter(MARKS_FOR_TRANSLATIONS_CREATED));

        setContentView(R.layout.activity_training_find_translation);

        Intent intent = getIntent();
        category = (Category) intent.getSerializableExtra(EXTRA_CATEGORY);
        inflater = LayoutInflater.from(this);

        markCrossContainer = (ViewGroup) findViewById(R.id.containerMarkAndCross);
        wordToTranslate = (TextView) findViewById(R.id.wordToTranslate);

        progress = findViewById(R.id.progress);
        progress.setVisibility(View.VISIBLE);

        inizializeTranslationButtons();
        inizializeMarksForTranslation();
        updateTrainings();
        setListenersOnTranslationButtons();
    }

    private void updateTrainings() {
        Api.getInstance().training().getForCategory(this.category, getForCategoryListener);
    }

    private void inizializeTranslationButtons() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        translationButtonsFragment = new FindTranslationButtonsFragment();
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

    private void setListenersOnTranslationButtons() {
        translationButtons = translationButtonsFragment.getButtons();
        for (View button:translationButtons) {
            button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        proccessAnswer(v);

                    }
                });
        }
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

    private final BroadcastReceiver translationButtonsReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            if (intent != null && TRANSLATION_BUTTONS_CREATED.equals(intent.getAction())) {
                setListenersOnTranslationButtons();
            }
        }
    };

    private final BroadcastReceiver marksForTranslationReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            if (intent != null && MARKS_FOR_TRANSLATIONS_CREATED.equals(intent.getAction())) {
                markCrossContainer = marksForTranslationFragment.getMarkCrossContainer();
                wordToTranslate = marksForTranslationFragment.getWordToTranslate();
            }
        }
    };

}


