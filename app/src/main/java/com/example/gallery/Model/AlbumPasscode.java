package com.example.gallery.Model;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;

import com.example.gallery.R;
import com.keijumt.passwordview.ActionListener;
import com.keijumt.passwordview.PasswordView;

public class AlbumPasscode extends Activity {
    //Khoi tao
    PasswordView albumPasscode;
    TextView text_0,text_1,text_2,text_3,text_4,text_5,text_6,text_7,text_8,text_9,text_d;
    BroadcastReceiver broadcastReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album_passcode);
        //Nhan password
        Bundle bundle = getIntent().getExtras();
        String password = bundle.getString("password");
        long albumId = bundle.getLong("albumId");
        String albumName = bundle.getString("albumName");
        //find
        albumPasscode = (PasswordView) findViewById(R.id.albumPasscode);
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
        albumPasscode.setListener(new ActionListener() {
            @Override
            public void onCompleteInput(@NonNull String s) {
                if (s.equals(password)) {
                    //neu nhap pass dung thi chuyen qua xem anh
                    albumPasscode.correctAnimation();
                    Intent image_intent = new Intent(AlbumPasscode.this, ViewAlbumImage.class);
                    Bundle bundle = new Bundle();
                    bundle.putLong("albumId",albumId);
                    bundle.putString("albumName",albumName);
                    bundle.putBoolean("hasPasscode",true);
                    image_intent.putExtras(bundle);
                    //tao receiver de ket thuc activity passcode
                    broadcastReceiver = new BroadcastReceiver() {
                        @Override
                        public void onReceive(Context arg0, Intent intent) {
                            String action = intent.getAction();
                            if (action.equals("finish_activity")) {
                                finish();
                            }
                        }
                    };
                    registerReceiver(broadcastReceiver, new IntentFilter("finish_activity"));
                    startActivity(image_intent);
                }
                else {
                albumPasscode.incorrectAnimation();
                }
            }
            @Override
            public void onEndJudgeAnimation() {
                albumPasscode.reset();
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
    };
    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(broadcastReceiver);
    }
}
