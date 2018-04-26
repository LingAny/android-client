package ru.tp.lingany.lingany.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.victor.loading.rotate.RotateLoading;

import ru.tp.lingany.lingany.R;

public class LoadingFragment extends Fragment {

    private RotateLoading loader;
    private ImageView refresh;

    private RefreshListener refreshListener;


    public interface RefreshListener {
        void onRefresh();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_loading, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        loader = view.findViewById(R.id.loader);
        refresh = view.findViewById(R.id.refresh);

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshListener.onRefresh();
            }
        });

        refresh.setVisibility(View.INVISIBLE);
        loader.start();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        refreshListener = (LoadingFragment.RefreshListener) context;
    }

    public void startLoading() {
        refresh.setVisibility(View.INVISIBLE);
        loader.setVisibility(View.VISIBLE);
        loader.start();
    }

    public void stopLoading() {
        loader.stop();
    }

    public void showRefresh() {
        stopLoading();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loader.setVisibility(View.INVISIBLE);
                refresh.setVisibility(View.VISIBLE);
            }
        }, 300);
    }
}
