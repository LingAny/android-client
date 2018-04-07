package ru.tp.lingany.lingany.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ru.tp.lingany.lingany.R;
import ru.tp.lingany.lingany.sdk.languages.Language;


public class LanguagesAdapter extends RecyclerView.Adapter<LanguagesAdapter.LanguageViewHolder> {

    private List<Language> data;

    public LanguagesAdapter(List<Language> data) {
        this.data = data;
    }

    @Override
    public LanguageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lang, parent, false);
        return new LanguageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(LanguageViewHolder holder, int position) {
        Language lang = data.get(position);
        holder.title.setText(lang.getTitle());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class LanguageViewHolder extends RecyclerView.ViewHolder {
        public TextView title;

        LanguageViewHolder(View itemView){
            super(itemView);
            title = itemView.findViewById(R.id.lang_title);
        }
    }
}
