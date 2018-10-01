package com.santoni7.interactiondemo.app_a.data;

import com.santoni7.interactiondemo.lib.model.ImageLink;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.reactivex.Flowable;

public class LinkRepository {
    private ImageLinkDao dao;

    public LinkRepository(ImageLinkDatabase database) {
        this.dao = database.getImageLinkDao();
    }

    public Flowable<List<ImageLink>> getSortedLinks(Comparator<ImageLink> comparator) {
        return dao.getLinksFlowable()
                .map(links -> {
                    Collections.sort(links, comparator);
                    return links;
                });
    }
}
