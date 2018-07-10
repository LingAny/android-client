package ru.tp.lingany.lingany.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ru.tp.lingany.lingany.R;


public class AuthFragment extends Fragment {

    private Button googleButton;
    private LayoutInflater inflater;

//    public static AuthFragment newInstance(TranslationData translationData) {
//        Bundle bundle = new Bundle();
//        bundle.putSerializable(TRANSLATION_DATA, (Serializable) translationData);
//
//        FindTranslationFragment fragment = new FindTranslationFragment();
//        fragment.setArguments(bundle);
//
//        return fragment;
//    }
//
//    @SuppressWarnings("unchecked")
//    private void readBundle(Bundle bundle) {
//        if (bundle != null) {
//            translationData = (TranslationData) bundle.getSerializable(TRANSLATION_DATA);
//        }
//    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.inflater = inflater;
        return inflater.inflate(R.layout.fragment_auth, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        readBundle(getArguments());

        googleButton = getView().findViewById(R.id.googleAuthButton);
        googleButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        processGoogleAuthorization(v);
                    }
                });
    }

    private void processGoogleAuthorization(View view) {

    }
}
