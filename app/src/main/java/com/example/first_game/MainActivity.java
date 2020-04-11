package com.example.first_game;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView tv;
    private ImageView img;
    private boolean isMute;
    private SharedPreferences prefs;
    private MediaPlayer mysong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        setContentView(R.layout.activity_main);
        mysong = MediaPlayer.create(MainActivity.this,R.raw.gocorona);
        mysong.start();
        tv = findViewById(R.id.highscore);
        img = findViewById(R.id.volume);
        prefs = getSharedPreferences("game",MODE_PRIVATE);
        tv.setText("Highscore : "+prefs.getInt("Highscore",0));

        findViewById(R.id.play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,GameActivity.class));
            }
        });
        isMute = prefs.getBoolean("isMute",false);
        if(isMute){
            img.setImageResource(R.drawable.ic_volume_off_black_24dp);
        }else
            img.setImageResource(R.drawable.ic_volume_up_black_24dp);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMute = !isMute;
                if(isMute){
                    img.setImageResource(R.drawable.ic_volume_off_black_24dp);
                }else
                    img.setImageResource(R.drawable.ic_volume_up_black_24dp);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("isMute",isMute);
                editor.apply();

            }
        });

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        View view = getWindow().getDecorView();
        int uiOption = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        view.setSystemUiVisibility(uiOption);
        return super.onTouchEvent(event);
    }


    @Override
    protected void onPause() {
        mysong.release();
        super.onPause();
        finish();
    }
}
