package ru.tp.lingany.lingany.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;

import java.util.List;

import ru.tp.lingany.lingany.R;
import ru.tp.lingany.lingany.adapters.LanguagesAdapter;
import ru.tp.lingany.lingany.sdk.Api;
import ru.tp.lingany.lingany.sdk.languages.Language;

public class SelectNativeLangFragment extends Fragment {

    private ProgressBar progressBar;
    private RecyclerView langRecyclerView;
    private TextView title;

    private NativeLangClickListener nativeLangClickListener;

    private List<Language> supportedLanguages;

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
        title = view.findViewById(R.id.title);
        progressBar = view.findViewById(R.id.progressBar);
        langRecyclerView = view.findViewById(R.id.languages);


        title.setText(getString(R.string.loadSupportedLang));
        progressBar.setVisibility(View.VISIBLE);

        RecyclerView.LayoutManager categoryLayoutManager = new LinearLayoutManager(getContext());
        langRecyclerView.setLayoutManager(categoryLayoutManager);

        Api.getInstance().languages().getAll(listener);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        nativeLangClickListener = (NativeLangClickListener) context;
    }

    private class ItemClickListener implements LanguagesAdapter.ItemClickListener {

        @Override
        public void onClick(View view, int position) {
            nativeLangClickListener.onNativeLangClick(view, supportedLanguages, position);
        }
    }

    private final ParsedRequestListener<List<Language>> listener = new ParsedRequestListener<List<Language>>() {
        @Override
        public void onResponse(List<Language> languages) {
            supportedLanguages = languages;
            title.setText(getString(R.string.chooseNativeLang));
            progressBar.setVisibility(View.INVISIBLE);
            langRecyclerView.setAdapter(new LanguagesAdapter(languages, new ItemClickListener()));
        }

        @Override
        public void onError(ANError anError) {
            Log.e("tag", anError.toString());
        }
    };
}
