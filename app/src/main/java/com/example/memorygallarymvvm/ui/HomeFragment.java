package com.example.memorygallarymvvm.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.memorygallarymvvm.R;
import com.example.memorygallarymvvm.adapter.PhotoAdapter;
import com.example.memorygallarymvvm.bean.PhotoItem;
import com.example.memorygallarymvvm.databinding.FragmentHomeBinding;
import com.example.memorygallarymvvm.databinding.HomeFragHandleListener;
import com.example.memorygallarymvvm.repository.Repository;
import com.example.memorygallarymvvm.viewmodel.MainActivityViewModel;

import java.util.List;
import java.util.concurrent.ExecutionException;


public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";

    private FragmentHomeBinding binding;
    private View view;
    private PhotoAdapter photoAdapter;
    private MainActivityViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home,container,false);
        view = binding.getRoot();
        binding.setHomeFragListener(new HomeFragHandleListener(getContext()));

        setHasOptionsMenu(true);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(binding.toolbar3);

        binding.floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_addingFragment);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated: called");
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView(binding.photoRecyclerView);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_by_created_time:
                photoAdapter.setPhotoItems(viewModel.getPhotosByCreatedDate());
                photoAdapter.notifyDataSetChanged();
                Toast.makeText(getContext(),"按创建时间排序",Toast.LENGTH_SHORT).show();
                break;
            case R.id.sort_by_modified_time:
                photoAdapter.setPhotoItems(viewModel.getPhotosByModifiedDate());
                photoAdapter.notifyDataSetChanged();
                Toast.makeText(getContext(),"按修改时间排序",Toast.LENGTH_SHORT).show();
                break;
            case R.id.sort_by_alphabet_time:
                Toast.makeText(getContext(),"按字母顺序排序",Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    private void initRecyclerView(RecyclerView recyclerView) {
        //设置layoutmanager
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        recyclerView.setLayoutManager(layoutManager);

        //设置adapter
        photoAdapter = new PhotoAdapter(Repository.getInstance().getPhotoList(getContext()), getContext());
        recyclerView.setAdapter(photoAdapter);
        ItemTouchedCallback callback = new ItemTouchedCallback(photoAdapter, getContext());
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        //获取viewmodel
        viewModel = new ViewModelProvider(getActivity()).get(MainActivityViewModel.class);
        LiveData<List<PhotoItem>> allPhotosLive = viewModel.getAllPhotoLive();
        PhotoAdapter finalPhotoAdapter = photoAdapter;
        allPhotosLive.observe(getViewLifecycleOwner(), new Observer<List<PhotoItem>>() {
            @Override
            public void onChanged(List<PhotoItem> photoItems) {
                finalPhotoAdapter.setPhotoItems(photoItems);
                finalPhotoAdapter.notifyDataSetChanged();
            }
        });
    }
}