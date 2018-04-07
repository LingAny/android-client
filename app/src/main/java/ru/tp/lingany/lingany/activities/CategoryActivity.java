package ru.tp.lingany.lingany.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;

import java.util.List;
import java.util.UUID;

import ru.tp.lingany.lingany.R;
import ru.tp.lingany.lingany.adapters.CategoryAdapter;
import ru.tp.lingany.lingany.sdk.Api;
import ru.tp.lingany.lingany.sdk.categories.Category;


public class CategoryActivity extends AppCompatActivity {

    public static final String EXTRA_REFLECTION = "EXTRA_REFLECTION";

    private ProgressBar progress;

    private List<Category> categories;

    private RecyclerView categoryRecyclerView;

    private class CategoryClickListener implements CategoryAdapter.ItemClickListener {

        @Override
        public void onClick(View view, int position) {
            Log.i("tag", "[CategoryClickListener.onClick]");
            Category category = categories.get(position);
        }
    }

    private final ParsedRequestListener<List<Category>> getForRefListener = new ParsedRequestListener<List<Category>>() {
        @Override
        public void onResponse(List<Category> response) {
            Log.i("tag","onResponse");
            categories = response;
            progress.setVisibility(View.INVISIBLE);
            categoryRecyclerView.setAdapter(new CategoryAdapter(categories, new CategoryClickListener()));
        }

        @Override
        public void onError(ANError anError) {
            Log.e("tag", anError.toString());
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_category);

        categoryRecyclerView = findViewById(R.id.categories);
        RecyclerView.LayoutManager categoryLayoutManager = new LinearLayoutManager(this);
        categoryRecyclerView.setLayoutManager(categoryLayoutManager);

        Intent intent = getIntent();
        UUID reflectionId = UUID.fromString(intent.getStringExtra(EXTRA_REFLECTION));

        TextView title = findViewById(R.id.title);
        title.setText(reflectionId.toString());

        progress = findViewById(R.id.progress);
        progress.setVisibility(View.VISIBLE);

        Api.getInstance().categories().getForReflection(reflectionId, getForRefListener);
    }
}
