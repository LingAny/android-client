package ru.tp.lingany.lingany.pages;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

import ru.tp.lingany.lingany.R;
import ru.tp.lingany.lingany.activities.TrainingModeActivity;
import ru.tp.lingany.lingany.adapters.TrainingModeAdapter;
import ru.tp.lingany.lingany.models.TrainingMode;
import ru.tp.lingany.lingany.models.TrainingModeTypes;

public class TrainingsPage extends Fragment {

    private ArrayList<TrainingMode> modes;

    private UUID refId;

    private TrainingModeClickListener trainingModeClickListener;

    private static final String REFLECTION_ID = "REFLECTION_ID";

    public static TrainingsPage getInstance(UUID refId) {
        Bundle bundle = new Bundle();

        bundle.putSerializable(REFLECTION_ID, refId);

        TrainingsPage fragment = new TrainingsPage();
        fragment.setArguments(bundle);
        return fragment;
    }

    public interface TrainingModeClickListener {
        void onClickTrainingMode(int position);
    }

    public void selectTraining(int position) {
        TrainingMode mode = modes.get(position);

        Intent intent = new Intent(getContext(), TrainingModeActivity.class);
        intent.putExtra(TrainingModeActivity.EXTRA_REFLECTION_ID, refId);
        intent.putExtra(TrainingModeActivity.EXTRA_TRAINING_MODE, mode);
        startActivity(intent);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        trainingModeClickListener = (TrainingsPage.TrainingModeClickListener) context;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        readBundle(Objects.requireNonNull(getArguments()));
        initTrainingModes();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.page_trainings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RecyclerView trainingRecyclerView = view.findViewById(R.id.trainings);
        RecyclerView.LayoutManager trainingLayoutManager = new LinearLayoutManager(getContext());
        trainingRecyclerView.setLayoutManager(trainingLayoutManager);
        trainingRecyclerView.setAdapter(new TrainingModeAdapter(modes, new TrainingsPage.ItemClickListener(), getContext()));
    }

    private class ItemClickListener implements TrainingModeAdapter.ItemClickListener {

        @Override
        public void onClick(View view, int position) {
            trainingModeClickListener.onClickTrainingMode(position);
        }
    }

    private void initTrainingModes() {
        modes = new ArrayList<>();

        TrainingMode sprintMode = new TrainingMode(TrainingModeTypes.Type.SPRINT);
        TrainingMode translationMode = new TrainingMode(TrainingModeTypes.Type.TRANSLATION);
        TrainingMode typeMode = new TrainingMode(TrainingModeTypes.Type.TYPING_MODE);
        TrainingMode studyMode = new TrainingMode(TrainingModeTypes.Type.STUDY_MODE);

        modes.add(sprintMode);
        modes.add(typeMode);
        modes.add(translationMode);
        modes.add(studyMode);
    }

    @SuppressWarnings("unchecked")
    private void readBundle(Bundle bundle) {
        refId = (UUID) bundle.getSerializable(REFLECTION_ID);
    }
}
