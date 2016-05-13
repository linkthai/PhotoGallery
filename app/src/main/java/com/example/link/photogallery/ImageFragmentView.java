package com.example.link.photogallery;

import android.content.Context;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by Link on 07-May-16.
 */
public class ImageFragmentView extends Fragment {
    private String imagePath;
    private TouchImageView imageView;
    Context mContext;
    BitmapCache bitmapCache;

    public static ImageFragmentView newInstance(Context context, BitmapCache cache) {
        ImageFragmentView f = new ImageFragmentView();
        f.mContext = context;
        f.bitmapCache = cache;
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.pager_item, container, false);
        imageView = (TouchImageView) root.findViewById(R.id.iw_singleImage);
        //imageView.setImageURI(Uri.parse(imagePath));
        //imageView.setImageBitmap(ImageDecoder.decodeSampledBitmapFromPath(imagePath, 500, 500));
        BitmapWorkerTask.loadBitmap(bitmapCache, imagePath, imageView, 500);


        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                TouchImageView img = (TouchImageView) v;
                SinglePhotoView photoView = (SinglePhotoView) mContext;

                if (img.isZoomed()) {
                    img.getParent().requestDisallowInterceptTouchEvent(true);
                    photoView.hideText();
                } else {
                    img.getParent().requestDisallowInterceptTouchEvent(false);
                    photoView.showText();
                }

                return false;
            }
        });

        return root;
    }

    public void setImagePath(String path) {
        this.imagePath = path;
    }
}
