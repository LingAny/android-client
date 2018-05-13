package ru.tp.lingany.lingany.utils;


import android.widget.ImageView;

import com.squareup.picasso.Picasso;


public class PicassoImageInflater {

    public static void inflate(ImageView iv, int resId) {
        Picasso.get()
                .load(resId)
                .into(iv);
    }
}
