package ru.tp.lingany.lingany.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.UUID;

import ru.tp.lingany.lingany.R;
import ru.tp.lingany.lingany.models.TrainingMode;

public class TrainingModeActivity extends AppCompatActivity {

    private UUID refId;
    private TrainingMode mode;

    public static final String EXTRA_REFLECTION_ID = "EXTRA_REFLECTION_ID";
    public static final String EXTRA_TRAINING_MODE = "EXTRA_TRAINING_MODE";

    @Override
    @SuppressWarnings("unchecked")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_mode);
        readIntent();

        TextView tv = findViewById(R.id.textView);
        tv.setText(mode.getTitle());
    }

    private void readIntent() {
        Intent intent = getIntent();
        refId = (UUID) intent.getSerializableExtra(EXTRA_REFLECTION_ID);
        mode = (TrainingMode) intent.getSerializableExtra(EXTRA_TRAINING_MODE);
    }
}
