package com.example.mygame;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

import mygame.GameFactors.Blocks;
import mygame.GameFactors.Speed;

public class GameView extends View {
    Runnable runnable;
    SharedPreferences sPref;

    Context context;
    float ball_x, ball_y;
    Speed speed = new Speed(30, 30);
    Handler handler;
    final long update_time = 30;

    Paint text_paint = new Paint();
    Paint health_paint = new Paint();
    Paint blocks_paint = new Paint();
    float board_x, board_y;
    float old_x, old_board_x;
    int points = 0;
    int life = 3;
    Bitmap ball, board;
    int dWidth, dHeight;
    int ballWidth, ballHeight;
    Random random;
    Blocks[] blocks = new Blocks[24];
    int kolvo_of_blocks = 0;
    int brocken_blocks = 0;
    boolean gameOver = false;

    public GameView(Context context) {
        super(context);
        this.context = context;
        ball = BitmapFactory.decodeResource(getResources(), R.drawable.ball);
        board = BitmapFactory.decodeResource(getResources(), R.drawable.board);
        handler = new Handler();

        health_paint.setColor(Color.RED);
        blocks_paint.setColor(Color.YELLOW);
        runnable = new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        };
        text_paint.setTextSize(100);
        text_paint.setTextAlign(Paint.Align.LEFT);
        text_paint.setColor(Color.BLUE);



        Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        dWidth = size.x;
        dHeight = size.y;

        ball_y = dHeight / 3;
        board_y = dHeight * 4/ 5;
        board_x = dWidth / 2 - board.getWidth() / 2;
        ballWidth = ball.getWidth();
        ballHeight = ball.getHeight();


        random = new Random();
        ball_x = random.nextInt(dWidth - 50);
        createBlocks();
    }

    private void createBlocks() {
        int BlockWidth = dWidth / 8;
        int BlockHeight = dHeight / 16;
        for (int column = 0; column < 8; column++) {
            for (int row = 0; row < 3; row++) {
                blocks[kolvo_of_blocks] = new Blocks(row, column, BlockWidth, BlockHeight);
                kolvo_of_blocks++;

            }
        }


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.BLACK);
        ball_x += speed.getX();
        ball_y += speed.getY();
        if ((ball_x >= dWidth - ball.getWidth()) || ball_x <= 0) {
            speed.setX(speed.getX() * -1);
        }
        if (ball_y <= 0) {
            speed.setY(speed.getY() * -1);
        }
        if (ball_y > board_y + board.getHeight()) {
            ball_x = 1 + random.nextInt(dWidth - ball.getWidth() - 1);
            ball_y = dHeight / 3;
            speed.setX(xVelocity());
            speed.setY(32);
            life--;
            if (life == 0) {
                gameOver = true;
                launchGameOver();
            }
        }
            if (((ball_x + ball.getWidth()) >= board_x)
                    && (ball_x <= board_x + board.getWidth())
                    && (ball_y + ball.getHeight() >= board_y) && (ball_y + ball.getHeight() <= board_y + board.getHeight())) {
                speed.setX(speed.getX() + 1);
                speed.setY((speed.getY() + 1) * -1);
            }
            canvas.drawBitmap(ball, ball_x, ball_y, null);
            canvas.drawBitmap(board, board_x, board_y, null);
            for (int i = 0; i < kolvo_of_blocks; i++) {
                if (blocks[i].getVisiblity()) {
                    canvas.drawRect(blocks[i].column * blocks[i].wigth + 1,
                            blocks[i].row * blocks[i].height + 1,
                            blocks[i].column * blocks[i].wigth + blocks[i].wigth - 1,
                            blocks[i].row * blocks[i].height + blocks[i].height - 1, blocks_paint);
                }
            }
            canvas.drawText("" + points, 20, 120, text_paint);
            canvas.drawRect(dWidth - 200, 30, dWidth - 200 + 60 * life, 80, health_paint);

            for (int i = 0; i < kolvo_of_blocks; i++) {
                if (blocks[i].getVisiblity()) {
                    if (ball_x + ballWidth >= blocks[i].column * blocks[i].wigth
                            && ball_x <= blocks[i].column * blocks[i].wigth + blocks[i].wigth
                            && ball_y <= blocks[i].row * blocks[i].height + blocks[i].height
                            && ball_y >= blocks[i].row * blocks[i].height) {
                        speed.setY((speed.getY() + 1) * -1);
                        blocks[i].setInvisible();
                        points+=10;
                        brocken_blocks+=1;
                        if (brocken_blocks == kolvo_of_blocks){
                            launchGameOver();
                        }
                    }
                }
            }
            if(brocken_blocks == kolvo_of_blocks){
                gameOver = true;
            }
            if(!gameOver){
                handler.postDelayed(runnable, update_time);
            }
        }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touch_x = event.getX();
        float touch_y = event.getY();
        if(touch_y >= board_y){
            int action = event.getAction();
            if(action == MotionEvent.ACTION_DOWN){
                old_x = event.getX();
                old_board_x = board_x;
            }
            if(action == MotionEvent.ACTION_MOVE){
                float shift = old_x - touch_x;
                float new_board_x = old_board_x - shift;
                if(new_board_x <= 0){
                    board_x = 0;
                }else if (new_board_x >= dWidth - board.getWidth()){
                    board_x = dWidth - board.getWidth();
                }else{
                    board_x = new_board_x;
                }
            }
        }
        return true;

    }

    private void launchGameOver() {
        handler.removeCallbacksAndMessages(null);
        Intent intent = new Intent(context, GameOver.class);
        intent.putExtra("points", points);
        context.startActivity(intent);
        ((Activity) context).finish();

    }

    private int xVelocity() {
        int[] values = {-35, -30, -25, 25, 30, 35};
        int index = random.nextInt(6);
        return values[index];
    }

    /*private void saveResult(){
        sPref = context.getSharedPreferences("com.example.app", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(SAVED_TEXT)


    }*/



}
