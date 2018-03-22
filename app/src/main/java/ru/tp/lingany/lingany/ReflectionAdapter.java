package ru.tp.lingany.lingany;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by anton on 22.03.18.
 */

public class ReflectionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<String> items;

    private static final int VIEW_TYPE_NONE = 1;
    private static final int VIEW_TYPE_ITEM = 2;

    public ReflectionAdapter(List<String> itemList) {
        items = itemList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();

        switch (viewType) {
            case VIEW_TYPE_ITEM:
                View titleView = LayoutInflater.from(context).inflate(VIEW_TYPE_ITEM, parent, false);
                return new LanguageHolder(titleView);

            default:
                throw new IllegalArgumentException("invalid view type");
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (getItemViewType(position)) {

            case VIEW_TYPE_ITEM:
                String item = items.get(position);
                ReflectionAdapter.LanguageHolder langHolder = ((ReflectionAdapter.LanguageHolder) holder);

                langHolder.textView.setText(item);
                break;

            default:
                throw new IllegalArgumentException("invalid view type");
        }
    }

    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_ITEM;

    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    class LanguageHolder extends RecyclerView.ViewHolder {
        TextView textView;

        LanguageHolder(View itemView) {
            super(itemView);
//            textView = itemView.findViewById(R.id.reflection_item_id);
        }
    }
}
