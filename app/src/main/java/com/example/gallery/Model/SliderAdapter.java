package com.example.gallery.Model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.example.gallery.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.Holder> {
    private ArrayList<ImageModel> imageModelArrayList;
    private Context context;

    // constructor
    public SliderAdapter(ArrayList<ImageModel> imageModelArrayList, Context context) {
        this.imageModelArrayList = imageModelArrayList;
        this.context = context;
    }
    @Override
    public SliderAdapter.Holder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_item, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(SliderAdapter.Holder viewHolder, int position) {
        viewHolder.imageView.setImageBitmap(imageModelArrayList.get(position).getImage());
    }

    @Override
    public int getCount() {
        return imageModelArrayList.size();
    }

    public class Holder extends SliderViewAdapter.ViewHolder{
        ImageView imageView;
        public Holder(View itemView)
        {
            super(itemView);
            imageView=itemView.findViewById(R.id.imageView);
        }
    }
}
