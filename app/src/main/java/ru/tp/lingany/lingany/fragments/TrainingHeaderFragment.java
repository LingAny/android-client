package ru.tp.lingany.lingany.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Objects;

import ru.tp.lingany.lingany.R;

public class TrainingHeaderFragment extends Fragment {
    final String TRAINING_HEADER_CREATED = "trainingHeaderCreated";

    private TextView trainingTitle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_training_header, null, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        try {
            trainingTitle = getView().findViewById(R.id.trainingTitle);

            Intent intent = new Intent(TRAINING_HEADER_CREATED);
            intent.setAction(TRAINING_HEADER_CREATED);
            getActivity().sendBroadcast(intent);
        } catch(NullPointerException ex) {

        }
    }

    public TextView getTrainingTitle() {
        return trainingTitle;
    }
}
