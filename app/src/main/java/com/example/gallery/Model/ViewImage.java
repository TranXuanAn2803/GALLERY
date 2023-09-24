package com.example.gallery.Model;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gallery.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class ViewImage extends Activity {
    ViewGroup scrollViewgroup;
    ImageView imageSelected;
    DBHandler dataBaseHelper;
    ArrayList<ImageModel> listImg;
    Integer selectedPosition=0;
    BottomNavigationView bottomNavigationView;
    //Edit tag
        TextView tvImageTag;
        Button btnEditImageTag;
    //
    long albumId;
    int prvX=0, prvY=0;
    int color= Color.TRANSPARENT;
    ArrayList<View> listSingleFrame= new ArrayList<View>();
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);
        //Nhan album id
        Bundle bundle = getIntent().getExtras();
        albumId = bundle.getLong("albumId");
        selectedPosition = bundle.getInt("position");
        dataBaseHelper= new DBHandler(this);
        //getId Image Tag
        tvImageTag = (TextView) findViewById(R.id.tv_imageTag);
        btnEditImageTag = (Button) findViewById(R.id.btn_editImageTag);
        //
        // get Image Tag Inf
        btnEditImageTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openEditImageTagDialog(Gravity.CENTER);
            }
        });
        //lay ds anh cua album do tu db
        listImg= dataBaseHelper.readAlbumImage(albumId);
        imageSelected = (ImageView) findViewById(R.id.imageSelected);
        scrollViewgroup = (ViewGroup) findViewById(R.id.viewgroup);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        showLargeImage(selectedPosition);
        for (int i = 0; i < listImg.size(); i++) {
            final View singleFrame = getLayoutInflater().inflate(R.layout.frame_scroll_horizontal, null);
            singleFrame.setId(i);
            listSingleFrame.add(singleFrame);
            if(i== selectedPosition)
            {
                Drawable background = singleFrame.getBackground();
                if (background instanceof ColorDrawable)

                    color = ((ColorDrawable) background).getColor();

                singleFrame.setBackgroundColor(getResources().getColor(R.color.gray));
            }
            ImageView icon = (ImageView) singleFrame.findViewById(R.id.icon);
            icon.setImageBitmap(listImg.get(i).getImage());
            scrollViewgroup.addView(singleFrame);
            singleFrame.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listSingleFrame.get(selectedPosition).setBackgroundColor(color);
                    Drawable background = singleFrame.getBackground();
                    if (background instanceof ColorDrawable)
                        color = ((ColorDrawable) background).getColor();
                    singleFrame.setBackgroundColor(getResources().getColor(R.color.gray));
                    selectedPosition=singleFrame.getId();
                    showLargeImage(selectedPosition);
                }
            });
            imageSelected.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    listSingleFrame.get(selectedPosition).setBackgroundColor(color);

                    int action = event.getAction();
                    int x= (int) event.getX();
                    int y = (int) event.getY();
                    switch (action) {
                        case  MotionEvent.ACTION_DOWN:
                            prvX=(int)event.getX();
                            prvY=(int)event.getY();

                        case MotionEvent.ACTION_MOVE:
                            x = (int) event.getX();
                            y = (int) event.getY();

                            break;
                        case MotionEvent.ACTION_UP:
                            listSingleFrame.get(selectedPosition).setBackgroundColor(color);

                            if (x-prvX>50 &&selectedPosition>0)
                            {
                                selectedPosition=selectedPosition-1;
                            }
                            else if(x-prvX<-50 &&selectedPosition<listImg.size()-1)
                            {
                                selectedPosition=selectedPosition+1;

                            }
                            Drawable background = listSingleFrame.get(selectedPosition).getBackground();
                            if (background instanceof ColorDrawable)
                                color = ((ColorDrawable) background).getColor();
                            listSingleFrame.get(selectedPosition).setBackgroundColor(getResources().getColor(R.color.gray));

                            showLargeImage(selectedPosition);
                            break;

                    }
                    return true;
                }

            });

        }
    }
    private void openEditImageTagDialog(int gravity ) {
        final Dialog dialog = new Dialog(ViewImage.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.edit_image_tag_dialog);

        Window window = dialog.getWindow();

        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);
        if (Gravity.CENTER == gravity) {
            dialog.setCancelable(true);
        } else {
            dialog.setCancelable(false);
        }
        EditText editTag  =  (EditText) dialog.findViewById(R.id.edt_tag);
        Button btnExit = (Button) dialog.findViewById(R.id.btn_exitImageTagDialog);
        Button btnSave = (Button) dialog.findViewById(R.id.btn_saveImageTagDialog);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editTag.getText().toString().compareTo("") == 0) {
                    editTag.setHint("Please Enter Image Tag!");
                } else {
                    tvImageTag.setText(editTag.getText());
                    dialog.dismiss();
                }
            }
        });
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        listImg= dataBaseHelper.readAlbumImage(albumId);
        showLargeImage(selectedPosition);
        scrollViewgroup.removeAllViews();
        for (int i = 0; i < listImg.size(); i++) {
            final View singleFrame = getLayoutInflater().inflate(R.layout.frame_scroll_horizontal, null);
            singleFrame.setId(i);

            ImageView icon = (ImageView) singleFrame.findViewById(R.id.icon);
            icon.setImageBitmap(listImg.get(i).getImage());
            scrollViewgroup.addView(singleFrame);
            singleFrame.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedPosition=singleFrame.getId();
                    showLargeImage(selectedPosition);
                }
            });

        }

    }

    protected void showLargeImage(int frameId) {
        imageSelected.setImageBitmap(listImg.get(frameId).getImage());
    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {


            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.btnfavorite: {
                    Intent i = new Intent(ViewImage.this, FavoriteAlbum.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("position",selectedPosition);
                    bundle.putLong("albumId",albumId);

                    i.putExtras(bundle);
                    startActivity(i);
                    listImg= dataBaseHelper.readAlbumImage(2);

                    return true;
                }
                case R.id.btnedit:
                {
                    Intent i = new Intent(ViewImage.this, Edit.class);
                    /*ByteArrayOutputStream bs = new ByteArrayOutputStream();
                    bitmapMaster.compress(Bitmap.CompressFormat.PNG, 50, bs);
                    i.putExtra("byteArray", bs.toByteArray());*/
                    Bundle bundle = new Bundle();
                    bundle.putInt("position",selectedPosition);
                    bundle.putLong("albumId",albumId);
                    i.putExtras(bundle);
                    startActivity(i);
                    listImg= dataBaseHelper.readAlbumImage(albumId);
                    return true;
                }
                case R.id.btndelete:
                    dataBaseHelper.deleteImage(listImg.get(selectedPosition).getId());
                    listImg.remove(selectedPosition);
                    //notifyDataSetChanged();
                    return true;
                case R.id.btninfor:
                    return true;
            }
            return false;
        }
    };

}