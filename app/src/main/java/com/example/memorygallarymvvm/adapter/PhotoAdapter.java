package com.example.memorygallarymvvm.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memorygallarymvvm.R;
import com.example.memorygallarymvvm.databinding.PhotoItemBinding;
import com.example.memorygallarymvvm.bean.PhotoItem;

import java.util.Collections;
import java.util.List;

import com.example.memorygallarymvvm.bean.PhotoItem;
import com.example.memorygallarymvvm.repository.Repository;
import com.example.memorygallarymvvm.util.Helper;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> implements ItemTouchAdapter{

    private List<PhotoItem> photoItems;
    private Context context;

    public void setPhotoItems(List<PhotoItem> photoItems) {
        this.photoItems = photoItems;
    }

    public PhotoAdapter(List<PhotoItem> photoItems, Context context) {
        this.photoItems = photoItems;
        this.context = context;
    }

    static class PhotoViewHolder extends RecyclerView.ViewHolder {

        PhotoItemBinding photoItemBinding;

        public PhotoViewHolder(@NonNull PhotoItemBinding itemView) {
            super(itemView.getRoot());
            photoItemBinding = itemView;
        }
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PhotoItemBinding photoItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                 R.layout.photo_item, parent, false);
        final PhotoViewHolder viewHolder = new PhotoViewHolder(photoItemBinding);
        photoItemBinding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("adapterPos",viewHolder.getAdapterPosition());
                Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_displayingFragment,bundle);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        PhotoItem item = photoItems.get(position);
        holder.photoItemBinding.setItem(item);
    }

    @Override
    public int getItemCount() {
        return photoItems.size();
    }

    @Override
    public void onItemMoved(int fromPosition, int toPosition) {
        Collections.swap(photoItems, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemSwiped(int position) {
        Repository.getInstance().deletePhotos(context, photoItems.get(position));
        notifyItemRemoved(position);
    }
}
