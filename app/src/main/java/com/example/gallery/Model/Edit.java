package com.example.gallery.Model;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gallery.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Edit extends Activity {
    ImageView imageResult;
    final int RQS_IMAGE1 = 1;
    ImageButton brightness, rotate, crop, draw, red, green,blue,black,white, editText, heart, star, smile, cry, angry, sticker;
    ImageButton dark3,dark2,dark1, root,light1,light2,light3;
    EditText insertText;
    Button applyBtn;

    LinearLayout drawLayout;
    Button savebtn;
    Bitmap bitmapMaster;
    Canvas canvasMaster;
    int prvX, prvY;
    Paint paintDraw;
    ArrayList<ImageModel> listImg;
    DBHandler dataBaseHelper;
    String mode="none";
    private  static final String dir="MyAppDir";
    long albumId;
    private  int selectedPosition=0;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        //Nhan album id
        Bundle bundle = getIntent().getExtras();
        albumId = bundle.getLong("albumId");
        selectedPosition = bundle.getInt("position");
        dataBaseHelper= new DBHandler(this);
        //lay ds anh cua album do tu db
        listImg= dataBaseHelper.readAlbumImage(albumId);
        imageResult = (ImageView)findViewById(R.id.imageDrawing);
        savebtn=(Button) findViewById(R.id.saveImageAfterDraw);
        Bitmap tempBitmap = null;
        tempBitmap = listImg.get(selectedPosition).getImage();
        Bitmap.Config config;
        if(tempBitmap.getConfig() != null){
            config = tempBitmap.getConfig();
        }else{
            config = Bitmap.Config.ARGB_8888;
        }
        bitmapMaster = Bitmap.createBitmap(
                tempBitmap.getWidth(),
                tempBitmap.getHeight(),
                config);
        canvasMaster = new Canvas(bitmapMaster);
        canvasMaster.drawBitmap(tempBitmap, 0, 0, null);
        imageResult.setImageBitmap(bitmapMaster);
        drawLayout = (LinearLayout) findViewById(R.id.editDraw);
        red = (ImageButton) findViewById(R.id.colorRed);  green = (ImageButton) findViewById(R.id.colorGreen);
        blue = (ImageButton) findViewById(R.id.colorBlue); black = (ImageButton) findViewById(R.id.colorBack);
        white = (ImageButton) findViewById(R.id.colorWhite);
        brightness = (ImageButton) findViewById(R.id.brightness); crop = (ImageButton) findViewById(R.id.crop);
        rotate = (ImageButton) findViewById(R.id.rotate); draw = (ImageButton) findViewById(R.id.draw);
        editText = (ImageButton) findViewById(R.id.editText);
        dark3 = (ImageButton) findViewById(R.id.dark3); dark2 = (ImageButton) findViewById(R.id.dark2);
        dark1 = (ImageButton) findViewById(R.id.dark1); root = (ImageButton) findViewById(R.id.img_root);
        light1 = (ImageButton) findViewById(R.id.light1); light2 = (ImageButton) findViewById(R.id.light2);
        light3 = (ImageButton) findViewById(R.id.light3);
        insertText= (EditText) findViewById(R.id.textInsert);
        applyBtn=(Button) findViewById(R.id.applyBtn);
        heart = (ImageButton) findViewById(R.id.heartSticker);
        star = (ImageButton) findViewById(R.id.starSticker);
        smile = (ImageButton) findViewById(R.id.smileSticker);
        cry = (ImageButton) findViewById(R.id.crySticker);
        angry = (ImageButton) findViewById(R.id.angrySticker);
        sticker=(ImageButton) findViewById(R.id.sticker);
        dark3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap recycle =adjustedContrast(bitmapMaster,-20);
                Bitmap.Config config;
                if(recycle.getConfig() != null){
                    config = recycle.getConfig();
                }else{
                    config = Bitmap.Config.ARGB_8888;
                }
                bitmapMaster = Bitmap.createBitmap(
                        recycle.getWidth(),
                        recycle.getHeight(),
                        config);
                canvasMaster = new Canvas(bitmapMaster);
                canvasMaster.drawBitmap(recycle, 0, 0, null);
                imageResult.setImageBitmap(bitmapMaster);
            }
        });
        dark2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap recycle =adjustedContrast(bitmapMaster,-10);
                Bitmap.Config config;
                if(recycle.getConfig() != null){
                    config = recycle.getConfig();
                }else{
                    config = Bitmap.Config.ARGB_8888;
                }
                bitmapMaster = Bitmap.createBitmap(
                        recycle.getWidth(),
                        recycle.getHeight(),
                        config);
                canvasMaster = new Canvas(bitmapMaster);
                canvasMaster.drawBitmap(recycle, 0, 0, null);
                imageResult.setImageBitmap(bitmapMaster);
            }
        });
        dark1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap recycle =adjustedContrast(bitmapMaster,-5);
                Bitmap.Config config;
                if(recycle.getConfig() != null){
                    config = recycle.getConfig();
                }else{
                    config = Bitmap.Config.ARGB_8888;
                }
                bitmapMaster = Bitmap.createBitmap(
                        recycle.getWidth(),
                        recycle.getHeight(),
                        config);
                canvasMaster = new Canvas(bitmapMaster);
                canvasMaster.drawBitmap(recycle, 0, 0, null);
                imageResult.setImageBitmap(bitmapMaster);
            }
        });
        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap recycle =adjustedContrast(bitmapMaster,0);
                Bitmap.Config config;
                if(recycle.getConfig() != null){
                    config = recycle.getConfig();
                }else{
                    config = Bitmap.Config.ARGB_8888;
                }
                bitmapMaster = Bitmap.createBitmap(
                        recycle.getWidth(),
                        recycle.getHeight(),
                        config);
                canvasMaster = new Canvas(bitmapMaster);
                canvasMaster.drawBitmap(recycle, 0, 0, null);
                imageResult.setImageBitmap(bitmapMaster);
            }
        });
        light1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap recycle =adjustedContrast(bitmapMaster,5);
                Bitmap.Config config;
                if(recycle.getConfig() != null){
                    config = recycle.getConfig();
                }else{
                    config = Bitmap.Config.ARGB_8888;
                }
                bitmapMaster = Bitmap.createBitmap(
                        recycle.getWidth(),
                        recycle.getHeight(),
                        config);
                canvasMaster = new Canvas(bitmapMaster);
                canvasMaster.drawBitmap(recycle, 0, 0, null);
                imageResult.setImageBitmap(bitmapMaster);
            }
        });
        light2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap recycle =adjustedContrast(bitmapMaster,10);
                Bitmap.Config config;
                if(recycle.getConfig() != null){
                    config = recycle.getConfig();
                }else{
                    config = Bitmap.Config.ARGB_8888;
                }
                bitmapMaster = Bitmap.createBitmap(
                        recycle.getWidth(),
                        recycle.getHeight(),
                        config);
                canvasMaster = new Canvas(bitmapMaster);
                canvasMaster.drawBitmap(recycle, 0, 0, null);
                imageResult.setImageBitmap(bitmapMaster);
            }
        });
        light3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap recycle =adjustedContrast(bitmapMaster,20);
                Bitmap.Config config;
                if(recycle.getConfig() != null){
                    config = recycle.getConfig();
                }else{
                    config = Bitmap.Config.ARGB_8888;
                }
                bitmapMaster = Bitmap.createBitmap(
                        recycle.getWidth(),
                        recycle.getHeight(),
                        config);
                canvasMaster = new Canvas(bitmapMaster);
                canvasMaster.drawBitmap(recycle, 0, 0, null);
                imageResult.setImageBitmap(bitmapMaster);
            }
        });
        brightness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap src = bitmapMaster;
                Bitmap bmResize = getResizedBitmap(src,150,150);
                Bitmap bmdark3 = adjustedContrast(bmResize,-20);
                Bitmap bmdark2 = adjustedContrast(bmResize,-10);
                Bitmap bmdark1 = adjustedContrast(bmResize,-5);
                Bitmap bmroot = adjustedContrast(bmResize,0);
                Bitmap bmlight1 = adjustedContrast(bmResize,5);
                Bitmap bmlight2 = adjustedContrast(bmResize,10);
                Bitmap bmlight3 = adjustedContrast(bmResize,20);
                dark3.setImageBitmap(bmdark3);    dark3.setVisibility(View.VISIBLE);
                dark2.setImageBitmap(bmdark2);    dark2.setVisibility(View.VISIBLE);
                dark1.setImageBitmap(bmdark1);    dark1.setVisibility(View.VISIBLE);
                root.setImageBitmap(bmroot);      root.setVisibility(View.VISIBLE);
                light1.setImageBitmap(bmlight1);  light1.setVisibility(View.VISIBLE); drawLayout.setVisibility(View.INVISIBLE);
                light2.setImageBitmap(bmlight2);  light2.setVisibility(View.VISIBLE);
                light3.setImageBitmap(bmlight3);  light3.setVisibility(View.VISIBLE);
                red.setVisibility(View.INVISIBLE);  blue.setVisibility(View.INVISIBLE);  green.setVisibility(View.INVISIBLE);
                black.setVisibility(View.INVISIBLE);  white.setVisibility(View.INVISIBLE);
                insertText.setVisibility(View.INVISIBLE);
                applyBtn.setVisibility(View.INVISIBLE);
                star.setVisibility(View.INVISIBLE);
                heart.setVisibility(View.INVISIBLE);
                smile.setVisibility(View.INVISIBLE);
                cry.setVisibility(View.INVISIBLE);
                angry.setVisibility(View.INVISIBLE);

                mode = "brightness";

            }
        });
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                red.setVisibility(View.INVISIBLE);    blue.setVisibility(View.INVISIBLE);   green.setVisibility(View.INVISIBLE);
                black.setVisibility(View.INVISIBLE);  white.setVisibility(View.INVISIBLE);  light3.setVisibility(View.INVISIBLE);
                dark3.setVisibility(View.INVISIBLE); dark2.setVisibility(View.INVISIBLE); dark1.setVisibility(View.INVISIBLE);
                root.setVisibility(View.INVISIBLE);  light1.setVisibility(View.INVISIBLE); light2.setVisibility(View.INVISIBLE);
                drawLayout.setVisibility(View.INVISIBLE);
                insertText.setVisibility(View.VISIBLE);
                applyBtn.setVisibility(View.VISIBLE);
                star.setVisibility(View.INVISIBLE);
                heart.setVisibility(View.INVISIBLE);
                smile.setVisibility(View.INVISIBLE);
                cry.setVisibility(View.INVISIBLE);
                angry.setVisibility(View.INVISIBLE);

                mode = "editText";
            }
        });
        sticker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                red.setVisibility(View.INVISIBLE);    blue.setVisibility(View.INVISIBLE);   green.setVisibility(View.INVISIBLE);
                black.setVisibility(View.INVISIBLE);  white.setVisibility(View.INVISIBLE);
                light3.setVisibility(View.INVISIBLE);
                dark3.setVisibility(View.INVISIBLE); dark2.setVisibility(View.INVISIBLE); dark1.setVisibility(View.INVISIBLE);
                root.setVisibility(View.INVISIBLE);  light1.setVisibility(View.INVISIBLE); light2.setVisibility(View.INVISIBLE);
                drawLayout.setVisibility(View.INVISIBLE);
                insertText.setVisibility(View.INVISIBLE);
                applyBtn.setVisibility(View.INVISIBLE);
                star.setVisibility(View.VISIBLE);
                heart.setVisibility(View.VISIBLE);
                smile.setVisibility(View.VISIBLE);
                cry.setVisibility(View.VISIBLE);
                angry.setVisibility(View.VISIBLE);

                mode = "sticker";
            }
        });

        crop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        rotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        draw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                red.setVisibility(View.VISIBLE);    blue.setVisibility(View.VISIBLE);   green.setVisibility(View.VISIBLE);
                black.setVisibility(View.VISIBLE);  white.setVisibility(View.VISIBLE);  light3.setVisibility(View.INVISIBLE);
                dark3.setVisibility(View.INVISIBLE); dark2.setVisibility(View.INVISIBLE); dark1.setVisibility(View.INVISIBLE);
                root.setVisibility(View.INVISIBLE);  light1.setVisibility(View.INVISIBLE); light2.setVisibility(View.INVISIBLE);
                insertText.setVisibility(View.INVISIBLE);
                applyBtn.setVisibility(View.INVISIBLE);
                star.setVisibility(View.INVISIBLE);
                heart.setVisibility(View.INVISIBLE);
                smile.setVisibility(View.INVISIBLE);
                cry.setVisibility(View.INVISIBLE);
                angry.setVisibility(View.INVISIBLE);

                drawLayout.setBackgroundColor(getResources().getColor(R.color.gray)); drawLayout.setVisibility(View.VISIBLE);
                mode="draw";
            }
        });

        imageResult.setOnTouchListener(new View.OnTouchListener() {


            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(mode=="draw"){
                    int action = event.getAction();
                    int x = (int) event.getX();
                    int y = (int) event.getY();
                    switch (action) {
                        case MotionEvent.ACTION_DOWN:
                            prvX = x;
                            prvY = y;
                            drawOnProjectedBitMap((ImageView) v, bitmapMaster, prvX, prvY, x, y);
                            break;
                        case MotionEvent.ACTION_MOVE:
                            drawOnProjectedBitMap((ImageView) v, bitmapMaster, prvX, prvY, x, y);
                            prvX = x;
                            prvY = y;
                            break;
                        case MotionEvent.ACTION_UP:
                            drawOnProjectedBitMap((ImageView) v, bitmapMaster, prvX, prvY, x, y);
                            break;
                    }
                }

                return true;
            }
        });

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //imageResult.setImageBitmap(bitmapMaster);
                save();
            }
        });
        red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paintDraw.setColor(Color.RED);

            }
        });
        green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paintDraw.setColor(Color.GREEN);

            }
        });
        blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paintDraw.setColor(Color.BLUE);

            }
        });
        black.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paintDraw.setColor(Color.BLACK);

            }
        });
        white.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paintDraw.setColor(Color.WHITE);

            }
        });
        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Bitmap dest = Bitmap.createBitmap(bitmapMaster.getWidth(), bitmapMaster.getHeight(), Bitmap.Config.ARGB_8888);
                    String yourText = insertText.getText().toString();
                    Canvas cs = new Canvas(dest);
                    Paint tPaint = new Paint();
                    tPaint.setTextSize(100);
                    tPaint.setColor(Color.RED);
                    tPaint.setStyle(Paint.Style.FILL);
                    cs.drawBitmap(bitmapMaster, 0f, 0f, null);
                    float width = tPaint.measureText(yourText);
                    float x_coord = (bitmapMaster.getWidth() - width) / 2;
                    float height = (bitmapMaster.getHeight() ) / 2;

                cs.drawText(yourText, x_coord, height , tPaint); // 15f is to put space between top edge and the text, if you want to change it, you can

                    Bitmap.Config config;
                    if (dest.getConfig() != null) {
                        config = dest.getConfig();
                    } else {
                        config = Bitmap.Config.ARGB_8888;
                    }
                    bitmapMaster = Bitmap.createBitmap(
                            dest.getWidth(),
                            dest.getHeight(),
                            config);
                    canvasMaster = new Canvas(bitmapMaster);
                    canvasMaster.drawBitmap(dest, 0, 0, null);
                    imageResult.setImageBitmap(bitmapMaster);

            }
        });
        heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = null;
                try {
                    Resources res = getResources();
                    Bitmap bitmap1 = bitmapMaster;

                    bitmap = Bitmap.createBitmap(bitmap1.getWidth(), bitmap1.getHeight(), bitmap1.getConfig());
                    Canvas c = new Canvas(bitmap);



                    Bitmap bitmap2 = BitmapFactory.decodeResource(res, R.drawable.heart); //green
                    Drawable drawable1 = new BitmapDrawable(bitmap1);
                    Drawable drawable2 = new BitmapDrawable(bitmap2);


                    drawable1.setBounds(0, 0, bitmap1.getWidth(), bitmap1.getHeight());
                    drawable2.setBounds(bitmap1.getWidth()/2, bitmap1.getHeight()/2, bitmap1.getWidth()/2+bitmap2.getWidth(),bitmap1.getHeight()/2+bitmap2.getHeight() );
                    drawable1.draw(c);
                    drawable2.draw(c);
                    Bitmap.Config config;
                    if (bitmap.getConfig() != null) {
                        config = bitmap.getConfig();
                    } else {
                        config = Bitmap.Config.ARGB_8888;
                    }
                    bitmapMaster = Bitmap.createBitmap(
                            bitmap.getWidth(),
                            bitmap.getHeight(),
                            config);
                    canvasMaster = new Canvas(bitmapMaster);
                    canvasMaster.drawBitmap(bitmap, 0, 0, null);
                    imageResult.setImageBitmap(bitmapMaster);


                } catch (Exception e) {
                }
            }
        });
        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = null;
                try {
                    Resources res = getResources();
                    Bitmap bitmap1 = bitmapMaster;

                    bitmap = Bitmap.createBitmap(bitmap1.getWidth(), bitmap1.getHeight(), bitmap1.getConfig());
                    Canvas c = new Canvas(bitmap);



                    Bitmap bitmap2 = BitmapFactory.decodeResource(res, R.drawable.star); //green
                    Drawable drawable1 = new BitmapDrawable(bitmap1);
                    Drawable drawable2 = new BitmapDrawable(bitmap2);


                    drawable1.setBounds(0, 0, bitmap1.getWidth(), bitmap1.getHeight());
                    drawable2.setBounds(bitmap1.getWidth()/2, bitmap1.getHeight()/2, bitmap1.getWidth()/2+bitmap2.getWidth(),bitmap1.getHeight()/2+bitmap2.getHeight() );
                    drawable1.draw(c);
                    drawable2.draw(c);
                    Bitmap.Config config;
                    if (bitmap.getConfig() != null) {
                        config = bitmap.getConfig();
                    } else {
                        config = Bitmap.Config.ARGB_8888;
                    }
                    bitmapMaster = Bitmap.createBitmap(
                            bitmap.getWidth(),
                            bitmap.getHeight(),
                            config);
                    canvasMaster = new Canvas(bitmapMaster);
                    canvasMaster.drawBitmap(bitmap, 0, 0, null);
                    imageResult.setImageBitmap(bitmapMaster);


                } catch (Exception e) {
                }
            }
        });
        smile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = null;
                try {
                    Resources res = getResources();
                    Bitmap bitmap1 = bitmapMaster;

                    bitmap = Bitmap.createBitmap(bitmap1.getWidth(), bitmap1.getHeight(), bitmap1.getConfig());
                    Canvas c = new Canvas(bitmap);



                    Bitmap bitmap2 = BitmapFactory.decodeResource(res, R.drawable.smile); //green
                    Drawable drawable1 = new BitmapDrawable(bitmap1);
                    Drawable drawable2 = new BitmapDrawable(bitmap2);


                    drawable1.setBounds(0, 0, bitmap1.getWidth(), bitmap1.getHeight());
                    drawable2.setBounds(bitmap1.getWidth()/2, bitmap1.getHeight()/2, bitmap1.getWidth()/2+bitmap2.getWidth(),bitmap1.getHeight()/2+bitmap2.getHeight() );
                    drawable1.draw(c);
                    drawable2.draw(c);
                    Bitmap.Config config;
                    if (bitmap.getConfig() != null) {
                        config = bitmap.getConfig();
                    } else {
                        config = Bitmap.Config.ARGB_8888;
                    }
                    bitmapMaster = Bitmap.createBitmap(
                            bitmap.getWidth(),
                            bitmap.getHeight(),
                            config);
                    canvasMaster = new Canvas(bitmapMaster);
                    canvasMaster.drawBitmap(bitmap, 0, 0, null);
                    imageResult.setImageBitmap(bitmapMaster);


                } catch (Exception e) {
                }
            }
        });
        cry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = null;
                try {
                    Resources res = getResources();
                    Bitmap bitmap1 = bitmapMaster;

                    bitmap = Bitmap.createBitmap(bitmap1.getWidth(), bitmap1.getHeight(), Bitmap.Config.ARGB_8888);
                    Canvas c = new Canvas(bitmap);



                    Bitmap bitmap2 = BitmapFactory.decodeResource(res, R.drawable.cry); //green
                    Drawable drawable1 = new BitmapDrawable(bitmap1);
                    Drawable drawable2 = new BitmapDrawable(bitmap2);


                    drawable1.setBounds(0, 0, bitmap1.getWidth(), bitmap1.getHeight());
                    drawable2.setBounds(bitmap1.getWidth()/2, bitmap1.getHeight()/2, bitmap1.getWidth()/2+bitmap2.getWidth(),bitmap1.getHeight()/2+bitmap2.getHeight() );
                    drawable1.draw(c);
                    drawable2.draw(c);
                    Bitmap.Config config;
                    if (bitmap.getConfig() != null) {
                        config = bitmap.getConfig();
                    } else {
                        config = Bitmap.Config.ARGB_8888;
                    }
                    bitmapMaster = Bitmap.createBitmap(
                            bitmap.getWidth(),
                            bitmap.getHeight(),
                            config);
                    canvasMaster = new Canvas(bitmapMaster);
                    canvasMaster.drawBitmap(bitmap, 0, 0, null);
                    imageResult.setImageBitmap(bitmapMaster);


                } catch (Exception e) {
                }
            }
        });
        angry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = null;
                try {
                    Resources res = getResources();
                    Bitmap bitmap1 = bitmapMaster;

                    bitmap = Bitmap.createBitmap(bitmap1.getWidth(), bitmap1.getHeight(), Bitmap.Config.ARGB_8888);
                    Canvas c = new Canvas(bitmap);



                    Bitmap bitmap2 = BitmapFactory.decodeResource(res, R.drawable.angry); //green
                    Drawable drawable1 = new BitmapDrawable(bitmap1);
                    Drawable drawable2 = new BitmapDrawable(bitmap2);


                    drawable1.setBounds(0, 0, bitmap1.getWidth(), bitmap1.getHeight());
                    drawable2.setBounds(bitmap1.getWidth()/2, bitmap1.getHeight()/2, bitmap1.getWidth()/2+bitmap2.getWidth(),bitmap1.getHeight()/2+bitmap2.getHeight() );
                    drawable1.draw(c);
                    drawable2.draw(c);
                    Bitmap.Config config;
                    if (bitmap.getConfig() != null) {
                        config = bitmap.getConfig();
                    } else {
                        config = Bitmap.Config.ARGB_8888;
                    }
                    bitmapMaster = Bitmap.createBitmap(
                            bitmap.getWidth(),
                            bitmap.getHeight(),
                            config);
                    canvasMaster = new Canvas(bitmapMaster);
                    canvasMaster.drawBitmap(bitmap, 0, 0, null);
                    imageResult.setImageBitmap(bitmapMaster);


                } catch (Exception e) {
                }
            }
        });
        paintDraw = new Paint();
        paintDraw.setStyle(Paint.Style.FILL);
        paintDraw.setColor(Color.RED);
        paintDraw.setStrokeWidth(10);
    }

    // Ta asyncTask de luu anh vao trong vao bo nho
    private class MyAsyncTask extends AsyncTask<Void, Void, String> {

        private Context myContextRef;
        private String fileName;
        private Bitmap bitmap;
        private final ProgressDialog dialog;

        public MyAsyncTask(Context myContextRef, String fileName, Bitmap bitmap) {
            this.myContextRef = myContextRef;
            this.fileName = fileName;
            this.bitmap = bitmap;
            dialog = new ProgressDialog(Edit.this);
        }

        @Override
        protected String doInBackground(Void... voids) {
            ContextWrapper cw = new ContextWrapper(myContextRef);
            File directory = cw.getDir(dir, Context.MODE_PRIVATE);
            String newName = fileName;
            //Tao file moi dua tren duong dan co san
            File imageFile = new File(directory, newName);
            if (imageFile.exists()) {
                newName = newName.substring(0, newName.lastIndexOf('.')) + java.time.LocalDateTime.now() + ".jpg";
                imageFile = new File(directory, newName);
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
            return directory.getAbsolutePath() + '/' + newName;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //hien thi dialog yeu cau cho doi
            this.dialog.setMessage("Wait load image being done. It can be a slow job!!!");
            this.dialog.setCancelable(false);
            this.dialog.show();
        }

        protected void onPostExecute(String unused) {
            super.onPostExecute(unused);
            if (unused != null && unused != "") {

                String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
                if (this.dialog.isShowing()) this.dialog.dismiss();
                String imageFile = unused.substring(unused.lastIndexOf('/') + 1);
                String directory = unused.substring(0, unused.lastIndexOf('/'));
                dataBaseHelper.updateImage(listImg.get(selectedPosition).getId(), fileName, directory);
                //dataBaseHelper.updateImageInTimeline(Integer.toString(listImg.get(selectedPosition).getId()), fileName);
                finish();
            }

        }

    }

    private void save() {
        String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        String fileName = java.time.LocalDateTime.now() + ".jpg";
        MyAsyncTask myAsyncTask = new MyAsyncTask(getApplicationContext(), fileName, bitmapMaster);
        myAsyncTask.execute();

        //String path= saveToInternalStorage(bitmapMaster, fileName);
        //fileName=path.substring(path.lastIndexOf('/')+1);
        //String directory=path.substring(0,path.lastIndexOf('/'));
        //dataBaseHelper.updateImage(listImg.get(selectedPosition).getId(),fileName,directory);
        //dataBaseHelper.updateImageInTimeline(Integer.toString(listImg.get(selectedPosition).getId()), fileName);
    }

        private void drawOnProjectedBitMap(ImageView iv, Bitmap bm,
                                           float x0, float y0, float x, float y) {
            if (x < 0 || y < 0 || x > iv.getWidth() || y > iv.getHeight()) {
                //outside ImageView
                return;
            } else {

                float ratioWidth = (float) bm.getWidth() / (float) iv.getWidth();
                float ratioHeight = (float) bm.getHeight() / (float) iv.getHeight();

                canvasMaster.drawLine(
                        x0 * ratioWidth,
                        y0 * ratioHeight,
                        x * ratioWidth,
                        y * ratioHeight,
                        paintDraw);
                imageResult.invalidate();
            }
        }

        private Bitmap adjustedContrast(Bitmap src, double value) {
            int width = src.getWidth();
            int height = src.getHeight();
            Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
            int A, R, G, B;
            int pixel;
            double contrast = Math.pow((100 + value) / 100, 2);
            for (int x = 0; x < width; ++x) {
                for (int y = 0; y < height; ++y) {
                    pixel = src.getPixel(x, y);
                    A = Color.alpha(pixel);
                    R = Color.red(pixel);
                    R = (int) (((((R / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                    if (R < 0) {
                        R = 0;
                    } else if (R > 255) {
                        R = 255;
                    }
                    G = Color.green(pixel);
                    G = (int) (((((G / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                    if (G < 0) {
                        G = 0;
                    } else if (G > 255) {
                        G = 255;
                    }
                    B = Color.blue(pixel);
                    B = (int) (((((B / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                    if (B < 0) {
                        B = 0;
                    } else if (B > 255) {
                        B = 255;
                    }
                    bmOut.setPixel(x, y, Color.argb(A, R, G, B));
                }
            }
            return bmOut;
        }

        public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
            int width = bm.getWidth();
            int height = bm.getHeight();
            if (width > height) {
                newHeight = newWidth * height / width;
            } else {
                newWidth = newHeight * width / height;
            }
            float scaleWidth = ((float) newWidth) / width;
            float scaleHeight = ((float) newHeight) / height;
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleHeight);
            Bitmap resizedBitmap = Bitmap.createBitmap(
                    bm, 0, 0, width, height, matrix, false);
            return resizedBitmap;
        }
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }

}
