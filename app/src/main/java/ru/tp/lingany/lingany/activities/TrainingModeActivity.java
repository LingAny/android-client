package ru.tp.lingany.lingany.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import ru.tp.lingany.lingany.R;

public class TrainingModeActivity extends AppCompatActivity {

    public static final String EXTRA_REFLECTION_ID = "EXTRA_REFLECTION_ID";

    public static final String EXTRA_TRAINING_MODE = "EXTRA_TRAINING_MODE";

    @Override
    @SuppressWarnings("unchecked")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_mode);
    }
}
