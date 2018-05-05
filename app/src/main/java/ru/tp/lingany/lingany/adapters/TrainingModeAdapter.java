package ru.tp.lingany.lingany.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ru.tp.lingany.lingany.R;
import ru.tp.lingany.lingany.models.TrainingMode;


public class TrainingModeAdapter extends RecyclerView.Adapter<TrainingModeAdapter.TrainingModeViewHolder> {

    private List<TrainingMode> data;
    private ItemClickListener itemClickListener;


    public TrainingModeAdapter(List<TrainingMode> data, ItemClickListener listener) {
        this.data = data;
        this.itemClickListener = listener;
    }


    @Override
    public TrainingModeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new TrainingModeViewHolder(itemView, itemClickListener);
    }

    @Override
    public void onBindViewHolder(TrainingModeViewHolder holder, int position) {
        TrainingMode category = data.get(position);
        holder.title.setText(category.getTitle());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public interface ItemClickListener {

        void onClick(View view, int position);
    }

    public static class TrainingModeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView title;

        private ItemClickListener listener;

        TrainingModeViewHolder(View itemView, ItemClickListener listener){
            super(itemView);
            this.listener = listener;
            itemView.setOnClickListener(this);
            title = itemView.findViewById(R.id.title);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition());
        }
    }
}
