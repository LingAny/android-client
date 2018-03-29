package ru.tp.lingany.lingany.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ru.tp.lingany.lingany.R;
import ru.tp.lingany.lingany.models.Language;

/**
 * Created by anton on 29.03.18.
 */

public class LanguagesAdapter extends RecyclerView.Adapter<LanguagesAdapter.LanguageViewHolder> {

    private List<Language> data;

    public LanguagesAdapter(List<Language> data) {
        this.data = data;
    }

    @Override
    public LanguageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(LanguageViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class LanguageViewHolder extends RecyclerView.ViewHolder {
        public TextView title;

        LanguageViewHolder(View itemView){
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.lang_title);
        }
    }
}
