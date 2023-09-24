package com.example.gallery.Model;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.gallery.R;

import java.util.ArrayList;
//appter hien thi anh o dang gridview trong album

public class ImageAdapter extends BaseAdapter {
    private Context context;
    ArrayList<ImageModel> list;
    //Khoi tao ImageAdapter
    public ImageAdapter(Context mainActivityContext, ArrayList<ImageModel> listImg)
    {
        context=mainActivityContext;
        list=listImg;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position).getImage();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    //Set cac view cho gridview
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if(convertView==null)
        {
            imageView= new ImageView(context);
            int gridsize = context.getResources().getDimensionPixelOffset(R.dimen.gridview);
            imageView.setLayoutParams(new GridView.LayoutParams(gridsize, gridsize));
        }
        else
        {
            imageView=(ImageView) convertView;
        }
        imageView.setImageBitmap(list.get(position).getImage());
        imageView.setId(position);
        return imageView;
    }
}
