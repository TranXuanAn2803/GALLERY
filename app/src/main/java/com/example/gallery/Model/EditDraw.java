package com.example.gallery.Model;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.gallery.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EditDraw extends Activity {
    ImageView imageResult;
    final int RQS_IMAGE1 = 1;
    ImageButton red, green, blue, black, white;
    Button savebtn;
    Bitmap bitmapMaster;
    Canvas canvasMaster;
    int prvX, prvY;
    Paint paintDraw;
    ArrayList<ImageModel> listImg;
    DBHandler dataBaseHelper;
    private  static final String dir="MyAppDir";

    private  int selectedPosition=0;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_draw);
        Bundle extras = getIntent().getExtras();
        dataBaseHelper= new DBHandler(this);
        listImg= dataBaseHelper.readTimeline();
        if (extras != null) {
            String value = extras.getString("imageSelected");
            selectedPosition=Integer.parseInt(value);
        }
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

        red = (ImageButton) findViewById(R.id.colorRed);
        green = (ImageButton) findViewById(R.id.colorGreen);
        blue = (ImageButton) findViewById(R.id.colorBlue);
        black = (ImageButton) findViewById(R.id.colorBack);
        white = (ImageButton) findViewById(R.id.colorWhite);
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

        paintDraw = new Paint();
        paintDraw.setStyle(Paint.Style.FILL);
        paintDraw.setColor(Color.RED);
        paintDraw.setStrokeWidth(10);
        imageResult.setOnTouchListener(new View.OnTouchListener() {


            @Override
            public boolean onTouch(View v, MotionEvent event) {

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
    }
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
    private void drawOnProjectedBitMap(ImageView iv, Bitmap bm,
                                       float x0, float y0, float x, float y){
        if(x<0 || y<0 || x > iv.getWidth() || y > iv.getHeight()){
            //outside ImageView
            return;
        }else{

            float ratioWidth = 1;
            float ratioHeight = 1;

            canvasMaster.drawLine(
                    x0 * ratioWidth,
                    y0 * ratioHeight,
                    x * ratioWidth,
                    y * ratioHeight,
                    paintDraw);
            imageResult.invalidate();
        }
    }
    private void save()
    {
        String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        String fileName=java.time.LocalDateTime.now()+".jpg";
        String path= saveToInternalStorage(bitmapMaster, fileName);
        fileName=path.substring(path.lastIndexOf('/')+1);
        String directory=path.substring(0,path.lastIndexOf('/'));

        dataBaseHelper.updateImageInTimeline(Integer.toString(listImg.get(selectedPosition).getId()), fileName);
        finish();
    }


}
