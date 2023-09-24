package com.example.gallery;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gallery.FindScreen.FindAlbumResult;
import com.example.gallery.Model.AddAlbum;
import com.example.gallery.Model.AlbumModel;
import com.example.gallery.Model.AlbumRVAdapter;
import com.example.gallery.Model.DBHandler;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ViewAlbum extends AppCompatActivity implements Serializable {
    private ArrayList<AlbumModel> albumModelArrayList;
    private DBHandler dbHandler;
    private AlbumRVAdapter albumRVAdapter;
    private RecyclerView albumsRV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_album);

        //hien thi ds album
        albumModelArrayList = new ArrayList<>();
        dbHandler = new DBHandler(this);
        albumModelArrayList = dbHandler.readAlbums();
        albumRVAdapter = new AlbumRVAdapter(albumModelArrayList, ViewAlbum.this);
        albumsRV = (RecyclerView) findViewById(R.id.idRVAlbums);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(ViewAlbum.this,
                2,GridLayoutManager.VERTICAL, false);
        albumsRV.setLayoutManager(gridLayoutManager);
        albumsRV.setAdapter(albumRVAdapter);

        //Cac cai dat lien quan bottom navigation
        BottomNavigationView navigationView = findViewById(R.id.bottomNavTimelineAlbum);
        navigationView.setSelectedItemId(R.id.albums);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.timeline:
                        Intent timelineIntent = new Intent(ViewAlbum.this, MainActivity.class);
                        startActivity(timelineIntent);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.albums:
                        return true;
                }
                return false;
            }
        });

    }

    @Override
    public void onRestart() {
        super.onRestart();
        this.recreate();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addButton:
                Intent intent = new Intent(ViewAlbum.this, AddAlbum.class);
                startActivity(intent);
                break;
            // click on sortDescBtn in menu --> Sort folder z->a
            case R.id.sortDescBtn:
                for (int i = 2;i<albumModelArrayList.size()-1;i++)
                    for (int j=i+1;j<albumModelArrayList.size();j++)
                    {
                        String name1 = albumModelArrayList.get(i).getName();
                        String name2 = albumModelArrayList.get(j).getName();
                        if (name1.compareTo(name2)>0)
                        {
                            Collections.swap(albumModelArrayList,i,j);
                        }
                    }
                albumRVAdapter.notifyDataSetChanged();
//                for (int j = 2; j < albumModelArrayList.size(); j++) {
//                    dbHandler.deleteAlbum(albumModelArrayList.get(j).getId());
//                }
//                for (int j = 2; j < albumModelArrayList.size(); j++) {
//                    dbHandler.addAlbum(albumModelArrayList.get(j).getName(), albumModelArrayList.get(j).getPassword());
//                }
//                albumModelArrayList.clear();
//                albumModelArrayList = dbHandler.readAlbums();
//                albumRVAdapter = new AlbumRVAdapter(albumModelArrayList, ViewAlbum.this);
//                albumsRV.setAdapter(albumRVAdapter);
//                albumRVAdapter.notifyDataSetChanged();
                break;

            // click on sortAscBtn in menu --> Sort folder a->z (làm ngược lại z->a)
            case R.id.sortAscBtn:
                for (int i = 2;i<albumModelArrayList.size()-1;i++)
                    for (int j=i+1;j<albumModelArrayList.size();j++)
                    {
                        String name1 = albumModelArrayList.get(i).getName();
                        String name2 = albumModelArrayList.get(j).getName();
                        if (name1.compareTo(name2)<0)
                        {
                            Collections.swap(albumModelArrayList,i,j);
                        }
                    }
                albumRVAdapter.notifyDataSetChanged();
//                for (int j = 2; j < albumModelArrayList.size(); j++) {
//                    dbHandler.deleteAlbum(albumModelArrayList.get(j).getId());
//                }
//                for (int j = 2; j < albumModelArrayList.size(); j++) {
//                    dbHandler.addAlbum(albumModelArrayList.get(j).getName(), albumModelArrayList.get(j).getPassword());
//                }
//                albumModelArrayList.clear();
//                albumModelArrayList = dbHandler.readAlbums();
//                albumRVAdapter = new AlbumRVAdapter(albumModelArrayList, ViewAlbum.this);
//                albumsRV.setAdapter(albumRVAdapter);

                break;
            // click on findBtn in menu --> find activity
            case R.id.findBtn:
                showDialogFindLayout();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDialogFindLayout() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.find_album);
        EditText edtAlbumName = (EditText) dialog.findViewById(R.id.edtAlbumName);
        Button btnExitFind = (Button) dialog.findViewById(R.id.btnExitFind);
        Button btnClickFind = (Button) dialog.findViewById(R.id.btnClickFind);

        btnClickFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String albumName = edtAlbumName.getText().toString().trim();
                boolean flag = false;
                ArrayList<AlbumModel> albumList = new ArrayList<>();
                for (int i = 0; i < albumModelArrayList.size(); i++) {
                    if(albumModelArrayList.get(i).getName().equals(albumName)) {
                        flag = true;
                        albumList.add(albumModelArrayList.get(i));
                    }
                }
                if (flag)
                {
                    Intent intent = new Intent(ViewAlbum.this, FindAlbumResult.class);
                    intent.putExtra("albumList", albumList);
                    startActivity(intent);
                }
                if(!flag) {
                    Toast.makeText(ViewAlbum.this, "Không tìm thấy", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnExitFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
