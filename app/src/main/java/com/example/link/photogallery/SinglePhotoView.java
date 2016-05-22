package com.example.link.photogallery;

import android.Manifest;
import android.app.WallpaperManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.support.v4.app.Fragment;

/**
 * Created by Link on 28-Apr-16.
 */
public class SinglePhotoView extends AppCompatActivity {

    TextView textName;
    TextView textDate;
    Toolbar toolBar;
    ViewPager viewPager;
    LinearLayout textLayout;

    ArrayList<String> imageList;
    File imageFile;
    int pos;

    final private int REQUEST_PERMISSION_CODE = 123;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_image_view);

        textLayout = (LinearLayout) findViewById(R.id.txt_singleImage);

        textName = (TextView) findViewById(R.id.tw_imageName);
        textDate = (TextView) findViewById(R.id.tw_imageDate);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        sendRequestPermission();

        toolBar = (Toolbar) findViewById(R.id.toolbar_singleImage);
        setSupportActionBar(toolBar);

        initFile();
        initImage();
    }

    private void initFile() {
        Intent intent = getIntent();

        imageList = intent.getStringArrayListExtra(getResources().getString(R.string.intent_image_list));
        pos = intent.getIntExtra(getResources().getString(R.string.intent_position), 0);
        getSupportFragmentManager();
    }

    private void initImage() {
        updateImageInfo();

        viewPager.setAdapter(new ImagePagerAdapter(getSupportFragmentManager(), this, imageList, pos));
        viewPager.setCurrentItem(pos, true);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                pos = position;
                updateImageInfo();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void updateImageInfo() {
        imageFile = new File(imageList.get(pos));

        String fileName = imageFile.getName();

        if (fileName.indexOf(".") > 0)
            fileName = fileName.substring(0, fileName.lastIndexOf("."));

        textName.setText(fileName);

        Date lastModDate = new Date(imageFile.lastModified());
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        textDate.setText(dateFormat.format(lastModDate));
    }

    public void showText() {
        textLayout.setVisibility(View.VISIBLE);
        textName.setVisibility(View.VISIBLE);
        textDate.setVisibility(View.VISIBLE);
    }

    public void hideText() {
        textLayout.setVisibility(View.INVISIBLE);
        textName.setVisibility(View.INVISIBLE);
        textDate.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_single_image, menu);

        try {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        } catch (NullPointerException ex) {
            Log.d("EXCEPTION", ex.getMessage());
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_setBackground) {
            setBackground();
        } else if (id == R.id.action_edit) {
            editImage();
        } else if (id == R.id.action_share) {
            shareImage();
        } else if (id == R.id.action_delete) {
            deleteImage();
        }
        return super.onOptionsItemSelected(item);
    }

    private void editImage() {
        Intent editSingleImage = new Intent(SinglePhotoView.this, EditImageView.class);
        editSingleImage.putExtra(getResources().getString(R.string.intent_image_path), imageFile.getAbsolutePath());
        startActivityForResult(editSingleImage, 1);
    }

    private void shareImage() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");

        // For a file in shared storage.  For data in private storage, use a ContentProvider.
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(imageFile));
        startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.send_to)));
    }

    private void deleteImage() {
        DialogInterface.OnClickListener backgroundClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:

                        imageList.remove(pos);
                        setResult(RESULT_OK, null);

                        if (imageList.size() > 0)
                        {
                            if (pos >= imageList.size())
                                pos = imageList.size() - 1;
                        }

                        boolean result = imageFile.delete();

                        if (imageList.size() == 0)
                        {
                            finish();
                            return;
                        }

                        updateImageInfo();

                        viewPager.setAdapter(new ImagePagerAdapter(getSupportFragmentManager(), SinglePhotoView.this, imageList, pos));
                        viewPager.setCurrentItem(pos, true);

                        if (!result)
                        {
                            AlertDialog.Builder builder = new AlertDialog.Builder(SinglePhotoView.this);
                            builder.setMessage("Error while deleting image!")
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            //do things
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }

                        break;

                    case DialogInterface.BUTTON_NEGATIVE:

                        break;
                }
            }
        };

        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setMessage("Do you want to delete this image?").setPositiveButton("Yes", backgroundClickListener)
                .setNegativeButton("No", backgroundClickListener).show();
    }

    private void setBackground() {
        DialogInterface.OnClickListener backgroundClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:

                        WallpaperManager myWallpaperManager
                                = WallpaperManager.getInstance(getApplicationContext());

                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                        Bitmap bitmap = BitmapFactory.decodeFile(imageFile.toString(), options);

                        try {
                            myWallpaperManager.setBitmap(bitmap);
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        break;

                    case DialogInterface.BUTTON_NEGATIVE:

                        break;
                }
            }
        };

        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setMessage("Do you want to set this image as background?").setPositiveButton("Yes", backgroundClickListener)
                .setNegativeButton("No", backgroundClickListener).show();
    }

    private void sendRequestPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_PERMISSION_CODE);

            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_PERMISSION_CODE);
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



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){

            setResult(RESULT_OK, null);

            initImage();
        }
    }
}
