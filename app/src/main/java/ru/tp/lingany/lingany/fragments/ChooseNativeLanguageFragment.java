package ru.tp.lingany.lingany.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import java.io.Serializable;

public class ChooseNativeLanguageFragment extends Fragment {

    private static final String LANG_CLICK_LISTENER = "LANG_CLICK_LISTENER";

    public interface LangClickListener {
        void onClick(View view, int position);
    }

    private LangClickListener itemClickListener;

    public static ChooseNativeLanguageFragment newInstance(ChooseNativeLanguageFragment.LangClickListener listener) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(LANG_CLICK_LISTENER, (Serializable) listener);

        ChooseNativeLanguageFragment fragment = new ChooseNativeLanguageFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

}
