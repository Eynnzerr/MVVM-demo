package com.example.memorygallarymvvm.bean;

import android.util.Log;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.text.SimpleDateFormat;
import java.util.Date;


@Entity
public class PhotoItem {

    private static final String TAG = "PhotoItem";

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "photoPath")
    private String photoPath;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "created_date")
    private long createdDate;

    @ColumnInfo(name = "modified_date")
    private long modifiedDate;

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public String getDescription() {
        Log.d(TAG, id + "getDescription: ");
        return description;
    }

    public void setDescription(String description) {
        Log.d(TAG, id + "setDescription: " + description);
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }

    public long getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(long modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

        public PhotoItem(String photoPath, String name) {
        this.photoPath = photoPath;
        this.name = name;
        this.description = "No description.";
        createdDate = System.currentTimeMillis();
        modifiedDate = System.currentTimeMillis();
    }

    @Ignore
    public PhotoItem() {
        photoPath = "";
        description = "No description.";
        name = "null";
        createdDate = System.currentTimeMillis();
        modifiedDate = System.currentTimeMillis();
    }

    @Ignore
    public PhotoItem(String photoPath, String name, String description) {
        this.photoPath = photoPath;
        this.name = name;
        this.description = description;
        createdDate = System.currentTimeMillis();
        modifiedDate = System.currentTimeMillis();
    }

}
