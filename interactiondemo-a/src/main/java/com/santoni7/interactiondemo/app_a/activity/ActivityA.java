package com.santoni7.interactiondemo.app_a.activity;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Toast;

import com.santoni7.interactiondemo.app_a.R;
import com.santoni7.interactiondemo.app_a.base.BaseActivity;
import com.santoni7.interactiondemo.app_a.fragment.HistoryFragment;
import com.santoni7.interactiondemo.app_a.fragment.TestFragment;
import com.santoni7.interactiondemo.app_a.data.ImageLinkDatabase;
import com.santoni7.interactiondemo.lib.AndroidUtils;
import com.santoni7.interactiondemo.app_a.ImageLinkOrder;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityA extends BaseActivity implements ContractA.View {

    private static final String TAG = ActivityA.class.getSimpleName();
    private static final String SAVED_STATE_KEY_PAGE = "com.santoni7.interactiondemo.app_a.PAGE";
    private static final String SAVED_STATE_KEY_ORDER_BY = "com.santoni7.interactiondemo.app_a.ORDER_BY";

    private ContractA.Presenter presenter;

    @BindView(R.id.viewPager) ViewPager viewPager;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tabs) TabLayout tabLayout;

    private ViewPagerAdapter viewPagerAdapter;

    private MenuItem menuItemOrderBy;

    private ImageLinkOrder linkOrder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: savedInstanceState=" + savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        presenter = new PresenterA();
        presenter.attachView(this);

        if(linkOrder == null) {
            linkOrder = ImageLinkOrder.NEWER_FIRST;
        }

        initView();

        presenter.viewReady();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    private void initView() {
        Log.d(TAG, "initView");
        setSupportActionBar(toolbar);

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.addOnPageChangeListener(onPageChangeListener);
        viewPagerAdapter.notifyDataSetChanged();

        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume(): linkOrder=" + linkOrder.name());


        presenter.updateDataOrdered(linkOrder);

        updateMenuItemOrderBy();


//
//        Handler h = new Handler(getMainLooper());
//        h.post(() -> AndroidUtils.hideKeyboard(this));
    }

    public void fragmentsUpdated() {
        getHistoryView().setPresenter(presenter);
        getTestView().setPresenter(presenter);
    }



    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(TAG, "onRestoreInstanceState: " + savedInstanceState);
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(SAVED_STATE_KEY_PAGE)) {
                int page = savedInstanceState.getInt(SAVED_STATE_KEY_PAGE);
                Log.d(TAG, "onRestoreInstanceState(): has SAVED_STATE_KEY_PAGE: page=" + page);
                viewPager.setCurrentItem(page);
            }
            if (savedInstanceState.containsKey(SAVED_STATE_KEY_ORDER_BY)) {
                linkOrder = ImageLinkOrder.valueOf(savedInstanceState.getString(SAVED_STATE_KEY_ORDER_BY));
                Log.d(TAG, "onRestoreInstanceState(): has SAVED_STATE_KEY_ORDER_BY: order=" + linkOrder);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState: currentItemPos = " + viewPager.getCurrentItem() + "; order=" + linkOrder.name());
        outState.putInt(SAVED_STATE_KEY_PAGE, viewPager.getCurrentItem());
        outState.putString(SAVED_STATE_KEY_ORDER_BY, linkOrder.name());
        super.onSaveInstanceState(outState);
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        menuItemOrderBy = menu.findItem(R.id.action_change_order);
        menuItemOrderBy.getIcon().setAlpha(0);


        updateMenuItemOrderBy();

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_order_by_date:
            case R.id.action_order_by_date_reversed:
            case R.id.action_order_by_status:
                linkOrder = ImageLinkOrder.fromMenuItemId(item.getItemId());
                presenter.updateDataOrdered(linkOrder);
                break;
        }

        SubMenu subMenu = menuItemOrderBy.getSubMenu();
        markOrderInMenu(subMenu, linkOrder);
        return super.onOptionsItemSelected(item);
    }

    /**
     * Adds icon to selected item, and removes from others, if any
     */
    private void markOrderInMenu(Menu menu, ImageLinkOrder order) {
        for (int i = 0; i < menu.size(); ++i) {
            MenuItem menuItem = menu.getItem(i);
            if (menuItem.getItemId() == order.getMenuItemId()) {
                menuItem.setIcon(R.mipmap.icon_checked);
            } else {
                menuItem.setIcon(null);
            }
        }
    }

    @Override
    public void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void updateMenuItemOrderBy() {
        PagerState currentState = PagerState.fromPosition(viewPager.getCurrentItem());
        Log.d(TAG, "\tonResume: currentState=" + currentState.toString());
        switch (currentState) {
            case TEST:
                hideMenuItemOrderBy();
                break;
            case HISTORY:
                showMenuItemOrderBy();
                break;
        }
    }

    @Override
    public void showMenuItemOrderBy() {
        if (menuItemOrderBy != null) {
            menuItemOrderBy.setEnabled(true);
            animateSortButton(false);
        } else {
            Log.e(TAG, "showMenuItemOrderBy: menuItemOrderBy is NULL");
        }
    }

    @Override
    public void hideMenuItemOrderBy() {
        if (menuItemOrderBy != null) {
            menuItemOrderBy.setEnabled(false);
            animateSortButton(true);
        } else {
            Log.e(TAG, "hideMenuItemOrderBy: menuItemOrderBy is NULL");
        }
    }

    @Override
    public HistoryView getHistoryView() {
        return (HistoryFragment) viewPagerAdapter.getItem(PagerState.HISTORY.ordinal());
    }

    @Override
    public TestView getTestView() {
        return (TestFragment) viewPagerAdapter.getItem(PagerState.TEST.ordinal());
    }

    @Override
    public ImageLinkDatabase getDatabase() {
        return ImageLinkDatabase.getDatabase(getApplicationContext());
    }


    /**
     * Applies fade in or fade out animation to "Order By" menu icon, depending on 'shouldFadeOut' parameter
     *
     * @param shouldFadeOut If true, icon would be fade out, otherwise fade in.
     */
    private void animateSortButton(boolean shouldFadeOut) {
        final int fadeInDuration = 400;
        final int fadeOutDuration = 200;

        final PropertyValuesHolder fadeOutHolder = PropertyValuesHolder.ofInt("alpha", 255, 0);
        final PropertyValuesHolder fadeInHolder = PropertyValuesHolder.ofInt("alpha", 0, 255);

        ObjectAnimator animator = ObjectAnimator
                .ofPropertyValuesHolder(menuItemOrderBy.getIcon(), shouldFadeOut ? fadeOutHolder : fadeInHolder);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setTarget(menuItemOrderBy.getIcon());
        animator.setDuration(shouldFadeOut ? fadeOutDuration : fadeInDuration);
        animator.start();
    }


    /**
     * Listens to ViewPager's scrolling between pages and informs presenter when page is selected
     */
    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {
        }

        @Override
        public void onPageScrollStateChanged(int i) {
        }

        @Override
        public void onPageSelected(int i) {
            AndroidUtils.hideKeyboard(ActivityA.this);
            presenter.onPageSelected(PagerState.values()[i]);
        }

    };

    /**
     * Implements FragmentPagerAdapter and shows fragments depending on viewpager state
     * Possible states are declared at PagerState enum
     */
    public class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final String TAG_ = TAG + "::" + ViewPagerAdapter.class.getSimpleName();
        Fragment[] fragments = new Fragment[PagerState.values().length];

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            Log.d(TAG_, "instantiateItem(..) called at position=" + position);
            Object item = super.instantiateItem(container, position);
            fragments[position] = (Fragment) item;
            fragmentsUpdated();
            return item;
        }

        @Override
        public Fragment getItem(int position) {
            Log.d(TAG_, "getItem(position) called with position=" + position);

            Fragment frag = fragments[position];

            if (frag == null) {
                Log.d(TAG_, "getItem(): fragments[position] is null, creating new instance");
                PagerState stateEnum = PagerState.fromPosition(position);
                switch (stateEnum) {
                    case TEST:
                        frag = TestFragment.newInstance();
                        break;
                    case HISTORY:
                        frag = HistoryFragment.newInstance();
                        break;
                    default:
                        Log.e(TAG, "ERROR: Not all possible states are processed in ViewPagerAdapter.getItem()");
                        throw new IllegalStateException("Not all possible states are processed in ViewPagerAdapter.getItem()");
                }
                fragments[position] = frag;
                fragmentsUpdated();
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


}
