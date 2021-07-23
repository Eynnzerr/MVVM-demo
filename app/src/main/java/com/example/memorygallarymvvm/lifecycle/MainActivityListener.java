package com.example.memorygallarymvvm.lifecycle;

import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//将全局线程池和activity的生命周期绑定，防止内存泄露
public class MainActivityListener implements LifecycleObserver {
    public static ExecutorService globalThreadPool;
    private static final String TAG = "MainActivityListener";

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private void initPool() {
        Log.d(TAG, "initPool: onCreated");
        globalThreadPool = Executors.newCachedThreadPool();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private void shutDownPool() {
        Log.d(TAG, "shutDownPool: onDestroyed");
        if( globalThreadPool != null )
            globalThreadPool.shutdownNow();
    }
}
