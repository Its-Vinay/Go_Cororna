package com.example.first_game;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.first_game.R;

public class Background {
    int x=0,y=0;
    Bitmap background;
    Background(int screenx, int screeny, Resources res){
        background = BitmapFactory.decodeResource(res, R.drawable.burakov200300693);
        background = Bitmap.createScaledBitmap(background,screenx,screeny,false);
    }
}
