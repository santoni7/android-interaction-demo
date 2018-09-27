package com.santoni7.interactiondemo.app_a.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.santoni7.interactiondemo.app_a.R;
import com.santoni7.interactiondemo.app_a.base.BaseActivity;
import com.santoni7.interactiondemo.app_a.fragment.HistoryFragment;
import com.santoni7.interactiondemo.app_a.fragment.TestFragment;
import com.santoni7.interactiondemo.app_a.data.ImageLinkDatabase;
import com.santoni7.interactiondemo.lib.mvp.MvpPresenter;

public class MainActivity extends BaseActivity implements MainContract.View {

    private static final String TAG = MainActivity.class.getSimpleName();

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    private TestFragment testFragment;
    private HistoryFragment historyFragment;

    private MainContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        presenter = new MainPresenter();
        presenter.attachView(this);

        initFragments();

        initView();

        presenter.viewReady();
    }

    private void initFragments() {
        testFragment = TestFragment.newInstance();
        historyFragment = HistoryFragment.newInstance();
        testFragment.setPresenter(presenter);
        historyFragment.setPresenter(presenter);
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = findViewById(R.id.viewPager);
        mViewPager.setAdapter(mSectionsPagerAdapter);


        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        testFragment.setPresenter(presenter);
        historyFragment.setPresenter(presenter);
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    public void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public HistoryView getHistoryView() {
        return historyFragment;
    }

    @Override
    public TestView getTestView() {
        return testFragment;
    }

    public MainContract.Presenter getPresenter() {
        return presenter;
    }

    @Override
    public ImageLinkDatabase getDatabase() {
        return ImageLinkDatabase.getDatabase(getApplicationContext());
    }

    /**
     * Implements FragmentPagerAdapter and shows fragments depending on viewpager state
     * Possible states are declared at PagerState enum
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            PagerState stateEnum = PagerState.values()[position];
            Fragment frag;
            switch (stateEnum) {
                case TEST:
                    frag = testFragment;
                    break;
                case HISTORY:
                    frag = historyFragment;
                    break;
                default:
                    Log.e(TAG, "ERROR: Not all possible states are processed in SectionsPagerAdapter.getItem()");
                    throw new IllegalStateException("Not all possible states are processed in SectionsPagerAdapter.getItem()");
            }
            return frag;
        }

        @Override
        public int getCount() {
            return PagerState.values().length;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return getString(PagerState.values()[position].getTitleResId());
        }
    }

    /**
     * Describes possible viewpager states (pages) and their titles
     */
    public enum PagerState {
        TEST(R.string.test_title),
        HISTORY(R.string.history_title);

        private int titleResId;

        PagerState(int titleResId) {
            this.titleResId = titleResId;
        }

        public int getTitleResId() {
            return titleResId;
        }
    }
}
