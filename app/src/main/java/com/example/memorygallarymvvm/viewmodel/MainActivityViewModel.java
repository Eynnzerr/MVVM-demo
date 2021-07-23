package com.example.memorygallarymvvm.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.memorygallarymvvm.bean.PhotoItem;
import com.example.memorygallarymvvm.repository.Repository;

import java.util.List;

public class MainActivityViewModel extends AndroidViewModel {

    private LiveData<List<PhotoItem>> allPhotoLive;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        allPhotoLive = Repository.getInstance().getAllPhotoLive(application);
    }

    public LiveData<List<PhotoItem>> getAllPhotoLive() {
        return allPhotoLive;
    }

    public void addPhotos(PhotoItem... photoItems) {
        Repository.getInstance().addPhotos(getApplication(), photoItems);
    }

    public void updatePhotos(PhotoItem... photoItems) {
        Repository.getInstance().updatePhotos(getApplication(),photoItems);
    }

    public List<PhotoItem> getPhotos() {
        return Repository.getInstance().getPhotoList(getApplication());
    }

    public List<PhotoItem> getPhotosByCreatedDate() {
        return Repository.getInstance().getPhotosByCreatedDate(getApplication());
    }

    public List<PhotoItem> getPhotosByModifiedDate() {
        return Repository.getInstance().getPhotosByModifiedDate(getApplication());
    }

}
