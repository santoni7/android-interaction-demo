package com.santoni7.interactiondemo.app_a.activity;

import com.santoni7.interactiondemo.app_a.R;
import com.santoni7.interactiondemo.app_a.fragment.HistoryFragment;
import com.santoni7.interactiondemo.app_a.fragment.TestFragment;

/**
 * Describes possible viewpager states (pages) and their titles
 */
public enum PagerState {
    TEST(R.string.test_title, TestFragment.class),
    HISTORY(R.string.history_title, HistoryFragment.class);

    private int titleResId;
    private Class<?> clazz;

    PagerState(int titleResId, Class<?> clazz) {
        this.titleResId = titleResId;
        this.clazz = clazz;
    }

    public static PagerState fromPosition(int pos){
        if(pos < 0 || pos >= values().length){
            throw new IndexOutOfBoundsException("Position out of bounds");
        }
        return values()[pos];
    }

    public int getTitleResId() {
        return titleResId;
    }

    public Class<?> getClazz() {
        return clazz;
    }
}
