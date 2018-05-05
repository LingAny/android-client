package ru.tp.lingany.lingany.pages;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import ru.tp.lingany.lingany.R;
import ru.tp.lingany.lingany.adapters.TrainingModeAdapter;
import ru.tp.lingany.lingany.models.TrainingMode;
import ru.tp.lingany.lingany.models.TrainingModeTypes;

public class TrainingsPage extends Fragment {

    private ArrayList<TrainingMode> modes;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
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
        trainingRecyclerView.setAdapter(new TrainingModeAdapter(modes, new TrainingsPage.ItemClickListener()));
    }

    private class ItemClickListener implements TrainingModeAdapter.ItemClickListener {

        @Override
        public void onClick(View view, int position) {
            TrainingMode mode = modes.get(position);
            Toast.makeText(getContext(), mode.getTitle(), Toast.LENGTH_SHORT).show();
        }
    }

    private void initTrainingModes() {
        modes = new ArrayList<>();

        TrainingMode sprintN2F = new TrainingMode(TrainingModeTypes.Type.SPRINT_N2F);
        TrainingMode sprintF2N = new TrainingMode(TrainingModeTypes.Type.SPRINT_F2N);
        TrainingMode translateN2F = new TrainingMode(TrainingModeTypes.Type.TRANSLATION_N2F);
        TrainingMode translateF2N = new TrainingMode(TrainingModeTypes.Type.TRANSLATION_F2N);

        modes.add(sprintN2F);
        modes.add(sprintF2N);
        modes.add(translateN2F);
        modes.add(translateF2N);
    }
}

