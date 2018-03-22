package ru.tp.lingany.lingany.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.tp.lingany.lingany.R;

/**
 * Created by anton on 22.03.18.
 */

public class ReflectionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }


    class LanguageHolder extends RecyclerView.ViewHolder {
        TextView textView;

        LanguageHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.reflection_item_id);
        }
    }
}
