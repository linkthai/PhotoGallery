package com.example.link.photogallery;

import android.content.Context;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.text.method.Touch;
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
public class ImagePagerAdapter extends PagerAdapter {

    Context mContext;
    LayoutInflater mLayoutInflater;

    ArrayList<String> imageList;
    int pos;

    public ImagePagerAdapter(Context context, ArrayList<String> list, int position) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageList = list;
        pos = position;
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);

        TouchImageView imageView = (TouchImageView) itemView.findViewById(R.id.iw_singleImage);

        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                TouchImageView img = (TouchImageView) v;
                if (img.isZoomed()) {
                    img.getParent().requestDisallowInterceptTouchEvent(true);
                } else {
                    img.getParent().requestDisallowInterceptTouchEvent(false);
                }
                return false;
            }
        });

        imageView.setImageURI(Uri.parse(imageList.get(position)));

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }


}
