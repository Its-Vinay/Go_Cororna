package com.example.first_game;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import static com.example.first_game.GameView.screenRatiox;
import static com.example.first_game.GameView.screenRatioy;

public class Flight {
    public int isGoingUp = 3;
    public int isshoot=0;
    int wingcounter=0,shootcounter=1;
    int x,y,width,height;
    Bitmap flight1,flight2,shoot1,shoot2,shoot3,shoot4,shoot5,dead;
    private GameView gameView;
    Flight(GameView gameView,int screeny, Resources res){
        this.gameView=gameView;
        flight1 = BitmapFactory.decodeResource(res,R.drawable.fly1);
        flight2 = BitmapFactory.decodeResource(res,R.drawable.fly2);
        width = flight1.getWidth();
        height = flight1.getHeight();
        width /= 4;
        height /=4;
      //  width *= (int)screenRatiox;
     //   height *= (int) screenRatioy;

flight1 = Bitmap.createScaledBitmap(flight1,width,height,false);
        flight2 = Bitmap.createScaledBitmap(flight2,width,height,false);
        y=(screeny/2)-(flight1.getHeight()/2);
        x = (int)(50 * screenRatiox);
        shoot1=BitmapFactory.decodeResource(res,R.drawable.shoot1);
        shoot2=BitmapFactory.decodeResource(res,R.drawable.shoot2);
        shoot3=BitmapFactory.decodeResource(res,R.drawable.shoot3);
        shoot4=BitmapFactory.decodeResource(res,R.drawable.shoot4);
        shoot5=BitmapFactory.decodeResource(res,R.drawable.shoot5);
        shoot1=Bitmap.createScaledBitmap(shoot1,width,height,false);
        shoot2=Bitmap.createScaledBitmap(shoot2,width,height,false);
        shoot3=Bitmap.createScaledBitmap(shoot3,width,height,false);
        shoot4=Bitmap.createScaledBitmap(shoot4,width,height,false);
        shoot5=Bitmap.createScaledBitmap(shoot5,width,height,false);

        dead = BitmapFactory.decodeResource(res,R.drawable.dead);
        dead = Bitmap.createScaledBitmap(dead,width,height,false);
    }
    Bitmap getFlight(){
        if(isshoot!=0){
            if(shootcounter==1){
                shootcounter++;
                return shoot1;
            }
            if(shootcounter==2){
                shootcounter++;
                return shoot2;
            }
            if(shootcounter==3){
                shootcounter++;
                return shoot3;
            }
            if(shootcounter==4){
                shootcounter++;
                return shoot4;
            }
           shootcounter=1;

            gameView.newBullet();
            return shoot5;
        }
        if(wingcounter == 0){
            wingcounter++;
            return flight1;
        }
            wingcounter--;
            return flight2;

    }
    Rect getCollision() {
        return new Rect(x, y, x + width, y + height);
    }
}
