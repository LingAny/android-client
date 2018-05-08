package ru.tp.lingany.lingany.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

public class InflateImageTask extends AsyncTask<Void, Void, Integer> {

    private int resId;

    private Drawable drawable;

    private WeakReference<Context> referenceContext;
    private WeakReference<ImageView> referenceImageView;

    public InflateImageTask(Context context, ImageView imageView, int resId) {
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