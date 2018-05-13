package ru.tp.lingany.lingany.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ru.tp.lingany.lingany.R;
import ru.tp.lingany.lingany.sdk.api.languages.Language;
import ru.tp.lingany.lingany.utils.FlagColorGenerator;
import ru.tp.lingany.lingany.utils.InflateImageTask;


public class LanguagesAdapter extends RecyclerView.Adapter<LanguagesAdapter.LanguageViewHolder> {

    private Context context;
    private List<Language> data;
    private ItemClickListener itemClickListener;
    private FlagColorGenerator colorGenerator;

    public LanguagesAdapter(List<Language> data, ItemClickListener listener, Context context) {
        this.data = data;
        this.itemClickListener = listener;
        this.context = context;
        colorGenerator = new FlagColorGenerator();
    }

    @Override
    public LanguageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lang, parent, false);
        return new LanguageViewHolder(itemView, itemClickListener);
    }

    @Override
    public void onBindViewHolder(LanguageViewHolder holder, int position) {
        Language lang = data.get(position);
        holder.title.setText(lang.getTitle());
        holder.image.setColorFilter(context.getResources().getColor(colorGenerator.getColorResId()));
//        PicassoImageInflater.inflate(holder.image, R.drawable.lang_flag);
        new InflateImageTask(context, holder.image,R.drawable.lang_flag).execute();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public interface ItemClickListener {

        void onClick(View view, int position);
    }

    public static class LanguageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView title;

        public ImageView image;

        private ItemClickListener listener;

        LanguageViewHolder(View itemView, ItemClickListener listener){
            super(itemView);
            this.listener = listener;
            itemView.setOnClickListener(this);
            title = itemView.findViewById(R.id.lang_title);
            image = itemView.findViewById(R.id.flag);
        }

        @Override
        public void onClick(View view) {
            Log.d("tag", "onClick");
            listener.onClick(view, getAdapterPosition());
        }
    }
}
