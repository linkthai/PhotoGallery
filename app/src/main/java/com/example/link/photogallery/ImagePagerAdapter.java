package com.example.link.photogallery;

import android.content.Context;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.text.method.Touch;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

/**
 * Created by Link on 29-Apr-16.
 */
public class ImagePagerAdapter extends FragmentStatePagerAdapter {

    Context mContext;
    LayoutInflater mLayoutInflater;

    BitmapCache bitmapCache;

    ArrayList<String> imageList;
    int pos;

    int height;
    int width;

    public ImagePagerAdapter(FragmentManager fm, Context context, ArrayList<String> list, int position) {
        super(fm);
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageList = list;
        pos = position;

        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((SinglePhotoView)context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        height = displaymetrics.heightPixels;
        width = displaymetrics.widthPixels;

        bitmapCache = new BitmapCache();
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public Fragment getItem(int position) {
        ImageFragmentView f = ImageFragmentView.newInstance(mContext, bitmapCache);
        f.setImagePath(imageList.get(position));
        return f;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //container.removeView((LinearLayout) object);
        super.destroyItem(container, position, object);
    }


}
