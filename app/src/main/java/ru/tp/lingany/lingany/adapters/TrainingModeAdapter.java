package ru.tp.lingany.lingany.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.List;

import ru.tp.lingany.lingany.R;
import ru.tp.lingany.lingany.models.TrainingMode;
import ru.tp.lingany.lingany.models.TrainingModeTypes;


public class TrainingModeAdapter extends RecyclerView.Adapter<TrainingModeAdapter.TrainingModeViewHolder> {

    private List<TrainingMode> data;
    private ItemClickListener itemClickListener;

    private Context context;


    public TrainingModeAdapter(List<TrainingMode> data, ItemClickListener listener, Context context) {
        this.data = data;
        this.itemClickListener = listener;
        this.context = context;
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

//        new InflateImagesTask(context, R.drawable.mode_sprint).execute(holder.image);


        if (type == TrainingModeTypes.Type.SPRINT) {
            new InflateImagesTask(context, holder.image, R.drawable.mode_sprint).execute();
//            holder.image.setImageResource(R.drawable.mode_sprint);
        } else if (type == TrainingModeTypes.Type.TRANSLATION) {
            new InflateImagesTask(context, holder.image, R.drawable.mode_select).execute();
//            holder.image.setImageResource(R.drawable.mode_select);
        } else if (type == TrainingModeTypes.Type.TYPING_MODE) {
            new InflateImagesTask(context, holder.image, R.drawable.mode_type).execute();
//            holder.image.setImageResource(R.drawable.mode_type);
        } else if (type == TrainingModeTypes.Type.STUDY_MODE) {
            new InflateImagesTask(context, holder.image, R.drawable.mode_study).execute();
//            holder.image.setImageResource(R.drawable.mode_study);
        } else {
            new InflateImagesTask(context, holder.image, R.drawable.mode_sprint).execute();
//            holder.image.setImageResource(R.drawable.mode_select);
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

    public static class InflateImagesTask extends AsyncTask<Void, Void, Integer> {

        private int resId;

        private Drawable drawable;

        private WeakReference<Context> referenceContext;
        private WeakReference<ImageView> referenceImageView;

        InflateImagesTask(Context context, ImageView imageView, int resId) {
            referenceContext = new WeakReference<>(context);
            referenceImageView = new WeakReference<>(imageView);
            this.resId = resId;
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            Context context = referenceContext.get();
            ImageView imageView = referenceImageView.get();

            if (context == null || imageView == null) return -1;

            drawable = ResourcesCompat.getDrawable(context.getResources(), resId, null);
            return 0;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            Context context = referenceContext.get();
            ImageView imageView = referenceImageView.get();

            if (context == null || imageView == null) return;

            imageView.setImageDrawable(drawable);
        }
    }
}
