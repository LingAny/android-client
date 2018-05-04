package ru.tp.lingany.lingany.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import ru.tp.lingany.lingany.R;
import ru.tp.lingany.lingany.fragments.LoadingFragment;
import ru.tp.lingany.lingany.fragments.SelectCategoryFragment;
import ru.tp.lingany.lingany.sdk.Api;
import ru.tp.lingany.lingany.sdk.categories.Category;
import ru.tp.lingany.lingany.utils.ListenerHandler;


public class CategoryActivity extends AppCompatActivity implements
        LoadingFragment.RefreshListener,
        SelectCategoryFragment.CategoryClickListener {

    private UUID refId;

    private List<Category> categories;

    private LoadingFragment loadingFragment;

    public static final String EXTRA_REFLECTION = "EXTRA_REFLECTION";

    public static final String CATEGORIES = "CATEGORIES";

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        if (categories != null) {
            savedInstanceState.putSerializable(CATEGORIES, (Serializable) categories);
        }
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        refId = getRefId();
        loadingFragment = new LoadingFragment();

        if (savedInstanceState != null) {
            categories = (List<Category>) savedInstanceState.getSerializable(CATEGORIES);
            if (categories != null) {
                inflateSelectCategoryFragment();
                return;
            }
        }

        inflateLoadingFragment();
        getCategoriesForReflection();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (getForRefListenerHandler != null) {
            getForRefListenerHandler.unregister();
        }
    }

    @Override
    public void onClickCategory(int position) {
        Category category = categories.get(position);
        Intent intent = new Intent(CategoryActivity.this, TrainingActivity.class);
        intent.putExtra(TrainingActivity.EXTRA_CATEGORY, category);
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        loadingFragment.startLoading();
        getCategoriesForReflection();
    }

    private ListenerHandler getForRefListenerHandler = ListenerHandler.wrap(ParsedRequestListener.class, new ParsedRequestListener<List<Category>>() {
        @Override
        public void onResponse(List<Category> response) {
            categories = response;
            loadingFragment.stopLoading();
            inflateSelectCategoryFragment(getResources().getInteger(R.integer.delayInflateAfterLoading));
        }

        @Override
        public void onError(ANError anError) {
            loadingFragment.showRefresh();
        }
    });

    @SuppressWarnings("unchecked")
    private void getCategoriesForReflection() {
        ParsedRequestListener<List<Category>> listener = (ParsedRequestListener<List<Category>>) getForRefListenerHandler.asListener();
        Api.getInstance().categories().getForReflection(refId, listener);
    }

    private UUID getRefId() {
        Intent intent = getIntent();
        return UUID.fromString(intent.getStringExtra(EXTRA_REFLECTION));
    }

    private void inflateSelectCategoryFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, SelectCategoryFragment.getInstance(categories))
                .commit();
    }

    private void inflateLoadingFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, loadingFragment)
                .commit();
    }

    private void inflateSelectCategoryFragment(int delayMillis) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                inflateSelectCategoryFragment();
            }
        }, delayMillis);
    }
}
