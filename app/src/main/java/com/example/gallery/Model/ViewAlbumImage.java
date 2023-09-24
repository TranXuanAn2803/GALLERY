package com.example.gallery.Model;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.gallery.MainActivity;
import com.example.gallery.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ViewAlbumImage extends AppCompatActivity {
    FloatingActionButton addFabBtn, addFabDeviceBtn, addFabCameraBtn;
    DBHandler dataBaseHelper;
    GridView gridView;
    ArrayList<ImageModel> listImg;
    long albumId;
    String albumName;
    boolean hasPasscode;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int MY_CAMERA_REQUEST_CODE = 102;
    boolean isAddBtnVisible= false;
    private  static final String dir="MyAppDir";
    private  static final int IMAGE_PICK_CODE=1000;
    private  static final int PERMISSION_CODE=1001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_image_view);
        //Nhan album id va album name
        Bundle bundle = getIntent().getExtras();
        albumId = bundle.getLong("albumId");
        albumName = bundle.getString("albumName");
        hasPasscode = bundle.getBoolean("hasPasscode");
        gridView=(GridView) findViewById(R.id.grid_view);
        dataBaseHelper= new DBHandler(this);
        //lay ds anh cua album do tu db
        listImg= dataBaseHelper.readAlbumImage(albumId);
        gridView.setAdapter(new ImageAdapter(this, listImg));
        //Ket thuc activity passcode
        Intent intent = new Intent("finish_activity");
        sendBroadcast(intent);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(ViewAlbumImage.this, ViewImage.class);
                //Gui vi tri anh va album id
                Bundle bundle = new Bundle();
                bundle.putInt("position",position);
                bundle.putLong("albumId",albumId);
                i.putExtras(bundle);
                startActivity(i);
            }
        });
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.add_fab);
        //Neu la album favorites(id 2) thi khong them anh vao
        if (albumId==2)
        {
            floatingActionButton.hide();
        }
        addFabCameraBtn = (FloatingActionButton) findViewById(R.id.add_camera_fab);

        addFabCameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askCameraPermissions();
            }
        });
        //Them anh tu device
        addFabDeviceBtn= (FloatingActionButton) findViewById(R.id.add_device_fab);
        addFabDeviceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
                {
                    if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED)
                    {
                        //Neu chua duoc cho phep thi can xin phep duoc truy cap vao device
                        String[] permissions={Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permissions, PERMISSION_CODE);
                        //Lay anh len tu device
                        pickImageFromGallery();

                    }
                    else
                    {
                        //Lay anh len tu device
                        pickImageFromGallery();
                    }
                }
                else
                {
                    //Lay anh len tu device
                    pickImageFromGallery();
                }
                //An cac nut FAB
                hideAddFab();
            }
        });

        addFabCameraBtn= (FloatingActionButton) findViewById(R.id.add_camera_fab);
        addFabBtn= (FloatingActionButton) findViewById(R.id.add_fab);
        addFabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isAddBtnVisible)
                {
                    isAddBtnVisible=!isAddBtnVisible;
                    visibleAddFab();
                }
                else
                {
                    isAddBtnVisible=!isAddBtnVisible;

                    hideAddFab();
                }
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        setTitle(albumName);
        getMenuInflater().inflate(R.menu.album_image_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.slideshowButton:
               Intent i = new Intent(ViewAlbumImage.this, SlideShow.class);
                //gui album id
                Bundle bundle = new Bundle();
                bundle.putLong("albumId",albumId);
                i.putExtras(bundle);
                startActivity(i);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    //Khi resume can load lai danh sach anh
    @Override
    protected void onResume() {
        super.onResume();
        listImg= dataBaseHelper.readAlbumImage(albumId);
        gridView.setAdapter(new ImageAdapter(this, listImg));
    }

    //show cac nut FAB
    public void visibleAddFab()
    {
        addFabDeviceBtn.show();
        addFabCameraBtn.show();
    }
    //an cac nut FAB
    public void hideAddFab()
    {
        addFabDeviceBtn.hide();
        addFabCameraBtn.hide();
    }

    private void askCameraPermissions() {
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA},MY_CAMERA_PERMISSION_CODE);
        }
        else
        {
            openCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE)
            openCamera();
    }

    private void openCamera() {
        //Toast.makeText(ViewAlbumImage.this,"Camera Open",Toast.LENGTH_SHORT).show();
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera, MY_CAMERA_REQUEST_CODE);
    }
    //New intent de lay anh tu device len
    public void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);

    }
    // Ta asyncTask de luu anh vao trong vao bo nho
    private class MyAsyncTask extends AsyncTask<Void, Void, String> {

        private Context myContextRef;
        private String fileName;
        private Bitmap bitmap;
        private final ProgressDialog dialog;
        public MyAsyncTask(Context myContextRef, String fileName, Bitmap bitmap) {
            this.myContextRef = myContextRef;
            this.fileName=fileName;
            this.bitmap=bitmap;
            dialog = new ProgressDialog(ViewAlbumImage.this);
        }
        @Override
        protected String doInBackground(Void... voids) {
            ContextWrapper cw = new ContextWrapper(myContextRef);
            File directory = cw.getDir(dir, Context.MODE_PRIVATE);
            String newName=fileName;
            //Tao file moi dua tren duong dan co san
            File imageFile=new File(directory,newName);
            if(imageFile.exists())
            {
                newName=newName.substring(0,newName.lastIndexOf('.'))+java.time.LocalDateTime.now()+".jpg";
                imageFile=new File(directory,newName);
            }
            FileOutputStream fos = null;
            //chep file bitmap vao trong file moi new
            try {
                fos = new FileOutputStream(imageFile);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // tra ve ten voi file moi tao ra
            return directory.getAbsolutePath()+'/'+newName;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //hien thi dialog yeu cau cho doi
            this.dialog.setMessage("Wait load image being done. It can be a slow job!!!");
            this.dialog.setCancelable(false);
            this.dialog.show();
        }

        @Override
        protected void onPostExecute(String unused) {
            super.onPostExecute(unused);
            if(unused!=null&&unused!="") {

                String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
                if (this.dialog.isShowing()) this.dialog.dismiss();
                String imageFile=unused.substring(unused.lastIndexOf('/')+1);
                String directory=unused.substring(0,unused.lastIndexOf('/'));
                long imageId = dataBaseHelper.addImage(imageFile,directory,date);
                //them anh vao album_image table co album id la id cua album hien tai
                dataBaseHelper.addAlbumImage(albumId,imageId);
                //Neu ko co pass thi them anh vao album_image table co album id 1 (la id cua album timeline)
                if (!hasPasscode &&albumId!=1) {
                    dataBaseHelper.addAlbumImage(1,imageId);
                }
                listImg=dataBaseHelper.readAlbumImage(albumId);
                gridView.setAdapter(new ImageAdapter(ViewAlbumImage.this, listImg));
            }

        }

    }

    /*
    private String saveToInternalStorage(Bitmap bitmapImage, String fileName){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir(dir, Context.MODE_PRIVATE);
        File imageFile=new File(directory,fileName);
        if(imageFile.exists())
        {
            fileName=fileName.substring(0,fileName.lastIndexOf('.'))+java.time.LocalDateTime.now()+".jpg";
            imageFile=new File(directory,fileName);
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(imageFile);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath()+'/'+fileName;
    }
    */

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_PICK_CODE && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                Toast.makeText(this,"ERROR", Toast.LENGTH_SHORT).show();
                return;
            }
            Uri uri = data.getData();
            String fileName="";
            try {
                fileName= PathUtil.getPath(this, uri);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            fileName=fileName.substring(fileName.lastIndexOf('/')+1, fileName.lastIndexOf('.'))+".jpg";
            try {
                String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                MyAsyncTask myAsyncTask = new MyAsyncTask(getApplicationContext(), fileName,bitmap);
                myAsyncTask.execute();

                /*String path= saveToInternalStorage(bitmap, fileName);
                fileName=path.substring(path.lastIndexOf('/')+1);
                String directory=path.substring(0,path.lastIndexOf('/'));
                //them anh vao image table
                long imageId = dataBaseHelper.addImage(fileName,directory,date);
                //them anh vao album_image table co album id la id cua album hien tai
                dataBaseHelper.addAlbumImage(albumId,imageId);
                //Neu ko co pass thi them anh vao album_image table co album id 1 (la id cua album timeline)
                if (!hasPasscode &&albumId!=1) {
                    dataBaseHelper.addAlbumImage(1,imageId);
                }*/
                listImg=dataBaseHelper.readAlbumImage(albumId);
                gridView.setAdapter(new ImageAdapter(this, listImg));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == MY_CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                Toast.makeText(this,"ERROR", Toast.LENGTH_SHORT).show();
                return;
            }
            String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            String fileName=java.time.LocalDateTime.now()+".jpg";
            MyAsyncTask myAsyncTask = new MyAsyncTask(getApplicationContext(), fileName,bitmap);
            myAsyncTask.execute();
            /*fileName=path.substring(path.lastIndexOf('/')+1);
            String directory=path.substring(0,path.lastIndexOf('/'));
            //them anh vao image table
            long imageId = dataBaseHelper.addImage(fileName,directory,date);
            //them anh vao album_image table co album id la id cua album hien tai
            dataBaseHelper.addAlbumImage(albumId,imageId);
            //Neu ko co pass thi them anh vao album_image table co album id 1 (la id cua album timeline)
            if (!hasPasscode &&albumId!=1) {
                dataBaseHelper.addAlbumImage(1,imageId);
            }*/
            listImg=dataBaseHelper.readAlbumImage(albumId);
            gridView.setAdapter(new ImageAdapter(this, listImg));
        }

    }

}


