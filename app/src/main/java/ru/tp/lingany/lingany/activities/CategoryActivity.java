package ru.tp.lingany.lingany.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseIntArray;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import ru.tp.lingany.lingany.R;
import ru.tp.lingany.lingany.fragments.LoadingFragment;
import ru.tp.lingany.lingany.fragments.SelectCategoryFragment;
import ru.tp.lingany.lingany.fragments.TrainingHeaderFragment;
import ru.tp.lingany.lingany.sdk.Api;
import ru.tp.lingany.lingany.sdk.api.categories.Category;
import ru.tp.lingany.lingany.utils.ListenerHandler;


public class CategoryActivity extends AppCompatActivity implements
        LoadingFragment.RefreshListener,
        SelectCategoryFragment.CategoryClickListener {

    private UUID refId;

    private List<Category> categories;

    private LoadingFragment loadingFragment;

    public static final String EXTRA_REFLECTION = "EXTRA_REFLECTION";

    public static final String CATEGORIES = "CATEGORIES";

    private BottomNavigationViewEx bnve;
    private ViewPager vp;
    private VpAdapter vpAdapter;

    private static final String BRAIN_STORM_PAGE_POSITION = "BRAIN_STORM_PAGE_POSITION";
    private static final String SHAPE_PAGE_POSITION = "SHAPE_PAGE_POSITION";
    private static final String REPEAT_PAGE_POSITION = "REPEAT_PAGE_POSITION";

    private final HashMap<String, Integer> itemPositionMap;
    {
        itemPositionMap = new HashMap<>();
        itemPositionMap.put(BRAIN_STORM_PAGE_POSITION, 0);
        itemPositionMap.put(SHAPE_PAGE_POSITION, 1);
        itemPositionMap.put(REPEAT_PAGE_POSITION, 2);
    }

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
        initBottomNavigationBarWithViewPager();

//        if (savedInstanceState != null) {
//            categories = (List<Category>) savedInstanceState.getSerializable(CATEGORIES);
//            if (categories != null) {
//                inflateSelectCategoryFragment();
//                return;
//            }
//        }
//
//        inflateLoadingFragment();
//        getCategoriesForReflection();
    }

    @Override
    public void onStart() {
        super.onStart();

        getCategoriesForReflection();
    }

    @Override
    public void onResume() {
        super.onResume();
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
//        getCategoriesForReflection();
    }

    private ListenerHandler getForRefListenerHandler = ListenerHandler.wrap(ParsedRequestListener.class, new ParsedRequestListener<List<Category>>() {
        @Override
        public void onResponse(List<Category> response) {
            categories = response;
//            loadingFragment.stopLoading();
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
        int position = itemPositionMap.get(BRAIN_STORM_PAGE_POSITION);
        Fragment fragment = SelectCategoryFragment.getInstance(categories);
//        vpAdapter.getItem(position).g
        vpAdapter.replaceItem(fragment, position);
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
        }, 1000);
    }

    @SuppressWarnings("unchecked")
    private void initBottomNavigationBarWithViewPager() {
        vp = findViewById(R.id.vp);
        bnve = findViewById(R.id.bottom_navigation);

        int iconSize = getResources().getInteger(R.integer.bottomNavBarIconSize);
        int itemHeight = getResources().getInteger(R.integer.bottomNavBarItemHeight);

        bnve.setTextVisibility(false);
        bnve.setIconSize(iconSize, iconSize);
        bnve.setItemHeight(BottomNavigationViewEx.dp2px(this, itemHeight));

        int size = 3;
        List<Fragment> fragments = new LinkedList<>();
        final SparseIntArray items = new SparseIntArray(size);

        Category category1 = new Category(null, null, "title1", null);
        List<Category> catList1 = new ArrayList<Category>();
        catList1.add(category1);
        Fragment fragment1 = SelectCategoryFragment.getInstance(catList1);

        Category category2 = new Category(null, null, "title2", null);
        List<Category> catList2 = new ArrayList<Category>();
        catList2.add(category2);
        Fragment fragment2 = SelectCategoryFragment.getInstance(catList2);

        Category category3 = new Category(null, null, "title3", null);
        List<Category> catList3 = new ArrayList<Category>();
        catList3.add(category3);
        Fragment fragment3 = SelectCategoryFragment.getInstance(catList3);

//        fragments.add(fragment1);
//        fragments.add(fragment2);
//        fragments.add(fragment3);

        fragments.add(new LoadingFragment());
        fragments.add(new LoadingFragment());
        fragments.add(new LoadingFragment());

        items.put(R.id.action_categories, 0);
        items.put(R.id.action_shape, 1);
        items.put(R.id.action_hot, 2);

        vpAdapter = new VpAdapter(getSupportFragmentManager(), fragments);
        vp.setAdapter(vpAdapter);

        // set listener to change the current item of view pager when click bottom nav item
        bnve.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            private int previousPosition = -1;

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int position = items.get(item.getItemId());
                if (previousPosition != position) {
                    previousPosition = position;
                    vp.setCurrentItem(position);
                }
                return true;
            }
        });

        // set listener to change the current checked item of bottom nav when scroll view pager
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                bnve.setCurrentItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private class VpAdapter extends FragmentPagerAdapter {
        private List<Fragment> data;

        public VpAdapter(FragmentManager fm, List<Fragment> data) {
            super(fm);
            this.data = data;
        }

        public void replaceItem(Fragment fragment, int position) {

            data.set(position, fragment);
            vp.setCurrentItem(vp.getCurrentItem());
//            data.set(1, fragment);
//            data.set(2, fragment);
            vp.destroyDrawingCache();
            this.notifyDataSetChanged();
            vp.destroyDrawingCache();
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = data.get(position);
            return fragment;
        }

        @Override
        public int getItemPosition(Object object) {
            if (object instanceof SelectCategoryFragment) {
                return POSITION_NONE;
            }
            return POSITION_UNCHANGED;
        }
    }
}
