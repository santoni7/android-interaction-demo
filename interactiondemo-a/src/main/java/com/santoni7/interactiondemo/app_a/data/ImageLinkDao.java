package com.santoni7.interactiondemo.app_a.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.Cursor;

import com.santoni7.interactiondemo.lib.model.ImageLink;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface ImageLinkDao {

    /**
     * Cursor selection methods
     */
    @Query("SELECT * FROM " + ImageLink.TABLE_NAME)
    Cursor selectAll();

    @Query("SELECT * FROM " + ImageLink.TABLE_NAME + " WHERE linkId = :linkId")
    Cursor selectById(long linkId);


    @Query("SELECT * FROM " + ImageLink.TABLE_NAME)
    Flowable<List<ImageLink>> getLinksFlowable();



    @Query("SELECT * FROM " + ImageLink.TABLE_NAME)
    List<ImageLink> getLinks();

    @Query("SELECT * FROM " + ImageLink.TABLE_NAME + " WHERE linkId = :linkId")
    ImageLink getLinkById(long linkId);

    @Insert
    long insert(ImageLink link);

    @Insert
    long[] insertAll(List<ImageLink> links);

    @Update
    int update(ImageLink link);

    @Delete
    int delete(ImageLink link);

    @Query("DELETE FROM " + ImageLink.TABLE_NAME + " WHERE linkId = :linkId")
    int delete(long linkId);

    @Query("DELETE FROM Links")
    void clearAll();

}
