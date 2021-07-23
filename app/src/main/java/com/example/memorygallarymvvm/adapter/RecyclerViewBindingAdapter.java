package com.example.memorygallarymvvm.adapter;

import android.graphics.Color;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.example.memorygallarymvvm.R;
import com.example.memorygallarymvvm.base.BaseApplication;
import com.example.memorygallarymvvm.util.Helper;


public class RecyclerViewBindingAdapter {
    @BindingAdapter("itemImage")
    public static void setImage(ImageView imageView, String imagePath) {
        if( imagePath == null )
            imageView.setBackgroundColor(Color.DKGRAY);
        else
            Glide.with(BaseApplication.getContext())
            .load(imagePath)
            .placeholder(R.drawable.empty)
            .into(imageView);
    }

    @BindingAdapter("itemDate")
    public static void setDate(TextView textView, Long timeStamp) {
        textView.setText(Helper.formatTimeStamp(timeStamp));
    }
}
