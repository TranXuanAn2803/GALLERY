package com.example.gallery.Model;

import android.app.Activity;
import android.os.Bundle;

import com.example.gallery.R;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

public class SlideShow extends Activity {
    SliderView sliderView;
    long albumId;
    ArrayList<ImageModel> imageModelArrayList;
    DBHandler dataBaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slideshow);
        //nhan album id
        Bundle bundle = getIntent().getExtras();
        albumId = bundle.getLong("albumId");
        dataBaseHelper= new DBHandler(this);
        //lay ds anh cua album do tu db
        imageModelArrayList= dataBaseHelper.readAlbumImage(albumId);
        sliderView = findViewById(R.id.slideshow);
        SliderAdapter sliderAdapter = new SliderAdapter(imageModelArrayList,SlideShow.this);
        sliderView.setSliderAdapter(sliderAdapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
        sliderView.startAutoCycle();
    }
}
