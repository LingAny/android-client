package ru.tp.lingany.lingany.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.Serializable;
import java.util.List;

import ru.tp.lingany.lingany.R;
import ru.tp.lingany.lingany.adapters.LanguagesAdapter;
import ru.tp.lingany.lingany.sdk.languages.Language;

public class SelectNativeLangFragment extends Fragment {

    private RecyclerView langRecyclerView;
    private TextView title;

    private NativeLangClickListener nativeLangClickListener;

    private List<Language> languages;

    private static final String SUPPORTED_LANGUAGES = "SUPPORTED_LANGUAGES";

    public static SelectNativeLangFragment getInstance(List<Language> languages) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(SUPPORTED_LANGUAGES, (Serializable) languages);

        SelectNativeLangFragment fragment = new SelectNativeLangFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public interface NativeLangClickListener {
        void onNativeLangClick(View view, List<Language> supportedLanguages, int position);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_select_native_lang, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        readBundle(getArguments());

        title = view.findViewById(R.id.title);
        langRecyclerView = view.findViewById(R.id.languages);

        title.setText(getString(R.string.chooseNativeLang));

        RecyclerView.LayoutManager categoryLayoutManager = new LinearLayoutManager(getContext());
        langRecyclerView.setLayoutManager(categoryLayoutManager);
        langRecyclerView.setAdapter(new LanguagesAdapter(languages, new ItemClickListener()));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        nativeLangClickListener = (NativeLangClickListener) context;
    }

    @SuppressWarnings("unchecked")
    private void readBundle(Bundle bundle) {
        languages = (List<Language>) bundle.getSerializable(SUPPORTED_LANGUAGES);
    }

    private class ItemClickListener implements LanguagesAdapter.ItemClickListener {

        @Override
        public void onClick(View view, int position) {
            nativeLangClickListener.onNativeLangClick(view, languages, position);
        }
    }
}
