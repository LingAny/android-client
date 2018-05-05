package ru.tp.lingany.lingany.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.UUID;

import ru.tp.lingany.lingany.R;
import ru.tp.lingany.lingany.fragments.LoadingFragment;
import ru.tp.lingany.lingany.models.TrainingMode;

public class TrainingModeActivity extends AppCompatActivity implements
        LoadingFragment.RefreshListener {

    private UUID refId;
    private TrainingMode mode;

    private LoadingFragment loadingFragment;


    public static final String EXTRA_REFLECTION_ID = "EXTRA_REFLECTION_ID";
    public static final String EXTRA_TRAINING_MODE = "EXTRA_TRAINING_MODE";

    @Override
    @SuppressWarnings("unchecked")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_mode);
        readIntent();

        loadingFragment = new LoadingFragment();
        inflateLoadingFragment();

        Toast.makeText(this, mode.getTitle(), Toast.LENGTH_SHORT).show();
    }

    private void inflateLoadingFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, loadingFragment)
                .commit();
    }

    private void readIntent() {
        Intent intent = getIntent();
        refId = (UUID) intent.getSerializableExtra(EXTRA_REFLECTION_ID);
        mode = (TrainingMode) intent.getSerializableExtra(EXTRA_TRAINING_MODE);
    }

    @Override
    public void onRefresh() {
        loadingFragment.startLoading();
    }
}
