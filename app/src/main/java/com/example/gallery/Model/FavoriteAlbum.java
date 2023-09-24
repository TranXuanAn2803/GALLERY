package com.example.gallery.Model;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.GridView;

import com.example.gallery.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FavoriteAlbum extends Activity {
    long albumId;
    ArrayList<ImageModel> imageModelArrayList;
    DBHandler dataBaseHelper;
    private  int selectedPosition=0;
    GridView gridView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_album);
        //nhan album id
        Bundle bundle = getIntent().getExtras();
        albumId = bundle.getLong("albumId");
        selectedPosition = bundle.getInt("position");
        dataBaseHelper= new DBHandler(this);
        gridView=(GridView) findViewById(R.id.grid_view);
        //lay ds anh cua album do tu db
        imageModelArrayList= dataBaseHelper.readAlbumImage(albumId);
        //tempBitmap = imageModelArrayList.get(selectedPosition).getImage();
        int albumFavoriteID = 2;

        dataBaseHelper.addAlbumImage(2,imageModelArrayList.get(selectedPosition).getId());
        imageModelArrayList=dataBaseHelper.readAlbumImage(2);
        gridView.setAdapter(new ImageAdapter(this, imageModelArrayList));
        //dataBaseHelper.addAlbumImage(2,imageId);
    }

}
