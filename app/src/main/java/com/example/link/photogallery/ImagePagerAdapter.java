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
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

//    @Override
//    public boolean isViewFromObject(View view, Object object) {
//        return view == ((TouchImageView) object);
//    }

    @Override
    public Fragment getItem(int position) {
        ImageFragmentView f = ImageFragmentView.newInstance(mContext);
        f.setImagePath(imageList.get(position));
        return f;
    }

//    @Override
//    public Object instantiateItem(ViewGroup container, int position) {
//        View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);
//
//        TouchImageView imageView = (TouchImageView) itemView.findViewById(R.id.iw_singleImage);
//
//        imageView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//
//                TouchImageView img = (TouchImageView) v;
//                SinglePhotoView photoView = (SinglePhotoView) mContext;
//
//                if (img.isZoomed()) {
//                    img.getParent().requestDisallowInterceptTouchEvent(true);
//                    photoView.hideText();
//                } else {
//                    img.getParent().requestDisallowInterceptTouchEvent(false);
//                    photoView.showText();
//                }
//
//                return false;
//            }
//        });
//
//        imageView.setImageURI(Uri.parse(imageList.get(position)));
//        //imageView.setImageBitmap(ImageDecoder.decodeSampledBitmapFromPath(imageList.get(position), width, height));
//
//        container.addView(itemView);
//
//        return itemView;
//    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //container.removeView((LinearLayout) object);
        super.destroyItem(container, position, object);
    }


}
