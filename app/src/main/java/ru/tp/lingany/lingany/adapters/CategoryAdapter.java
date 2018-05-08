package ru.tp.lingany.lingany.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ru.tp.lingany.lingany.R;
import ru.tp.lingany.lingany.sdk.api.categories.Category;
import ru.tp.lingany.lingany.utils.IconCategoryGenerator;
import ru.tp.lingany.lingany.utils.InflateImageTask;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private Context context;
    private List<Category> data;
    private ItemClickListener itemClickListener;
    private IconCategoryGenerator iconCategoryGenerator;

    public CategoryAdapter(List<Category> data, ItemClickListener listener, Context context) {
        this.data = data;
        this.itemClickListener = listener;
        this.context = context;
        iconCategoryGenerator = new IconCategoryGenerator();

    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(itemView, itemClickListener);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        Category category = data.get(position);
        holder.title.setText(category.getTitle());
        new InflateImageTask(context, holder.image, iconCategoryGenerator.getIconResId()).execute();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public interface ItemClickListener {

        void onClick(View view, int position);
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView title;

        public ImageView image;

        private ItemClickListener listener;

        CategoryViewHolder(View itemView, ItemClickListener listener){
            super(itemView);
            this.listener = listener;
            itemView.setOnClickListener(this);
            title = itemView.findViewById(R.id.title);
            image = itemView.findViewById(R.id.thumbnail);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition());
        }
    }
}
