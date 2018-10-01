package com.santoni7.interactiondemo.app_a.data;

import com.santoni7.interactiondemo.app_a.R;
import com.santoni7.interactiondemo.lib.model.ImageLink;

import java.util.Comparator;

public enum LinkSortingOrderEnum {

    NEWER_FIRST(R.id.action_order_by_date, (a, b) -> b.getTimestamp().compareTo(a.getTimestamp())),

    OLDER_FIRST(R.id.action_order_by_date_reversed, (a, b) -> -NEWER_FIRST.getComparator().compare(a, b)),

    BY_STATUS(R.id.action_order_by_status, (a, b) -> {
        int x = a.getStatus().getCode() - b.getStatus().getCode();
        return x != 0 ? x : NEWER_FIRST.getComparator().compare(a, b);
    });

    private Comparator<ImageLink> comparator;
    private int itemId;

    LinkSortingOrderEnum(int menuItemId, Comparator<ImageLink> cmp) {
        comparator = cmp;
        itemId = menuItemId;
    }

    public Comparator<ImageLink> getComparator() {
        return comparator;
    }

    public int getMenuItemId() {
        return itemId;
    }

    public static LinkSortingOrderEnum fromMenuItemId(int itemId) {
        switch (itemId) {
            case R.id.action_order_by_date:
                return LinkSortingOrderEnum.NEWER_FIRST;
            case R.id.action_order_by_date_reversed:
                return LinkSortingOrderEnum.OLDER_FIRST;
            case R.id.action_order_by_status:
                return LinkSortingOrderEnum.BY_STATUS;
            default:
                return null;
        }
    }
}
