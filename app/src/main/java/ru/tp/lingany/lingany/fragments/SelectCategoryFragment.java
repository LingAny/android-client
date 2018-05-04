package ru.tp.lingany.lingany.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import ru.tp.lingany.lingany.R;
import ru.tp.lingany.lingany.adapters.CategoryAdapter;
import ru.tp.lingany.lingany.sdk.api.categories.Category;


public class SelectCategoryFragment extends Fragment {

    private List<Category> categories;

    private CategoryClickListener categoryClickListener;

    private static final String CATEGORIES = "CATEGORIES";

    public static SelectCategoryFragment getInstance(List<Category> categories) {
        Bundle bundle = new Bundle();

        bundle.putSerializable(CATEGORIES, (Serializable) categories);

        SelectCategoryFragment fragment = new SelectCategoryFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public interface CategoryClickListener {
        void onClickCategory(int position);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_select_category, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        readBundle(Objects.requireNonNull(getArguments()));

        RecyclerView langRecyclerView = view.findViewById(R.id.categories);
        RecyclerView.LayoutManager categoryLayoutManager = new LinearLayoutManager(getContext());
        langRecyclerView.setLayoutManager(categoryLayoutManager);
        langRecyclerView.setAdapter(new CategoryAdapter(categories, new ItemClickListener()));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        categoryClickListener = (CategoryClickListener) context;
    }

    @SuppressWarnings("unchecked")
    private void readBundle(Bundle bundle) {
        categories = (List<Category>) bundle.getSerializable(CATEGORIES);
    }

    private class ItemClickListener implements CategoryAdapter.ItemClickListener {

        @Override
        public void onClick(View view, int position) {
            categoryClickListener.onClickCategory(position);
        }
    }
}
