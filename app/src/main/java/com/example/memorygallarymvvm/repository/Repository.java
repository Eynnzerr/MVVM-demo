package com.example.memorygallarymvvm.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import com.example.memorygallarymvvm.bean.PhotoItem;
import com.example.memorygallarymvvm.lifecycle.MainActivityListener;
import com.example.memorygallarymvvm.room.PhotoDao;
import com.example.memorygallarymvvm.room.PhotoDatabase;

/**
 * singleton
 */
public class Repository {

    private volatile static Repository repository = new Repository();

    private Repository() {

    }

    public static Repository getInstance() {
        if( repository == null ) {
            synchronized (Repository.class) {
                if( repository == null ) repository = new Repository();
            }
        }
        return repository;
    }

    //返回按ID(创建时间)排序。由于可能引发空指针，这里不能使用全局线程池
    public List<PhotoItem> getPhotoList(Context context) {
        FutureTask ft = new FutureTask<List<PhotoItem>>(new Callable<List<PhotoItem>>() {
            @Override
            public List<PhotoItem> call() throws Exception {
                return PhotoDatabase.getDataBase(context).getPhotoDao().getAllPhotos();
            }
        });
        new Thread(ft).start();
        try {
            return (List<PhotoItem>) ft.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    //返回按创建时间排序
    public List<PhotoItem> getPhotosByCreatedDate(Context context) {
        Future<List<PhotoItem>> future = MainActivityListener.globalThreadPool.submit(new Callable<List<PhotoItem>>() {
            @Override
            public List<PhotoItem> call() throws Exception {
                return PhotoDatabase.getDataBase(context).getPhotoDao().getPhotosByCreatedDate();
            }
        });
        try {
            return future.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    //返回按修改时间排序
    public List<PhotoItem> getPhotosByModifiedDate(Context context) {
        Future<List<PhotoItem>> future = MainActivityListener.globalThreadPool.submit(new Callable<List<PhotoItem>>() {
            @Override
            public List<PhotoItem> call() throws Exception {
                return PhotoDatabase.getDataBase(context).getPhotoDao().getPhotosByModifiedDate();
            }
        });
        try {
            return future.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public LiveData<List<PhotoItem>> getAllPhotoLive(Context context) {
        return PhotoDatabase.getDataBase(context).getPhotoDao().getAllPhotosLive();
    }

    public void addPhotos(Context context, PhotoItem... photoItems) {
        MainActivityListener.globalThreadPool.submit(new Runnable() {
            @Override
            public void run() {
                PhotoDatabase.getDataBase(context).getPhotoDao().addPhotos(photoItems);
            }
        });
    }

    public void updatePhotos(Context context, PhotoItem... photoItems) {
        MainActivityListener.globalThreadPool.submit(new Runnable() {
            @Override
            public void run() {
                PhotoDatabase.getDataBase(context).getPhotoDao().updatePhotos(photoItems);
            }
        });
    }

    public void deletePhotos(Context context, PhotoItem... photoItems) {
        MainActivityListener.globalThreadPool.submit(new Runnable() {
            @Override
            public void run() {
                PhotoDatabase.getDataBase(context).getPhotoDao().deletePhotos(photoItems);
            }
        });
    }

}
