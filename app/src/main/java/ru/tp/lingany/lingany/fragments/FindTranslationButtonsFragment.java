package ru.tp.lingany.lingany.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ru.tp.lingany.lingany.R;

public class FindTranslationButtonsFragment extends Fragment {
    private List<TextView> buttons = new ArrayList<>();

    public interface OnClickCallback {
        void onClick(View view);
    }

    private OnClickCallback callback;

    public static FindTranslationButtonsFragment newInstance(OnClickCallback listener) {
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
            buttons.add((TextView) Objects.requireNonNull(getView()).findViewById(R.id.leftUpperButton));
            buttons.add((TextView) getView().findViewById(R.id.leftDownButton));
            buttons.add((TextView) getView().findViewById(R.id.rightUpperButton));
            buttons.add((TextView) getView().findViewById(R.id.rightDownButton));

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

    public void setWordsOnButtons(String translationWord, List<String> words) {
        int translationPosition = (int) (Math.random() * 3);
        for (int i = 0, j = 0; i < buttons.size(); ++i) {
            if (i == translationPosition) {
                buttons.get(i).setText(translationWord);
            } else {
                if (words.size() < 1) {
                    break;
                }
                buttons.get(i).setText(words.get(j));
                ++j;
            }
        }
    }
}
