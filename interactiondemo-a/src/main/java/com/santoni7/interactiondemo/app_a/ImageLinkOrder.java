package com.santoni7.interactiondemo.app_a;

import com.santoni7.interactiondemo.lib.model.ImageLink;

import java.util.Comparator;

public enum ImageLinkOrder {

    NEWER_FIRST(R.id.action_order_by_date, (a, b) -> b.getTimestamp().compareTo(a.getTimestamp())),

    OLDER_FIRST(R.id.action_order_by_date_reversed, (a, b) -> -NEWER_FIRST.getComparator().compare(a, b)),

    BY_STATUS(R.id.action_order_by_status, (a, b) -> {
        int x = a.getStatus().getCode() - b.getStatus().getCode();
        return x != 0 ? x : NEWER_FIRST.getComparator().compare(a, b);
    });

    private Comparator<ImageLink> comparator;
    private int itemId;

    ImageLinkOrder(int menuItemId, Comparator<ImageLink> cmp) {
        comparator = cmp;
        itemId = menuItemId;
    }

    public Comparator<ImageLink> getComparator() {
        return comparator;
    }

    public int getMenuItemId() {
        return itemId;
    }

    public static ImageLinkOrder fromMenuItemId(int itemId) {
        switch (itemId) {
            case R.id.action_order_by_date:
                return ImageLinkOrder.NEWER_FIRST;
            case R.id.action_order_by_date_reversed:
                return ImageLinkOrder.OLDER_FIRST;
            case R.id.action_order_by_status:
                return ImageLinkOrder.BY_STATUS;
            default:
                return null;
        }
    }
}
