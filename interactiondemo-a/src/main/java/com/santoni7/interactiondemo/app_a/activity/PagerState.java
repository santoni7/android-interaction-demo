package com.santoni7.interactiondemo.app_a.activity;

import com.santoni7.interactiondemo.app_a.R;
import com.santoni7.interactiondemo.app_a.fragment.HistoryFragment;
import com.santoni7.interactiondemo.app_a.fragment.TestFragment;

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

    public static PagerState fromPosition(int pos){
        if(pos < 0 || pos >= values().length){
            throw new IndexOutOfBoundsException("Position out of bounds");
        }
        return values()[pos];
    }

    public int getTitleResId() {
        return titleResId;
    }
}
