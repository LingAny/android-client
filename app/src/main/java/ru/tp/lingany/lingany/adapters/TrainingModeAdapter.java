package ru.tp.lingany.lingany.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ru.tp.lingany.lingany.R;
import ru.tp.lingany.lingany.models.TrainingMode;
import ru.tp.lingany.lingany.models.TrainingModeTypes;


public class TrainingModeAdapter extends RecyclerView.Adapter<TrainingModeAdapter.TrainingModeViewHolder> {

    private List<TrainingMode> data;
    private ItemClickListener itemClickListener;


    public TrainingModeAdapter(List<TrainingMode> data, ItemClickListener listener) {
        this.data = data;
        this.itemClickListener = listener;
    }


    @Override
    public TrainingModeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_training_mode, parent, false);
        return new TrainingModeViewHolder(itemView, itemClickListener);
    }

    @Override
    public void onBindViewHolder(TrainingModeViewHolder holder, int position) {
        TrainingMode mode = data.get(position);
        holder.title.setText(mode.getTitle());
        TrainingModeTypes.Type type = mode.getType();
        if (type == TrainingModeTypes.Type.SPRINT_N2F || type == TrainingModeTypes.Type.SPRINT_F2N) {
            holder.image.setImageResource(R.drawable.mode_sprint);
        } else if (type == TrainingModeTypes.Type.TRANSLATION_N2F || type == TrainingModeTypes.Type.TRANSLATION_F2N) {
            holder.image.setImageResource(R.drawable.mode_select);
        } else if (type == TrainingModeTypes.Type.TYPING_MODE) {
            holder.image.setImageResource(R.drawable.mode_type);
        } else if (type == TrainingModeTypes.Type.STUDY_MODE) {
            holder.image.setImageResource(R.drawable.mode_study);
        } else {
            holder.image.setImageResource(R.drawable.mode_select);
        }
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

        public ImageView image;

        private ItemClickListener listener;

        TrainingModeViewHolder(View itemView, ItemClickListener listener){
            super(itemView);
            this.listener = listener;
            itemView.setOnClickListener(this);
            title = itemView.findViewById(R.id.title);
            image = itemView.findViewById(R.id.image);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition());
        }
    }
}
