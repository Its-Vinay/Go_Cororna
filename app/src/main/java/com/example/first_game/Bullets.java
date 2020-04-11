package com.example.first_game;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import static com.example.first_game.GameView.screenRatiox;
import static com.example.first_game.GameView.screenRatioy;

public class Bullets {
    int x, y,width,height;
    Bitmap bullet;

    Bullets(Resources res) {
        bullet = BitmapFactory.decodeResource(res, R.drawable.bullet);
        width = bullet.getWidth();
        height = bullet.getHeight();
        width /= 4;
        height /= 4;
        width *= (int) screenRatiox;
        height *= (int) screenRatioy;

        bullet = Bitmap.createScaledBitmap(bullet, width, height, false);
    }

     Rect getCollision() {
        return new Rect(x, y, x + width, y + height);
    }
}
