package ru.tp.lingany.lingany.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ru.tp.lingany.lingany.R;
import ru.tp.lingany.lingany.activities.TrainingActivity;

public class FindTranslationButtonsFragment extends Fragment {
    private List<View> buttons = new ArrayList<>();

    public interface OnClickCallback {
        void onClick(View view);
    }

    private OnClickCallback callback;

    public static FindTranslationButtonsFragment newInstance(OnClickCallback listener, List<String> text) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("listener", (Serializable) listener);

        FindTranslationButtonsFragment fragment = new FindTranslationButtonsFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    private void readBundle(Bundle bundle) {
        if (bundle != null) {
            callback = (OnClickCallback) bundle.getSerializable("listener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_find_translation_buttons, null, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            buttons.add((View) Objects.requireNonNull(getView()).findViewById(R.id.leftUpperButton));
            buttons.add((View) getView().findViewById(R.id.leftDownButton));
            buttons.add((View) getView().findViewById(R.id.rightUpperButton));
            buttons.add((View) getView().findViewById(R.id.rightDownButton));

            readBundle(getArguments());

            for (View button:buttons) {
                button.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                callback.onClick(v);
                            }
                        });
            }
    }

    public List<View> getButtons() {
        return buttons;
    }
}
