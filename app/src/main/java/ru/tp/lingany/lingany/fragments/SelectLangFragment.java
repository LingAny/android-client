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
import java.util.Objects;

import ru.tp.lingany.lingany.R;
import ru.tp.lingany.lingany.adapters.LanguagesAdapter;
import ru.tp.lingany.lingany.sdk.languages.Language;

public class SelectLangFragment extends Fragment {

    private String title;
    private List<Language> languages;

    private LangClickListener langClickListener;

    private static final String LANGUAGES = "LANGUAGES";
    private static final String TITLE = "TITLE";

    public static SelectLangFragment getInstance(String title, List<Language> languages) {
        Bundle bundle = new Bundle();

        bundle.putSerializable(TITLE, title);
        bundle.putSerializable(LANGUAGES, (Serializable) languages);

        SelectLangFragment fragment = new SelectLangFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public interface LangClickListener {
        void onClickLanguage(int position);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_select_lang, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        readBundle(Objects.requireNonNull(getArguments()));

        TextView titleTextView = view.findViewById(R.id.title);
        RecyclerView langRecyclerView = view.findViewById(R.id.languages);

        titleTextView.setText(title);

        RecyclerView.LayoutManager categoryLayoutManager = new LinearLayoutManager(getContext());
        langRecyclerView.setLayoutManager(categoryLayoutManager);
        langRecyclerView.setAdapter(new LanguagesAdapter(languages, new ItemClickListener()));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        langClickListener = (LangClickListener) context;
    }

    @SuppressWarnings("unchecked")
    private void readBundle(Bundle bundle) {
        title = (String) bundle.getSerializable(TITLE);
        languages = (List<Language>) bundle.getSerializable(LANGUAGES);
    }

    private class ItemClickListener implements LanguagesAdapter.ItemClickListener {

        @Override
        public void onClick(View view, int position) {
            langClickListener.onClickLanguage(position);
        }
    }
}
