package ru.tp.lingany.lingany.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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

    private List<Training> training;

    private final ParsedRequestListener<List<Training>> getForCategoryListener = new ParsedRequestListener<List<Training>>() {
        @Override
        public void onResponse(List<Training> response) {
            training = response;
            progress.setVisibility(View.INVISIBLE);
            Log.i("tag", "onResponse");
        }

        @Override
        public void onError(ANError anError) {
            Log.e("tag", anError.toString());
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_reflection);

        Intent intent = getIntent();
        Category category = (Category) intent.getSerializableExtra(EXTRA_CATEGORY);

        TextView title = findViewById(R.id.title);
        title.setText(category.getId().toString());

        progress = findViewById(R.id.progress);
        progress.setVisibility(View.VISIBLE);

        Api.getInstance().training().getForCategory(category, getForCategoryListener);
    }
}
