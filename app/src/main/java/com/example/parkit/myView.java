package com.example.parkit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.graphics.Bitmap.createBitmap;

public class myView extends View {

    Paint rectPaint, pathPaint;
    Bitmap bitmap;
    Path path;
    boolean stat = false, movestat = false , peekerstat = false;

    int bm_offsetX, bm_offsetY;

    PathMeasure pathMeasure;
    float pathLength;

    float step;   //distance each step
    float distance;  //distance moved
    float curX, curY;


    float[] pos;
    float[] tan;

    int bmpposx, bmpposy, count = 0;

    Matrix matrix;

    List<Drawable> drawables = new ArrayList<>();
    List<Point> points = new ArrayList<>();
    Random rand;
    int score;
    viewInt inter;
    int i;

    List<Drawable> obstdrawables = new ArrayList<>();
    List<Point> obstpoints = new ArrayList<>();
    Vibrator vibrator;
    MediaPlayer mediaPlayer;
    Handler handler;


    public myView(Context context) {
        super(context);
        init(null);
    }

    public myView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public myView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(@Nullable AttributeSet attrs) {
        rectPaint = new Paint();
        rectPaint.setAntiAlias(true);
        rectPaint.setStyle(Paint.Style.STROKE);
        rectPaint.setStrokeWidth(15);
        rectPaint.setColor(Color.BLACK);
        pathPaint = new Paint();
        pathPaint.setAntiAlias(true);
        pathPaint.setStyle(Paint.Style.STROKE);
        pathPaint.setStrokeWidth(15);
        pathPaint.setColor(Color.RED);
        pathPaint.setMaskFilter(new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL));
        path = new Path();
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.car_obj);
        Matrix matrix1 = new Matrix();
        matrix1.postScale(0.25F, 0.2F);
        bitmap = createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix1, true);

        bm_offsetX = bitmap.getWidth() / 2;
        bm_offsetY = bitmap.getHeight() / 2;

        step = 10;
        distance = 0;
        curX = 0;
        curY = 0;


        tan = new float[2];
        pos = new float[2];

        matrix = new Matrix();
        rand = new Random();
        vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
        mediaPlayer = MediaPlayer.create(getContext() , R.raw.crash);
        handler = new Handler();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (count == 0) {
            bmpposx = getWidth() / 2 - bitmap.getWidth() / 2;
            bmpposy = getHeight() - 250 - bitmap.getHeight() / 2;
            i = 0;
            int x, y;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                while (i < 5) {
                    drawables.add(getResources().getDrawable(R.drawable.ic_remove_circle_black_24dp, null));
                    x = 80 + rand.nextInt(getWidth() - 160);
                    y = 580 + rand.nextInt(bmpposy - 660);
                    points.add(new Point(x, y));
                    drawables.get(drawables.size() - 1).setBounds(x - 70, y - 70, x + 70, y + 70);
                    if (i < 3) {
                        obstdrawables.add(getResources().getDrawable(R.drawable.ic_directions_bus_black_24dp, null));
                        x = 120 + rand.nextInt(getWidth() - 240);
                        y = 620 + rand.nextInt(bmpposy - 740);
                        obstpoints.add(new Point(x, y));
                        obstdrawables.get(obstdrawables.size() - 1).setBounds(x - 110, y - 110, x + 110, y + 110);
                    }
                    i++;
                }
            }
            count++;
        }
        canvas.drawColor(Color.WHITE);
        //canvas.drawLine(0, 500, getWidth() / 2 - 147, 500, rectPaint);
        //canvas.drawLine(getWidth()/2+147, 500, getWidth(), 500, rectPaint);
        canvas.drawLine(getWidth() / 2 - 153, 150, getWidth() / 2 + 153, 150, rectPaint);
        canvas.drawLine(getWidth() / 2 - 150, 150, getWidth() / 2 - 150, 500, rectPaint);
        canvas.drawLine(getWidth() / 2 + 150, 150, getWidth() / 2 + 150, 500, rectPaint);
        canvas.drawPath(path, pathPaint);

        if(peekerstat){
            for (Drawable x : obstdrawables) {
                x.draw(canvas);
            }
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    peekerstat = false;
                    postInvalidate();
                }
            } , 1000);
        }


        for (Drawable x : drawables) {
            x.draw(canvas);
        }


        if (!movestat) {
            if (score != -5) {
                canvas.drawBitmap(bitmap, bmpposx, bmpposy, null);
                if(score==-4) {
                    for (Drawable x : obstdrawables) {
                        x.draw(canvas);
                    }
                }
            } else {
                pathMeasure.getPosTan(distance, pos, tan);
                matrix.reset();
                float degrees = (float) (Math.atan2(tan[1], tan[0]) * 180.0 / Math.PI);
                matrix.postRotate(degrees + 90, bm_offsetX, bm_offsetY);
                curX = pos[0] - bm_offsetX;
                curY = pos[1] - bm_offsetY;
                matrix.postTranslate(curX, curY);
                canvas.drawBitmap(bitmap, matrix, null);
                for (Drawable x : obstdrawables) {
                    x.draw(canvas);
                }
            }
        }


        if (movestat) {
            for (Drawable x : obstdrawables) {
                x.draw(canvas);
            }
            if (distance < pathLength) {
                pathMeasure.getPosTan(distance, pos, tan);
                matrix.reset();
                float degrees = (float) (Math.atan2(tan[1], tan[0]) * 180.0 / Math.PI);
                matrix.postRotate(degrees + 90, bm_offsetX, bm_offsetY);
                curX = pos[0] - bm_offsetX;
                curY = pos[1] - bm_offsetY;
                matrix.postTranslate(curX, curY);
                i = 0;
                float hnj , pnj = 0;
                if(degrees>-100 && degrees<-80){
                    hnj = bm_offsetY;
                }
                else{
                    hnj  = bm_offsetX;
                }
                if(!(degrees>-150 && degrees < -30) && !(degrees>30 && degrees<150)){
                    pnj = bm_offsetY;
                }
                for (Point p : points) {
                    if ((pos[0] > p.x - 60 && pos[0] < p.x + 60 && pos[1] > p.y - 60 && pos[1] < p.y + 60) ||( pos[0] +bm_offsetX > p.x - 60 && pos[0]-bm_offsetX< p.x-60 && pos[1] > p.y - 60 && pos[1] < p.y + 60)
                            ||(pos[0] - bm_offsetX < p.x + 60 && pos[0]+bm_offsetX > p.x + 60 && pos[1] > p.y - 60 && pos[1] < p.y + 60)) {
                        points.remove(p);
                        vibrator.vibrate(100);
                        score++;
                        break;
                    }
                    i++;
                }
                if (i < drawables.size()) {
                    drawables.remove(i);
                }

                for (Point p : obstpoints) {
                    if ((pos[0] - pnj > p.x - 90 && pos[0] + pnj< p.x + 90 && pos[1] - hnj < p.y + 90 && pos[1] - hnj > p.y - 90) ||( pos[0] +bm_offsetX - 90> p.x - 90 && pos[0]-bm_offsetX + 90< p.x-90 && pos[1]< p.y + 90 && pos[1]> p.y - 90)
                     ||(pos[0] - bm_offsetX +90 < p.x + 90 && pos[0]+bm_offsetX -90> p.x + 90 && pos[1] < p.y + 90 && pos[1]> p.y - 90)) {
                        score = -5;
                        inter.setscore(score);
                        movestat = false;
                        vibrator.vibrate(700);
                        mediaPlayer.start();;
                        break;
                    }
                }

                canvas.drawBitmap(bitmap, matrix, null);
                distance += step;
            } else {
                distance = 0;
                movestat = false;
                inter.setscore(score);
                score = -4;
            }
            invalidate();
        }

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean val = super.onTouchEvent(event);

        float xx = event.getX();
        float yy = event.getY();
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN: {
                if (xx > bmpposx && xx < bmpposx + bitmap.getWidth() && yy > bmpposy && yy < bmpposy + bitmap.getHeight() && !movestat && bmpposy != 325 - bitmap.getHeight() / 2) {
                    path.moveTo(xx, yy);
                    stat = true;
                }

                return true;
            }
            case MotionEvent.ACTION_MOVE: {
                if (stat) {
                    path.lineTo(xx, yy);
                    if (yy + bitmap.getHeight() / 2 < 500 && (xx < getWidth() / 2 - 150 || xx > getWidth() / 2 + 150)) {
                        stat = false;
                        path.reset();
                        return false;
                    }
                }
                postInvalidate();
                return true;
            }
            case MotionEvent.ACTION_UP: {
                if (stat) {
                    if (xx > getWidth() / 2 - 150 && yy > 150 && xx < getWidth() / 2 + 150 && yy < 500) {
                        movestat = true;
                        matrix.postRotate(90);
                        bmpposx = getWidth() / 2 - bitmap.getWidth() / 2;
                        bmpposy = 325 - bitmap.getHeight() / 2;
                        path.lineTo(getWidth() / 2, 325);
                        pathMeasure = new PathMeasure(path, false);
                        pathLength = pathMeasure.getLength();
                        invalidate();
                    }
                    stat = false;
                    path.reset();
                    postInvalidate();
                }
                return val;
            }
        }

        return val;
    }

    public void redo() {
        if(!peekerstat) {
            stat = false;
            path.reset();
            movestat = false;
            count = 0;
            score = 0;
            distance = 0;
            curX = 0;
            curY = 0;
            drawables.clear();
            points.clear();
            obstdrawables.clear();
            obstpoints.clear();
            mediaPlayer.stop();
            try {
                mediaPlayer.prepare();
            } catch (IOException e) {
                Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
            }
            postInvalidate();
        }
    }

    public void setActivity(viewInt inter) {
        this.inter = inter;
    }
    public void peeker(){
        if(!peekerstat) {
            peekerstat = true;
            postInvalidate();
        }
    }

}
