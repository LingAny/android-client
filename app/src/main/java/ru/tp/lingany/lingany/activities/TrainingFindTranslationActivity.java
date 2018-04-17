package ru.tp.lingany.lingany.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ru.tp.lingany.lingany.R;
import ru.tp.lingany.lingany.fragments.FindTranslationButtonsFragment;
import ru.tp.lingany.lingany.sdk.Api;
import ru.tp.lingany.lingany.sdk.categories.Category;
import ru.tp.lingany.lingany.sdk.trainings.Training;


public class TrainingFindTranslationActivity extends AppCompatActivity {

    public static final String EXTRA_CATEGORY = "EXTRA_CATEGORY";

    private ProgressBar progress;

    private Category category;
    private List<Training> trainings;
    private Training currentTraining;

    private LayoutInflater inflater;

    private ViewGroup buttonsContainer;
    private ViewGroup markCrossContainer;

    private TextView wordToTranslate;

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
//        <3 to have time
        if (trainings.size() < 3) {
            updateTrainings();
        }
        Training training = trainings.get(0);
        currentTraining = training;
        clearMarkAndCross();
        wordToTranslate.setText(training.getForeignWord());
//        inflateButtonsContainers(training);
    }

    private void setButtonsText(Training training) {
        ViewGroup fragmentFindTranslationButtons = findViewById(R.id.fragmentFindTranslationButtons);
        fragmentFindTranslationButtons.removeAllViews();

        int translationPosition = (int) (Math.random() * 3);

        RandArray randArr = new RandArray(trainings.size());
        List<Integer> indexes = randArr.getRandIdx();

        for (int i = 0; i < 4; ++i) {
            ViewGroup currentContainer;

            if (i % 2 == 0) {
                currentContainer = leftBtnContainer;
            } else {
                currentContainer = rightBtnContainer;
            }
            if (i == translationPosition) {
                inflateContainer(currentContainer, R.layout.item_answer_button, training.getNativeWord());
            } else {
                Integer idx = indexes.get(i);
                inflateContainer(currentContainer, R.layout.item_answer_button, trainings.get(idx).getNativeWord());
            }
        }
    }

    private void inflateContainer(ViewGroup container, int button, String buttonText) {
        final View view = inflater.inflate(button, container, false);
        TextView textView = (TextView) view;
        textView.setText(buttonText);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                proccessAnswer(view);
            }
        });
        container.addView(textView);
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

        setContentView(R.layout.activity_training_find_translation);

        Intent intent = getIntent();
        category = (Category) intent.getSerializableExtra(EXTRA_CATEGORY);
        inflater = LayoutInflater.from(this);

        markCrossContainer = (ViewGroup) findViewById(R.id.containerMarkAndCross);
        wordToTranslate = (TextView) findViewById(R.id.wordToTranslate);
        buttonsContainer = (ViewGroup) findViewById(R.id.buttonsContainer);

        progress = findViewById(R.id.progress);
        progress.setVisibility(View.VISIBLE);

        addTranlationButtons();
        updateTrainings();
    }

    private void updateTrainings() {
        Api.getInstance().training().getForCategory(this.category, getForCategoryListener);
    }

    private void addTranlationButtons() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(R.id.buttonsContainer, new FindTranslationButtonsFragment());
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
}
