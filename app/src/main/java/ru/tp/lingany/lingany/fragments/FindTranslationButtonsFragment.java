package ru.tp.lingany.lingany.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ru.tp.lingany.lingany.R;

public class FindTranslationButtonsFragment extends Fragment {
    private List<View> buttons = new ArrayList<>();
    private final String TRANSLATION_BUTTONS_CREATED = "translationButtonsCreated";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_find_translation_buttons, null, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        try {
            buttons.add((View) Objects.requireNonNull(getView()).findViewById(R.id.leftUpperButton));
            buttons.add((View) getView().findViewById(R.id.leftDownButton));
            buttons.add((View) getView().findViewById(R.id.rightUpperButton));
            buttons.add((View) getView().findViewById(R.id.rightDownButton));

            Intent intent = new Intent(TRANSLATION_BUTTONS_CREATED);
            intent.setAction(TRANSLATION_BUTTONS_CREATED);
            getActivity().sendBroadcast(intent);
        } catch(NullPointerException ex) {

        }
    }

    public List<View> getButtons() {
        return buttons;
    }

}
