package com.santoni7.interactiondemo.app_a;

import com.santoni7.interactiondemo.app_a.data.ImageLinkDao;
import com.santoni7.interactiondemo.app_a.data.ImageLinkDatabase;
import com.santoni7.interactiondemo.lib.model.ImageLink;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.Observable;

public class LinkRepository {
    private ImageLinkDao dao;
    public static final Comparator<ImageLink> COMPARATOR_BY_DATE = (a, b) -> b.getTimestamp().compareTo(a.getTimestamp());
    public static final Comparator<ImageLink> COMPARATOR_BY_STATUS =
            (a, b) -> {
                int x = a.getStatus().getCode() - b.getStatus().getCode();
                return x != 0 ? x : COMPARATOR_BY_DATE.compare(a, b);
            };


    public LinkRepository(ImageLinkDatabase database) {
        this.dao = database.getImageLinkDao();
    }

    public ImageLinkDao getDao() {
        return dao;
    }

    public Flowable<List<ImageLink>> getLinksSorted(Comparator<ImageLink> comparator) {
        return dao.getLinksFlowable()
                .map(links -> {
                    Collections.sort(links, comparator);
                    return links;
                });
    }


    public void insertAsync(ImageLink link) {
//        Completable.fromAction(() -> dao.insert(link))
//                .subscribeOn(Schedulers.io())
//                .subscribe();

        Observable.timer(500, TimeUnit.MILLISECONDS)
                .map(o -> {
                    dao.insert(link);
                    return "";
                })
                .subscribe();
    }

    // TODO: Remove
    public void clearAll() {
        Observable.timer(500, TimeUnit.MILLISECONDS)
                .map(o -> {
                    dao.clearAll();
                    return "";
                })
                .subscribe();

    }
}
