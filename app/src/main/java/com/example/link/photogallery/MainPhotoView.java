package com.example.link.photogallery;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.*;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class MainPhotoView extends AppCompatActivity {

    final private int REQUEST_PERMISSION_CODE = 123;
    public static final String POSITION_STRING = "com.whatever.appName.MESSAGE";
    public static final String EXTRA_MESSAGE = "com.whatever.appName.MESSAGE";

    Toolbar toolBar;
    GridView gridView;
    TextView textView;

    ArrayList<String> imageList;

    Context context;
    Bundle myOriginalMemoryBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_photo_view);

        context = this;
        myOriginalMemoryBundle = savedInstanceState;

        initToolbar();

        sendRequestPermission();

        initImageGridView();
    }

    private void initImageGridView() {
        imageList = getImageFromStorage(new File(Environment.getExternalStorageDirectory().toString()));
        toolBar.setTitle(String.valueOf(imageList.size()));

        gridView = (GridView) findViewById(R.id.gridview);
        gridView.setAdapter(new ImageAdapter(this, imageList));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                Intent openSingleImage = new Intent(MainPhotoView.this, SinglePhotoView.class);
                openSingleImage.putExtra(getResources().getString(R.string.intent_position), pos);
                openSingleImage.putStringArrayListExtra(getResources().getString(R.string.intent_image_list), imageList);
                context.startActivity(openSingleImage);
            }

        });
    }

    private void sendRequestPermission() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_PERMISSION_CODE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void initToolbar()
    {
        toolBar = (Toolbar) findViewById(R.id.toolbar);
        toolBar.setTitle(R.string.title);
        toolBar.setLogo(R.mipmap.ic_launcher);
        setSupportActionBar(toolBar);

    }

    ArrayList<String> getImageFromStorage(File root) {
        ArrayList<String> newList = new ArrayList<>();

        File[] files = root.listFiles();

        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                newList.addAll(getImageFromStorage(files[i]));
            }
            else
            {
                if (files[i].getName().endsWith(".jpg")) {
                    newList.add(files[i].toString());
                }
            }
        }

        return newList;
    }
}
