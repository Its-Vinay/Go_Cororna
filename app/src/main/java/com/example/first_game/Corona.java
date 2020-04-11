package com.example.first_game;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import static com.example.first_game.GameView.screenRatiox;
import static com.example.first_game.GameView.screenRatioy;

public class Corona {
    public static int speed = 20;
    public boolean wasshoot= true;
    int x=0,y,width,height;
    Bitmap corona1,corona2;
    Corona(Resources res){
        corona1 = BitmapFactory.decodeResource(res,R.drawable.cc);
        width = corona1.getWidth();
        height = corona1.getHeight();
        width /= 14.5;
        height /= 14.5;
        width *= screenRatiox;
        height *= screenRatioy;
        corona1 = Bitmap.createScaledBitmap(corona1,width,height,false);

    }
    Rect getCollision(){
        return new Rect(x,y,x+width,y+height);
    }
}
