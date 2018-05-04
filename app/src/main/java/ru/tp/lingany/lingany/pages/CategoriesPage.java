package ru.tp.lingany.lingany.pages;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import ru.tp.lingany.lingany.R;
import ru.tp.lingany.lingany.activities.TrainingActivity;
import ru.tp.lingany.lingany.fragments.LoadingFragment;
import ru.tp.lingany.lingany.fragments.SelectCategoryFragment;
import ru.tp.lingany.lingany.sdk.Api;
import ru.tp.lingany.lingany.sdk.api.categories.Category;
import ru.tp.lingany.lingany.utils.ListenerHandler;

public class CategoriesPage extends Fragment {

    private UUID refId;

    private List<Category> categories;

    private LoadingFragment loadingFragment;

    public static final String REFLECTION_ID = "REFLECTION_ID";

    public static final String CATEGORIES = "CATEGORIES";

    private ListenerHandler getForRefListenerHandler;

    public static CategoriesPage getInstance(UUID refId) {
        Bundle bundle = new Bundle();

        bundle.putSerializable(REFLECTION_ID, refId);

        CategoriesPage fragment = new CategoriesPage();
        fragment.setArguments(bundle);
        return fragment;
    }

    public void refresh() {
        loadingFragment.startLoading();
        getCategoriesForReflection();
    }

    public void selectCategory(int position) {
        Category category = categories.get(position);
        Intent intent = new Intent(getContext(), TrainingActivity.class);
        intent.putExtra(TrainingActivity.EXTRA_CATEGORY, category);
        startActivity(intent);
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        readBundle(Objects.requireNonNull(getArguments()));
        loadingFragment = new LoadingFragment();
        getForRefListenerHandler = ListenerHandler.wrap(ParsedRequestListener.class, new ParsedRequestListener<List<Category>>() {
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
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.page_categories, container, false);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            categories = (List<Category>) savedInstanceState.getSerializable(CATEGORIES);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        if (categories == null) {
            inflateLoadingFragment();
            getCategoriesForReflection();
        } else {
            inflateSelectCategoryFragment();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        if (categories != null) {
            outState.putSerializable(CATEGORIES, (Serializable) categories);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getForRefListenerHandler != null) {
            getForRefListenerHandler.unregister();
        }
    }

    @SuppressWarnings("unchecked")
    private void getCategoriesForReflection() {
        ParsedRequestListener<List<Category>> listener = (ParsedRequestListener<List<Category>>) getForRefListenerHandler.asListener();
        Api.getInstance().categories().getForReflection(refId, listener);
    }

    private void inflateSelectCategoryFragment() {
        Fragment fragment = SelectCategoryFragment.getInstance(categories);
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    private void inflateLoadingFragment() {
        getChildFragmentManager()
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

    @SuppressWarnings("unchecked")
    private void readBundle(Bundle bundle) {
        refId = (UUID) bundle.getSerializable(REFLECTION_ID);
    }
}
