package com.example.first_game;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.R.color.white;

public class GameView extends SurfaceView implements Runnable {

    private boolean isplaying;
    private int screenx,screeny,g=0,score = 0;
    private Thread thread;
    private Corona[] corona;
    private Random random;
    public static float screenRatiox,screenRatioy;
    private Flight flight;
    private SharedPreferences prefs;
    private Paint paint;
    private List<Bullets> bullet;
    private GameActivity activity;
    private SoundPool soundPool;
    private int sound;
    private Background background1,background2;
    private boolean isGameOver = false;


    public GameView(GameActivity activity,int screenx,int screeny) {
        super(activity);
        this.activity = activity;
        prefs = activity.getSharedPreferences("game",Context.MODE_PRIVATE);
        paint = new Paint();
        paint.setTextSize(128);
        paint.setColor(getResources().getColor(white));
        this.screenx = screenx;
        this.screeny = screeny;
        screenRatiox = 2340f/screenx;
        screenRatioy = 1080f/screeny;
        background1 = new Background(screenx,screeny,getResources());
        background2 = new Background(screenx,screeny,getResources());
        background2.x=screenx;
        flight = new Flight(this,screeny,getResources());
        bullet=new ArrayList<>();
        corona = new Corona[6];
            for(int i=0;i<6;i++)
                corona[i] = new Corona(getResources());
        random = new Random();
        AudioAttributes audioAttributes= new AudioAttributes.Builder()
                                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                        .setUsage(AudioAttributes.USAGE_GAME)
                                        .build();
        soundPool = new SoundPool.Builder()
                .setAudioAttributes(audioAttributes)
                .build();
        sound = soundPool.load(activity,R.raw.shoot,1);

    }

    @Override
    public void run() {
        while (isplaying) {
            update();
            draw();
            sleep();

        }
    }
    public void resume(){
        isplaying = true;
        thread = new Thread(this);
        thread.start();

    }
    public void pause(){
        try {
            isplaying=false;
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    private void update() {
        background1.x -= 8;
        background2.x -= 8;
        if (background1.x + background1.background.getWidth() < 0)
            background1.x = screenx;
        if (background2.x + background2.background.getWidth() < 0)
            background2.x = screenx;
        if (flight.isGoingUp==1) {
            flight.y -= 20 * screenRatioy;
        } if(flight.isGoingUp==0)
            flight.y += 20 * screenRatioy;

        if (flight.y < 0)
            flight.y = 0;
        if (flight.y > screeny - flight.height)
            flight.y = screeny - flight.height;
        List<Bullets> trash = new ArrayList<>();

        for (Bullets bullet : bullet) {
            if (bullet.x > screenx)
                trash.add(bullet);
            bullet.x += 25 * screenRatiox;

            for (Corona corona : corona) {
                if (Rect.intersects(bullet.getCollision(), corona.getCollision())) {
                    corona.x = -500;
                    bullet.x = screenx + 500;
                    score++;
                    corona.wasshoot = true;
                }
            }
        }
        for (Bullets bullett : trash) {
            bullet.remove(bullett);
        }


        for(Corona corona:corona){

            corona.x -= Corona.speed;
            if(corona.x + corona.width < 0){
                if(!corona.wasshoot) {
                        isGameOver = true;
                    return;
                }

                int bound = (int) (30*screenRatiox);
                Corona.speed = random.nextInt(bound);
                if(Corona.speed < (10*screenRatiox))
                    Corona.speed = (int) (10*screenRatiox);
                corona.x = screenx;
                corona.y = random.nextInt(screeny-corona.height);
                corona.wasshoot = false;
            }
            if(Rect.intersects(flight.getCollision(),corona.getCollision())){
                isGameOver = true;
            }
        }
    }
    private void draw(){
        if(getHolder().getSurface().isValid()){
            Canvas canvas = getHolder().lockCanvas();

            canvas.drawBitmap(background1.background,background1.x,background1.y,paint);
            canvas.drawBitmap(background2.background,background2.x,background2.y,paint);
            for(Corona corona:corona)
                canvas.drawBitmap(corona.corona1, corona.x, corona.y, paint);
           // for(Corona corona:corona)
            //    canvas.drawBitmap(corona.corona2, corona.x, corona.y, paint);

            if(isGameOver){
                isplaying = false;
                canvas.drawBitmap(flight.dead,flight.x,flight.y,paint);
                getHolder().unlockCanvasAndPost(canvas);

                saveIfHighScore();
                waitBeforeExiting();
                return;
            }
            canvas.drawBitmap(flight.getFlight(),flight.x,flight.y,paint);
            canvas.drawText(score+"",screenx/2,164,paint);
            for(Bullets bullets:bullet){
                canvas.drawBitmap(bullets.bullet,bullets.x,bullets.y,paint);
            }


            getHolder().unlockCanvasAndPost(canvas);
        }

    }

    private void waitBeforeExiting() {
        try {
            thread.sleep(2000);
            activity.startActivity(new Intent(activity,MainActivity.class));
            activity.finish();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void saveIfHighScore() {
            if(prefs.getInt("Highscore",0) < score){
                SharedPreferences.Editor editor =  prefs.edit();
                editor.putInt("Highscore",score);
                editor.apply();
            }
    }

    private void sleep(){
        try {
            thread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(event.getX() < screenx/2)
                    flight.isGoingUp = 1;
                flight.isshoot++;

                break;
            case MotionEvent.ACTION_UP:
                    flight.isGoingUp = 0;

                    break;


        }

        return true;
    }

    public void newBullet() {
        if(!prefs.getBoolean("isMute",false))
            soundPool.play(sound,1,1,0,0,1);
        Bullets bullets = new Bullets(getResources());
        bullets.x = flight.x + flight.width;
        bullets.y = flight.y + (flight.height/2);
        bullet.add(bullets);
    }
}
