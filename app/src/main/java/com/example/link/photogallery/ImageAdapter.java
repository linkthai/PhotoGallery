package com.example.link.photogallery;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Link on 27-Apr-16.
 */
public class ImageAdapter extends BaseAdapter {


    private Context context; // main activity's context

    ArrayList<String> list;

    public ImageAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        //return smallImages.length;
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        // TODO Auto-generated method stub
        //return smallImages[pos];
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        // TODO Auto-generated method stub
        //return smallImages[pos];
        return 0;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(context).inflate(R.layout.imageview_grid_layout, parent, false);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageGridView);

        //imageView.setImageResource((smallImages[pos]));
        imageView.setImageBitmap(ImageDecoder.decodeSampledBitmapFromPath((String)getItem(pos),
                100, 100));
        return imageView;
    }
}
