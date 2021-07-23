package com.example.memorygallarymvvm.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import com.example.memorygallarymvvm.bean.PhotoItem;

@Dao
public interface PhotoDao {
    @Insert
    void addPhotos(PhotoItem... photoItems);

    @Update
    void updatePhotos(PhotoItem... photoItems);

    @Delete
    void deletePhotos(PhotoItem... photoItems);

    @Query("SELECT * FROM PhotoItem")
    List<PhotoItem> getAllPhotos();

    @Query("SELECT * FROM PhotoItem Where name = :name")
    PhotoItem getPhotoByName(String name);

    @Query("DELETE FROM PhotoItem")
    void deleteAllPhotos();

    @Query("SELECT * FROM PhotoItem ORDER BY ID")
    LiveData<List<PhotoItem>> getAllPhotosLive();

    @Query("SELECT * FROM PhotoItem ORDER BY created_date DESC")
    List<PhotoItem> getPhotosByCreatedDate();

    @Query("SELECT * FROM PhotoItem ORDER BY modified_date DESC")
    List<PhotoItem> getPhotosByModifiedDate();
}
