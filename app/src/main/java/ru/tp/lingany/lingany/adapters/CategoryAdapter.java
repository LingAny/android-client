package ru.tp.lingany.lingany.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Random;

import ru.tp.lingany.lingany.R;
import ru.tp.lingany.lingany.sdk.api.categories.Category;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private List<Category> data;
    private ItemClickListener itemClickListener;
    private int NUMBER_OF_IMAGES = 6;
    private int[] ConttextPhotos;

    private Context context;


    public CategoryAdapter(List<Category> data, ItemClickListener listener, Context context) {
        this.data = data;
        this.itemClickListener = listener;
        this.ConttextPhotos = new int[NUMBER_OF_IMAGES];
        this.ConttextPhotos[0] = R.drawable.ic_category_1;
        this.ConttextPhotos[1] = R.drawable.ic_category_2;
        this.ConttextPhotos[2] = R.drawable.ic_category_3;
        this.ConttextPhotos[3] = R.drawable.ic_category_4;
        this.ConttextPhotos[4] = R.drawable.ic_category_5;
        this.ConttextPhotos[5] = R.drawable.ic_category_6;
        this.context = context;
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
        Random ran = new Random();
        int x = ran.nextInt(NUMBER_OF_IMAGES - 1);
        int id = this.ConttextPhotos[x];
        new InflateImagesTask(context, holder.image, id).execute();
//        holder.image.setImageResource(id);
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
            AppCompatActivity context = (AppCompatActivity) referenceContext.get();
            ImageView imageView = referenceImageView.get();

            if (context == null || context.isFinishing() || imageView == null) return -1;

            drawable = ResourcesCompat.getDrawable(context.getResources(), resId, null);
            return 0;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            AppCompatActivity context = (AppCompatActivity) referenceContext.get();
            ImageView imageView = referenceImageView.get();

            if (context == null || context.isFinishing() || imageView == null) return;

            imageView.setImageDrawable(drawable);
        }
    }
}
