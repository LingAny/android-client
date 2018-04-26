package ru.tp.lingany.lingany.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ru.tp.lingany.lingany.R;

public class SprintButtonsFragment extends Fragment {
    TextView agreeButton;
    TextView notAgreeButton;

    public interface SprintBtnListener {
        void onSprintBtnClick(View view);
    }

    private SprintBtnListener sprintBtnListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sprint_buttons, null, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        agreeButton = (TextView) Objects.requireNonNull(getView()).findViewById(R.id.agreeButton);
        notAgreeButton = (TextView) getView().findViewById(R.id.notAgreeButton);

        List<TextView> buttons = new ArrayList<>();
        buttons.add(agreeButton);
        buttons.add(notAgreeButton);
        for (View button:buttons) {
            button.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sprintBtnListener.onSprintBtnClick(v);
                        }
                    });
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        sprintBtnListener = (SprintBtnListener) context;
    }
}
