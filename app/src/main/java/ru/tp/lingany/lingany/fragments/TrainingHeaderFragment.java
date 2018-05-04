package ru.tp.lingany.lingany.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.Objects;

import ru.tp.lingany.lingany.R;

public class TrainingHeaderFragment extends Fragment {
    private String title;

    public static TrainingHeaderFragment newInstance(String title) {
        Bundle bundle = new Bundle();
        bundle.putString("title", title);

        TrainingHeaderFragment fragment = new TrainingHeaderFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    private void readBundle(Bundle bundle) {
        if (bundle != null) {
            title = bundle.getString("title");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_training_header, null, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        readBundle(getArguments());
        TextView trainingTitle = Objects.requireNonNull(getView()).findViewById(R.id.trainingTitle);
        trainingTitle.setText(title);
    }
}
