package com.example.memorygallarymvvm.adapter;

public interface ItemTouchAdapter {
    void onItemMoved(int fromPosition, int toPosition);
    void onItemSwiped(int position);
}
