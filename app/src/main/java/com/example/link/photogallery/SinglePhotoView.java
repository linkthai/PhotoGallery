package com.example.link.photogallery;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Link on 28-Apr-16.
 */
public class SinglePhotoView extends AppCompatActivity {

    TextView textName;
    TextView textDate;
    Toolbar toolBar;
    ViewPager viewPager;

    ArrayList<String> imageList;
    File imageFile;
    int pos;

    ShareActionProvider shareActionProvider;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_image_view);

        toolBar = (Toolbar) findViewById(R.id.toolbar_singleImage);
        setSupportActionBar(toolBar);

        textName = (TextView) findViewById(R.id.tw_imageName);
        textDate = (TextView) findViewById(R.id.tw_imageDate);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        initFile();

        initImage();
    }

    private void initFile() {
        Intent intent = getIntent();

        imageList = intent.getStringArrayListExtra(getResources().getString(R.string.intent_image_list));
        pos = intent.getIntExtra(getResources().getString(R.string.intent_position), 0);
    }

    private void initImage() {
        updateImageInfo();

        viewPager.setAdapter(new ImagePagerAdapter(this, imageList, pos));
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
            return true;
        } else if (id == R.id.action_edit) {

        } else if (id == R.id.action_share) {

        } else if (id == R.id.action_delete) {

        }
        return super.onOptionsItemSelected(item);
    }
}
