package com.example.gallery.Model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.gallery.R;
import com.keijumt.passwordview.ActionListener;
import com.keijumt.passwordview.PasswordView;

public class AddAlbum extends Activity {
    private DBHandler handler;
    private Button addAlbumBtn;
    private EditText albumNameEdt;
    private Switch idSwitchPassword;
    PasswordView albumPasscode;
    TextView text_0,text_1,text_2,text_3,text_4,text_5,text_6,text_7,text_8,text_9,text_d;
    ConstraintLayout passcodeLayout;
    String albumPassword="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_album);
        Intent i = getIntent();
        addAlbumBtn = findViewById(R.id.idBtnAddAlbum);
        albumNameEdt = findViewById(R.id.idEdtAlbumName);
        idSwitchPassword = (Switch) findViewById(R.id.idSwitchPassword);
        handler = new DBHandler(AddAlbum.this);

        albumPasscode = (PasswordView) findViewById(R.id.albumPasscode);
        passcodeLayout = (ConstraintLayout) findViewById(R.id.passcodeLayout);
        //layout nhap pass
        passcodeLayout.setVisibility(View.INVISIBLE);
        text_0 = (TextView) findViewById(R.id.text_0);
        text_1 = (TextView) findViewById(R.id.text_1);
        text_2 = (TextView) findViewById(R.id.text_2);
        text_3 = (TextView) findViewById(R.id.text_3);
        text_4 = (TextView) findViewById(R.id.text_4);
        text_5 = (TextView) findViewById(R.id.text_5);
        text_6 = (TextView) findViewById(R.id.text_6);
        text_7 = (TextView) findViewById(R.id.text_7);
        text_8 = (TextView) findViewById(R.id.text_8);
        text_9 = (TextView) findViewById(R.id.text_9);
        text_d = (TextView) findViewById(R.id.text_d);
        //hien thi ban phim de nhap ten album
        albumNameEdt.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        idSwitchPassword.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        //chon co hoac khong co pass => co thi hien thi passcode
                        if (idSwitchPassword.isChecked()) {
                            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                            passcodeLayout.setVisibility(View.VISIBLE);
                            albumPasscode.setListener(new ActionListener() {
                                @Override
                                public void onCompleteInput(@NonNull String s) {
                                    albumPassword=s;
                                }
                                @Override
                                public void onEndJudgeAnimation() {
                                }
                            });
                        }
                        if (!idSwitchPassword.isChecked()) {
                            passcodeLayout.setVisibility(View.VISIBLE);
                            albumPasscode.reset();
                        }
                    }
                });
        addAlbumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String albumName = albumNameEdt.getText().toString();
                handler.addAlbum(albumName,albumPassword);
                finish();
            }
        });
        text_0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                albumPasscode.appendInputText(text_0.getText().toString());
            }
        });
        text_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                albumPasscode.appendInputText(text_1.getText().toString());
            }
        });
        text_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                albumPasscode.appendInputText(text_2.getText().toString());
            }
        });
        text_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                albumPasscode.appendInputText(text_3.getText().toString());
            }
        });
        text_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                albumPasscode.appendInputText(text_4.getText().toString());
            }
        });
        text_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                albumPasscode.appendInputText(text_5.getText().toString());
            }
        });
        text_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                albumPasscode.appendInputText(text_6.getText().toString());
            }
        });
        text_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                albumPasscode.appendInputText(text_7.getText().toString());
            }
        });
        text_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                albumPasscode.appendInputText(text_8.getText().toString());
            }
        });
        text_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                albumPasscode.appendInputText(text_9.getText().toString());
            }
        });
        text_d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                albumPasscode.removeInputText();
            }
        });
    }
}
