package com.example.gallery.FindScreen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gallery.MainActivity;
import com.example.gallery.Model.AlbumModel;
import com.example.gallery.Model.AlbumRVAdapter;
import com.example.gallery.Model.DBHandler;
import com.example.gallery.R;
import com.example.gallery.ViewAlbum;

import java.util.ArrayList;

public class FindAlbumResult extends AppCompatActivity {
    Button btnQuayLai;
    private AlbumRVAdapter albumRVAdapter;
    private RecyclerView albumsRV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_album_result);

        btnQuayLai = (Button) findViewById(R.id.btnQuayLai);

        ArrayList<AlbumModel> albumList = (ArrayList<AlbumModel>) getIntent().getSerializableExtra("albumList");
        //hien thi ds album
        albumRVAdapter = new AlbumRVAdapter(albumList, FindAlbumResult.this);
        albumsRV = (RecyclerView) findViewById(R.id.idRVAlbums);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(FindAlbumResult.this,
                2,GridLayoutManager.VERTICAL, false);
        albumsRV.setLayoutManager(gridLayoutManager);
        albumsRV.setAdapter(albumRVAdapter);

        // quay lại màn hình chính
        btnQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(FindAlbumResult.this, ViewAlbum.class);
                startActivity(intent1);
            }
        });
    }
    @Override
    public void onRestart() {
        super.onRestart();
        this.recreate();
    }
}
