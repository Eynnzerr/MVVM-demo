package com.example.memorygallarymvvm.databinding;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.View;

import androidx.navigation.Navigation;

import com.example.memorygallarymvvm.R;
import com.example.memorygallarymvvm.bean.PhotoItem;
import com.example.memorygallarymvvm.lifecycle.MainActivityListener;
import com.example.memorygallarymvvm.room.PhotoDatabase;

import java.util.concurrent.ExecutorService;

public class HomeFragHandleListener {

    private Context context;

    public HomeFragHandleListener(Context context) {
        this.context = context;
    }

    /*
    public void onButtonClicked(View view) {
        ExecutorService globalPool = MainActivityListener.globalThreadPool;
        switch (view.getId()) {
            case R.id.clear_db:
                globalPool.submit(new Runnable() {
                    @Override
                    public void run() {
                        PhotoDatabase.getDataBase(context).getPhotoDao().deleteAllPhotos();
                    }
                });
                break;
            case R.id.add_db:
                globalPool.submit(new Runnable() {
                    @Override
                    public void run() {
                        PhotoDatabase.getDataBase(context).getPhotoDao().addPhotos(new PhotoItem(getMipmapToUri(R.drawable.watermelon), "nothing"));
                    }
                });
                break;
            case R.id.nav_to_f1:
                Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_addingFragment);
                break;
            case R.id.nav_to_f2:
                Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_displayingFragment);
                break;
            default:
                break;
        }
    }*/

    private String getMipmapToUri(int resId) {
        Resources r = context.getResources();
        Uri uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                + r.getResourcePackageName(resId) + "/"
                + r.getResourceTypeName(resId) + "/"
                + r.getResourceEntryName(resId));

        return uri.toString();
    }

}
