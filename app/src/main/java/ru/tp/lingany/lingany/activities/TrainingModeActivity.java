package ru.tp.lingany.lingany.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;

import java.util.List;
import java.util.UUID;

import ru.tp.lingany.lingany.R;
import ru.tp.lingany.lingany.fragments.LoadingFragment;
import ru.tp.lingany.lingany.models.TrainingMode;
import ru.tp.lingany.lingany.models.TrainingModeTypes;
import ru.tp.lingany.lingany.sdk.Api;
import ru.tp.lingany.lingany.sdk.api.trainings.Training;
import ru.tp.lingany.lingany.utils.ListenerHandler;

public class TrainingModeActivity extends AppCompatActivity implements
        LoadingFragment.RefreshListener {

    private UUID refId;
    private TrainingMode mode;

    private List<Training> mix;

    private LoadingFragment loadingFragment;

    private ListenerHandler getMixForRefListenerHandler;

    public static final String EXTRA_REFLECTION_ID = "EXTRA_REFLECTION_ID";
    public static final String EXTRA_TRAINING_MODE = "EXTRA_TRAINING_MODE";

    @Override
    @SuppressWarnings("unchecked")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_mode);
        readIntent();

        loadingFragment = new LoadingFragment();
        getMixForRefListenerHandler = ListenerHandler.wrap(ParsedRequestListener.class, new ParsedRequestListener<List<Training>>() {
            @Override
            public void onResponse(List<Training> response) {
                mix = response;
                loadingFragment.stopLoading();
                inflateTrainingMode(getResources().getInteger(R.integer.delayInflateAfterLoading));
            }

            @Override
            public void onError(ANError anError) {
                loadingFragment.showRefresh();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mix == null) {
            inflateLoadingFragment();
            getMixForReflection();
        } else {
            inflateTrainingMode();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getMixForRefListenerHandler != null) {
            getMixForRefListenerHandler.unregister();
        }
    }

    private void inflateLoadingFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, loadingFragment)
                .commit();
    }

    private void inflateTrainingMode() {
        Fragment fragment = null;
        TrainingModeTypes.Type type = mode.getType();

        if (type == TrainingModeTypes.Type.SPRINT_F2N) {
            // TODO init fragmetn
        } else if (type == TrainingModeTypes.Type.SPRINT_N2F) {
            // TODO init fragmetn
        } else if (type == TrainingModeTypes.Type.TRANSLATION_F2N) {
            // TODO init fragmetn
        } else if (type == TrainingModeTypes.Type.TRANSLATION_N2F) {
            // TODO init fragmetn
        }

//        getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.container, fragment)
//                .commit();
    }

    private void inflateTrainingMode(int delayMillis) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                inflateTrainingMode();
            }
        }, delayMillis);
    }

    private void readIntent() {
        Intent intent = getIntent();
        refId = (UUID) intent.getSerializableExtra(EXTRA_REFLECTION_ID);
        mode = (TrainingMode) intent.getSerializableExtra(EXTRA_TRAINING_MODE);
    }

    @Override
    public void onRefresh() {
        loadingFragment.startLoading();
        getMixForReflection();
    }

    @SuppressWarnings("unchecked")
    private void getMixForReflection() {
        ParsedRequestListener<List<Training>> listener = (ParsedRequestListener<List<Training>>) getMixForRefListenerHandler.asListener();
        Api.getInstance().training().getMixForReflection(refId, listener);
    }
}
