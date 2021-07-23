package com.example.memorygallarymvvm.ui;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.memorygallarymvvm.R;
import com.example.memorygallarymvvm.bean.PhotoItem;
import com.example.memorygallarymvvm.databinding.FragmentAddingBinding;
import com.example.memorygallarymvvm.viewmodel.MainActivityViewModel;

import static android.app.Activity.RESULT_OK;

public class AddingFragment extends Fragment {

    private Menu menu;
    private FragmentAddingBinding binding;
    private MainActivityViewModel viewModel;

    ActivityResultLauncher<String> requestPermission;
    ActivityResultLauncher<Intent> pickPhoto;

    private String path;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestPermission = registerForActivityResult(new ActivityResultContracts.RequestPermission(),
                new ActivityResultCallback<Boolean>() {
                    @Override
                    public void onActivityResult(Boolean result) {
                        if(result) openAlbum();
                        else Toast.makeText(getContext(), "You denied permission request.", Toast.LENGTH_SHORT).show();
                    }
                });
        pickPhoto = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if( result.getResultCode() == RESULT_OK) {
                            if(Build.VERSION.SDK_INT >= 19){
                                //4.4及以上系统使用这个方法处理图片
                                handleImageOnKitKat(result.getData());
                            }else{
                                //4.4以下系统使用这个方法处理图片
                                handeleImageBeforeKitKat(result.getData());
                            }
                        }
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewModel = new ViewModelProvider(getActivity()).get(MainActivityViewModel.class);

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_adding, container, false);

        //设置toolbar
        setHasOptionsMenu(true);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(binding.toolbar3);
        binding.toolbar3.setTitle("New");

        //为图片添加点击事件。直接在fragment中设置比使用databinding更简单。
        binding.addingImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                    requestPermission.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                else
                    openAlbum();
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        this.menu = menu;
        menu.clear();
        inflater.inflate(R.menu.toolbar, menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.check:
                PhotoItem photoItem = new PhotoItem();
                photoItem.setPhotoPath(path);
                photoItem.setName(binding.addingName.getText().toString());
                photoItem.setDescription(binding.addingDescription.getText().toString());
                photoItem.setModifiedDate(System.currentTimeMillis());
                viewModel.addPhotos(photoItem);
                Toast.makeText(getActivity(),"保存成功",Toast.LENGTH_SHORT).show();
                //Navigation.findNavController(binding.getRoot()).navigate(R.id.action_addingFragment_to_homeFragment);
                getActivity().onBackPressed();
                break;
        }
        return true;
    }

    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        pickPhoto.launch(intent);
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if(DocumentsContract.isDocumentUri(getContext(), uri)){
            //如果是 document 类型的 Uri，则通过 document id 处理
            String docId = DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id = docId.split(":")[1];//解析出数字格式的 id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            }else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        }else if ("content".equalsIgnoreCase(uri.getScheme())){
            //如果是 content 类型的 uri ， 则使用普通方式处理
            imagePath = getImagePath(uri, null);
        }else if("file".equalsIgnoreCase(uri.getScheme())){
            //如果是 file 类型的 Uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        displayImage(imagePath);//显示选中的图片
    }

    private void handeleImageBeforeKitKat(Intent data){
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        //通过 Uri 和 selection 来获取真实的图片路径
        Cursor cursor = getActivity().getContentResolver().query(uri, null, selection, null, null);
        if(cursor != null){
            if(cursor.moveToFirst()){
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath) {
        if(imagePath != null) {
            Glide.with(getContext()).load(imagePath).placeholder(R.drawable.empty).into(binding.addingImage);
            path = imagePath;
        }
        else
            Toast.makeText(getActivity(),"failed to get image", Toast.LENGTH_LONG).show();
    }
}