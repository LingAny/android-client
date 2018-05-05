package ru.tp.lingany.lingany.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseIntArray;
import android.view.MenuItem;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ru.tp.lingany.lingany.R;
import ru.tp.lingany.lingany.fragments.LoadingFragment;
import ru.tp.lingany.lingany.fragments.SelectCategoryFragment;
import ru.tp.lingany.lingany.pages.CategoriesPage;
import ru.tp.lingany.lingany.pages.TrainingsPage;


public class MenuActivity extends AppCompatActivity implements
        LoadingFragment.RefreshListener,
        SelectCategoryFragment.CategoryClickListener {

    private UUID refId;

    public static final String EXTRA_REFLECTION = "EXTRA_REFLECTION";

    private ViewPager vp;

    private BottomNavigationViewEx bnve;

    private CategoriesPage categoriesPage;
    private TrainingsPage trainingsPage;

    @Override
    @SuppressWarnings("unchecked")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        refId = getRefId();
        initBottomNavigationBarWithViewPager();
    }

    @Override
    public void onClickCategory(int position) {
        categoriesPage.selectCategory(position);
    }

    @Override
    public void onRefresh() {
        categoriesPage.refresh();
    }

    private UUID getRefId() {
        Intent intent = getIntent();
        return UUID.fromString(intent.getStringExtra(EXTRA_REFLECTION));
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
        ArrayList<Fragment> fragments = new ArrayList<>();
        final SparseIntArray items = new SparseIntArray(size);

        categoriesPage = CategoriesPage.getInstance(refId);
        trainingsPage = new TrainingsPage();

        fragments.add(categoriesPage);
        fragments.add(trainingsPage);
        fragments.add(new LoadingFragment());

        items.put(R.id.action_categories, 0);
        items.put(R.id.action_shape, 1);
        items.put(R.id.action_hot, 2);

        VpAdapter vpAdapter = new VpAdapter(getSupportFragmentManager(), fragments);
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

        VpAdapter(FragmentManager fm, List<Fragment> data) {
            super(fm);
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Fragment getItem(int position) {
            return data.get(position);
        }
    }
}
