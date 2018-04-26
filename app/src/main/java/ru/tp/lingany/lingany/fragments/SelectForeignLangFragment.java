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

public class SelectForeignLangFragment extends Fragment {

    private TextView title;
    private RecyclerView langRecyclerView;
    private ForeignLangClickListener foreignLangClickListener;

    private List<Language> languages;

    private static final String SUPPORTED_FOREIGN_LANGUAGES = "SUPPORTED_FOREIGN_LANGUAGES";


    public static SelectForeignLangFragment getInstance(List<Language> foreignLanguages) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(SUPPORTED_FOREIGN_LANGUAGES, (Serializable) foreignLanguages);

        SelectForeignLangFragment fragment = new SelectForeignLangFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public interface ForeignLangClickListener {
        void onForeignLangClick(View view, int position);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_select_foreign_lang, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        readBundle(Objects.requireNonNull(getArguments()));

        title = view.findViewById(R.id.title);
        langRecyclerView = view.findViewById(R.id.languages);

        title.setText(getString(R.string.chooseForeignLang));

        RecyclerView.LayoutManager categoryLayoutManager = new LinearLayoutManager(getContext());
        langRecyclerView.setLayoutManager(categoryLayoutManager);
        langRecyclerView.setAdapter(new LanguagesAdapter(languages, new ItemClickListener()));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        foreignLangClickListener = (ForeignLangClickListener) context;
    }

    @SuppressWarnings("unchecked")
    private void readBundle(Bundle bundle) {
        languages = (List<Language>) bundle.getSerializable(SUPPORTED_FOREIGN_LANGUAGES);
    }

    private class ItemClickListener implements LanguagesAdapter.ItemClickListener {

        @Override
        public void onClick(View view, int position) {
            foreignLangClickListener.onForeignLangClick(view, position);
        }
    }

}
