package ru.tp.lingany.lingany.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;

import java.util.List;

import ru.tp.lingany.lingany.R;
import ru.tp.lingany.lingany.sdk.Api;
import ru.tp.lingany.lingany.sdk.categories.Category;
import ru.tp.lingany.lingany.sdk.trainings.Training;


public class TrainingActivity extends AppCompatActivity {

    public static final String EXTRA_CATEGORY = "EXTRA_CATEGORY";

    private ProgressBar progress;

    private Category category;
    private List<Training> trainings;
    private Training currentTraining;

    private LayoutInflater inflater;

    private ViewGroup leftBtnContainer;
    private ViewGroup rightBtnContainer;
    private ViewGroup markCrossContainer;

    private TextView wordToTranslate;

    private final ParsedRequestListener<List<Training>> getForCategoryListener = new ParsedRequestListener<List<Training>>() {
        @Override
        public void onResponse(List<Training> response) {
            trainings = response;
            progress.setVisibility(View.INVISIBLE);

            Log.i("TrainingActivity", "onResponse");
        }

        @Override
        public void onError(ANError anError) {
            Log.e("tag", anError.toString());
        }
    };

    private void setWords() {
        if (trainings.size() < 1) {
            updateTrainings();
        }
        Training training = this.trainings.get(0);
        this.currentTraining = training;
        clearMarkAndCross();
        this.wordToTranslate.setText(training.getForeignWord());
        inflateButtonsContainers(training);

    }

    private void inflateButtonsContainers(Training training) {
        leftBtnContainer.removeAllViews();
        rightBtnContainer.removeAllViews();

        int translationPosition = (int) (Math.random() * 3);
        for (int i = 0; i < 4; ++i) {
            ViewGroup currentContainer;

            if (i % 2 == 0) {
                currentContainer = leftBtnContainer;
            } else {
                currentContainer = rightBtnContainer
            }
            if (i == translationPosition) {
                this.inflateContainer(currentContainer, R.layout.item_answer_button, training.getNativeWord());
            } else {
                this.inflateContainer(currentContainer, R.layout.item_answer_button, training.getRandomWord());
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
        if (this.currentTraining != null && this.currentTraining == textView.getText()) {
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

        setContentView(R.layout.activity_training);

        Intent intent = getIntent();
        this.category = (Category) intent.getSerializableExtra(EXTRA_CATEGORY);

        TextView title = findViewById(R.id.trainingTitle);
        title.setText(category.getId().toString());

        progress = findViewById(R.id.progress);
        progress.setVisibility(View.VISIBLE);

        updateTrainings();

        this.inflater = LayoutInflater.from(this);
        this.leftBtnContainer = (ViewGroup) findViewById(R.id.leftButtonsContainer);
        this.rightBtnContainer = (ViewGroup) findViewById(R.id.rightButtonsContainer);
        this.markCrossContainer = (ViewGroup) findViewById(R.id.containerMarkAndCross);
        this.wordToTranslate = (TextView) findViewById(R.id.wordToTranslate);
    }

    private void updateTrainings() {
        Api.getInstance().training().getForCategory(this.category, getForCategoryListener);
    }
}
