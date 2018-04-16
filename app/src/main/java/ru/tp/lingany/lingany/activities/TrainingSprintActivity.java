package ru.tp.lingany.lingany.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;

import java.util.List;

import ru.tp.lingany.lingany.R;
import ru.tp.lingany.lingany.sdk.Api;
import ru.tp.lingany.lingany.sdk.categories.Category;
import ru.tp.lingany.lingany.sdk.trainings.Training;

public class TrainingSprintActivity extends AppCompatActivity {

    public static final String EXTRA_CATEGORY = "EXTRA_CATEGORY";

    private ProgressBar progress;
    private ProgressBar timeEstimates;

    private Category category;
    private List<Training> trainings;
    private Training currentTraining;

    private LayoutInflater inflater;
    private ViewGroup containerForMarks;
    private TextView wordToTranslate;
    private TextView wordTranslation;

    private Button agreeButton;
    private Button notAgreeButton;

    private int amountOfMarks = 0;

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
        Training training = this.trainings.get(0);
        wordToTranslate.setText(training.getForeignWord());
        wordTranslation.setText(training.getNativeWord());
    }

    private void proccessAnswer(View view) {
        TextView textView = (TextView) view;
        if (true) {
            System.out.println("YES");
            inflateMarkOrCross(R.layout.item_mark);
        } else {
            System.out.println("NO");
            inflateMarkOrCross(R.layout.item_cross);
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
        amountOfMarks += 1;
        final View view = inflater.inflate(layout, this.containerForMarks, false);
        this.containerForMarks.addView(view);
    }

    private void clearMarkAndCross() {
        this.containerForMarks.removeAllViews();
        amountOfMarks = 0;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_training_sprint);

        inflater = LayoutInflater.from(this);
        containerForMarks = (ViewGroup) findViewById(R.id.containerForMarks);
        wordToTranslate = (TextView) findViewById(R.id.wordToTranslate);
        wordTranslation = (TextView) findViewById(R.id.wordTranslation);

        progress = findViewById(R.id.progress);
        progress.setVisibility(View.VISIBLE);
        timeEstimates = findViewById(R.id.timeEstimates);

        Intent intent = getIntent();
        category = (Category) intent.getSerializableExtra(EXTRA_CATEGORY);

        updateTrainings();
        setUpButtons();
    }

    private void setUpButtons() {
        agreeButton = (Button) findViewById(R.id.agreeButton);
        notAgreeButton = (Button) findViewById(R.id.notAgreeButton);
        agreeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                proccessAnswer(view);
            }
        });
        notAgreeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                proccessAnswer(view);
            }
        });
    }

    private void updateTrainings() {
        Api.getInstance().training().getForCategory(category, getForCategoryListener);
    }
}
