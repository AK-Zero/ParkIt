package com.example.parkit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.util.Random;

import static android.graphics.Bitmap.createBitmap;

public class threecarview extends View {

    Paint rectPaint, pathPaintgreen, pathPaintred, pathPaintblue;
    Bitmap bitmapred, bitmapgreen, bitmapblue;
    Path pathred, pathgreen, pathblue;
    boolean statgreen = false, statred = false, statblue = false, movestatgreen = false, movestatred = false, movestatblue = false, extragreen = false, extrared = false, extrablue = false;

    int bm_offsetX, bm_offsetY;

    PathMeasure pathMeasuregreen, pathMeasurereed, pathMeasureblue;
    float pathLengthgreen, pathLengthred, pathLengthblue;

    float step;   //distance each step
    float distancegreen, distancered, distanceblue;  //distance moved
    float curXgreen, curYgreen, curXred, curYred, curXblue, curYblue;


    float[] posgreen;
    float[] tangreen;

    float[] posred;
    float[] tanred;

    float[] posblue;
    float[] tanblue;

    int count = 0, bmpposx1, bmpposy1, bmpposx2, bmpposy2, bmpposx3, bmpposy3;

    Matrix matrixgreen, matrixred, matrixblue;

    Vibrator vibrator;
    MediaPlayer mediaPlayer;


    float hnjgreen, pnjgreen = 0, hnjred, pnjred = 0, hnjblue, pnjblue = 0;


    public threecarview(Context context) {
        super(context);
        init(null);
    }

    public threecarview(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public threecarview(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(@Nullable AttributeSet attrs) {
        rectPaint = new Paint();
        rectPaint.setAntiAlias(true);
        rectPaint.setStyle(Paint.Style.STROKE);
        rectPaint.setStrokeWidth(15);
        rectPaint.setColor(Color.BLACK);
        pathPaintgreen = new Paint();
        pathPaintgreen.setAntiAlias(true);
        pathPaintgreen.setStyle(Paint.Style.STROKE);
        pathPaintgreen.setStrokeWidth(15);
        pathPaintgreen.setColor(Color.GREEN);
        pathPaintgreen.setMaskFilter(new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL));
        pathPaintred = new Paint();
        pathPaintred.setAntiAlias(true);
        pathPaintred.setStyle(Paint.Style.STROKE);
        pathPaintred.setStrokeWidth(15);
        pathPaintred.setColor(Color.RED);
        pathPaintred.setMaskFilter(new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL));
        pathPaintblue = new Paint();
        pathPaintblue.setAntiAlias(true);
        pathPaintblue.setStyle(Paint.Style.STROKE);
        pathPaintblue.setStrokeWidth(15);
        pathPaintblue.setColor(Color.BLUE);
        pathPaintblue.setMaskFilter(new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL));
        pathgreen = new Path();
        pathred = new Path();
        pathblue = new Path();
        bitmapred = BitmapFactory.decodeResource(getResources(), R.drawable.car_obj);
        Matrix matrix1 = new Matrix();
        matrix1.postScale(0.25F, 0.2F);
        bitmapred = createBitmap(bitmapred, 0, 0, bitmapred.getWidth(), bitmapred.getHeight(), matrix1, true);

        bitmapgreen = BitmapFactory.decodeResource(getResources(), R.drawable.green);
        bitmapgreen = createBitmap(bitmapgreen, 0, 0, bitmapgreen.getWidth(), bitmapgreen.getHeight(), matrix1, true);

        bitmapblue = BitmapFactory.decodeResource(getResources(), R.drawable.blue);
        bitmapblue = createBitmap(bitmapblue, 0, 0, bitmapblue.getWidth(), bitmapblue.getHeight(), matrix1, true);


        bm_offsetX = bitmapred.getWidth() / 2;
        bm_offsetY = bitmapred.getHeight() / 2;


        step = 10;

        distancegreen = 0;
        distancered = 0;
        distanceblue = 0;

        curXgreen = 0;
        curYgreen = 0;
        curXred = 0;
        curYred = 0;
        curXblue = 0;
        curYblue = 0;

        tangreen = new float[2];
        posgreen = new float[2];

        tanred = new float[2];
        posred = new float[2];

        tanblue = new float[2];
        posblue = new float[2];

        matrixgreen = new Matrix();
        matrixred = new Matrix();
        matrixblue = new Matrix();

        vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
        mediaPlayer = MediaPlayer.create(getContext(), R.raw.crash);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (count == 0) {
            bmpposx1 = (int) ((float) (getWidth()) / 3.5) - 160 - bitmapgreen.getWidth() / 2;
            bmpposy1 = getHeight() - 250 - bitmapgreen.getHeight() / 2;

            bmpposx2 = getWidth() / 2 - bitmapred.getWidth() / 2;
            bmpposy2 = getHeight() - 250 - bitmapred.getHeight() / 2;

            bmpposx3 = (int) (2.5 * (float) (getWidth()) / 3.5) + 160 - bitmapblue.getWidth() / 2;
            bmpposy3 = getHeight() - 250 - bitmapblue.getHeight() / 2;
            count++;
        }
        canvas.drawColor(Color.WHITE);
        canvas.drawPath(pathgreen, pathPaintgreen);
        canvas.drawPath(pathred, pathPaintred);
        canvas.drawPath(pathblue, pathPaintblue);
        if (!movestatblue || !movestatgreen || !movestatred) {
            canvas.drawBitmap(bitmapgreen, bmpposx1, bmpposy1, null);
            canvas.drawBitmap(bitmapred, bmpposx2, bmpposy2, null);
            canvas.drawBitmap(bitmapblue, bmpposx3, bmpposy3, null);
        }
        canvas.drawLine(getWidth() / 2 - 123, 150, getWidth() / 2 + 123, 150, rectPaint);
        canvas.drawLine(getWidth() / 2 - 120, 150, getWidth() / 2 - 120, 500, rectPaint);
        canvas.drawLine(getWidth() / 2 + 120, 150, getWidth() / 2 + 120, 500, rectPaint);

        canvas.drawLine(getWidth() / 2 - 123 - 317, 150, getWidth() / 2 - 123 - 67, 150, rectPaint);
        canvas.drawLine(getWidth() / 2 - 120 - 317, 150, getWidth() / 2 - 120 - 317, 500, rectPaint);
        canvas.drawLine(getWidth() / 2 - 120 - 67, 150, getWidth() / 2 - 120 - 67, 500, rectPaint);

        canvas.drawLine(getWidth() / 2 + 123 + 317, 150, getWidth() / 2 + 123 + 67, 150, rectPaint);
        canvas.drawLine(getWidth() / 2 + 120 + 317, 150, getWidth() / 2 + 120 + 317, 500, rectPaint);
        canvas.drawLine(getWidth() / 2 + 120 + 67, 150, getWidth() / 2 + 120 + 67, 500, rectPaint);


        if (movestatred && movestatgreen && movestatblue) {
            bm_offsetX -=30;
            if(posblue[1] < getHeight() - 250 - bitmapblue.getHeight() / 2 -150 && posgreen[1] < getHeight() - 250 - bitmapblue.getHeight() / 2 -150 && posred[1] < getHeight() - 250 - bitmapblue.getHeight() / 2 -150)
            if ((posgreen[0] + bm_offsetX > posred[0] - bm_offsetX && posgreen[0] - bm_offsetX < posred[0] - bm_offsetX && posgreen[1] < posred[1] + bm_offsetY && posgreen[1] > posred[1] - bm_offsetY)
                    || (posgreen[0] - bm_offsetX < posred[0] + bm_offsetX && posgreen[0] + bm_offsetX > posred[0] + bm_offsetX && posgreen[1] < posred[1] + bm_offsetY && posgreen[1] > posred[1] - bm_offsetY) ||
                    (posgreen[0] > posred[0] - bm_offsetX && posgreen[0] < posred[0] + bm_offsetX && posgreen[1] - bm_offsetY < posred[1] + bm_offsetY && posgreen[1] - bm_offsetY > posred[1] - bm_offsetY)
                    || (posblue[0] + bm_offsetX > posred[0] - bm_offsetX && posblue[0] - bm_offsetX < posred[0] - bm_offsetX && posblue[1] < posred[1] + bm_offsetY && posblue[1] > posred[1] - bm_offsetY)
                    || (posblue[0] - bm_offsetX < posred[0] + bm_offsetX && posblue[0] + bm_offsetX > posred[0] + bm_offsetX && posblue[1] < posred[1] + bm_offsetY && posblue[1] > posred[1] - bm_offsetY) ||
                    (posblue[0] > posred[0] - bm_offsetX && posblue[0] < posred[0] + bm_offsetX && posblue[1] - bm_offsetY < posred[1] + bm_offsetY && posblue[1] - bm_offsetY > posred[1] - bm_offsetY)
                    || (posgreen[0] + bm_offsetX > posblue[0] - bm_offsetX && posgreen[0] - bm_offsetX < posblue[0] - bm_offsetX && posgreen[1] < posblue[1] + bm_offsetY && posgreen[1] > posblue[1] - bm_offsetY)
                    || (posgreen[0] - bm_offsetX < posblue[0] + bm_offsetX && posgreen[0] + bm_offsetX > posblue[0] + bm_offsetX && posgreen[1] < posblue[1] + bm_offsetY && posgreen[1] > posblue[1] - bm_offsetY) ||
                    (posgreen[0] > posblue[0] - bm_offsetX && posgreen[0] < posblue[0] + bm_offsetX && posgreen[1] - bm_offsetY < posblue[1] + bm_offsetY && posgreen[1] - bm_offsetY > posblue[1] - bm_offsetY)) {

                movestatgreen = false;
                movestatred = false;
                movestatblue = false;
                vibrator.vibrate(700);
                mediaPlayer.start();
                ;Toast.makeText(getContext() , "COLLISION!!!" , Toast.LENGTH_LONG).show();


            }
            bm_offsetX += 30;


            if (distancegreen < pathLengthgreen) {
                pathMeasuregreen.getPosTan(distancegreen, posgreen, tangreen);
                matrixgreen.reset();
                float degrees = (float) (Math.atan2(tangreen[1], tangreen[0]) * 180.0 / Math.PI);
                matrixgreen.postRotate(degrees + 90, bm_offsetX, bm_offsetY);
                curXgreen = posgreen[0] - bm_offsetX;
                curYgreen = posgreen[1] - bm_offsetY;
                matrixgreen.postTranslate(curXgreen, curYgreen);
                if (degrees > -100 && degrees < -80) {
                    hnjgreen = bm_offsetY;
                } else {
                    hnjgreen = bm_offsetX;
                }
                if (!(degrees > -150 && degrees < -30) && !(degrees > 30 && degrees < 150)) {
                    pnjgreen = bm_offsetY;
                }

                canvas.drawBitmap(bitmapgreen, matrixgreen, null);
                distancegreen += step;
            } else {
                movestatgreen = false;
            }
            if (distancered < pathLengthred) {
                pathMeasurereed.getPosTan(distancered, posred, tanred);
                matrixred.reset();
                float degrees = (float) (Math.atan2(tanred[1], tanred[0]) * 180.0 / Math.PI);
                matrixred.postRotate(degrees + 90, bm_offsetX, bm_offsetY);
                curXred = posred[0] - bm_offsetX;
                curYred = posred[1] - bm_offsetY;
                matrixred.postTranslate(curXred, curYred);
                if (degrees > -100 && degrees < -80) {
                    hnjred = bm_offsetY;
                } else {
                    hnjred = bm_offsetX;
                }
                if (!(degrees > -150 && degrees < -30) && !(degrees > 30 && degrees < 150)) {
                    pnjred = bm_offsetY;
                }
                canvas.drawBitmap(bitmapred, matrixred, null);
                distancered += step;
            } else {
                movestatred = false;
            }

            if (distanceblue < pathLengthblue) {
                pathMeasureblue.getPosTan(distanceblue, posblue, tanblue);
                matrixblue.reset();
                float degrees = (float) (Math.atan2(tanblue[1], tanblue[0]) * 180.0 / Math.PI);
                matrixblue.postRotate(degrees + 90, bm_offsetX, bm_offsetY);
                curXblue = posblue[0] - bm_offsetX;
                curYblue = posblue[1] - bm_offsetY;
                matrixblue.postTranslate(curXblue, curYblue);
                if (degrees > -100 && degrees < -80) {
                    hnjblue = bm_offsetY;
                } else {
                    hnjblue = bm_offsetX;
                }
                if (!(degrees > -150 && degrees < -30) && !(degrees > 30 && degrees < 150)) {
                    pnjblue = bm_offsetY;
                }
                canvas.drawBitmap(bitmapblue, matrixblue, null);
                distanceblue += step;
            } else {
                movestatblue = false;
            }
            if (distancegreen > pathLengthgreen && distancered > pathLengthred && distanceblue > pathLengthblue) {
                distanceblue = distancegreen = distancered = 0;
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
                if (!extragreen && xx > bmpposx1 && xx < bmpposx1 + bitmapgreen.getWidth() && yy > bmpposy1 && yy < bmpposy1 + bitmapgreen.getHeight() && !movestatgreen && bmpposy1 != 325 - bitmapgreen.getHeight() / 2) {
                    pathgreen.moveTo(xx, yy);
                    statgreen = true;
                } else if (!extrared && xx > bmpposx2 && xx < bmpposx2 + bitmapred.getWidth() && yy > bmpposy2 && yy < bmpposy2 + bitmapred.getHeight() && !movestatred && bmpposy2 != 325 - bitmapred.getHeight() / 2) {
                    pathred.moveTo(xx, yy);
                    statred = true;
                } else if (!extrablue && xx > bmpposx3 && xx < bmpposx3 + bitmapblue.getWidth() && yy > bmpposy3 && yy < bmpposy3 + bitmapblue.getHeight() && !movestatblue && bmpposy2 != 325 - bitmapblue.getHeight() / 2) {
                    pathblue.moveTo(xx, yy);
                    statblue = true;
                }

                return true;
            }
            case MotionEvent.ACTION_MOVE: {
                if (statgreen) {
                    pathgreen.lineTo(xx, yy);
                } else if (statred) {
                    pathred.lineTo(xx, yy);
                } else if (statblue) {
                    pathblue.lineTo(xx, yy);
                }
                postInvalidate();
                return true;
            }
            case MotionEvent.ACTION_UP: {

                if (statgreen) {
                    if ((xx > getWidth() / 2 - 440 && yy > 150 && xx < getWidth() / 2 - 190 && yy < 500) || (xx > getWidth() / 2 - 150 && yy > 150 && xx < getWidth() / 2 + 150 && yy < 500)
                            || (xx > getWidth() / 2 + 190 && yy > 150 && xx < getWidth() / 2 + 440 && yy < 500)) {
                        movestatgreen = true;
                        matrixgreen.postRotate(90);
                        if ((xx > getWidth() / 2 - 440 && yy > 150 && xx < getWidth() / 2 - 190 && yy < 500)) {
                            pathgreen.lineTo(getWidth() / 2 - 123 - 197, 325);
                        } else if (xx > getWidth() / 2 - 150 && yy > 150 && xx < getWidth() / 2 + 150 && yy < 500) {
                            pathgreen.lineTo(getWidth() / 2, 325);
                        } else if (xx > getWidth() / 2 + 190 && yy > 150 && xx < getWidth() / 2 + 440 && yy < 500) {
                            pathgreen.lineTo(getWidth() / 2 + 123 + 197, 325);
                        }
                        pathMeasuregreen = new PathMeasure(pathgreen, false);
                        pathLengthgreen = pathMeasuregreen.getLength();
                        statgreen = false;
                        extragreen = true;
                    } else {
                        statgreen = false;
                        pathgreen.reset();
                    }
                    invalidate();
                } else if (statred) {
                    if ((xx > getWidth() / 2 - 440 && yy > 150 && xx < getWidth() / 2 - 190 && yy < 500) || (xx > getWidth() / 2 - 150 && yy > 150 && xx < getWidth() / 2 + 150 && yy < 500)
                            || (xx > getWidth() / 2 + 190 && yy > 150 && xx < getWidth() / 2 + 440 && yy < 500)) {
                        movestatred = true;
                        matrixred.postRotate(90);
                        if ((xx > getWidth() / 2 - 440 && yy > 150 && xx < getWidth() / 2 - 190 && yy < 500)) {
                            pathred.lineTo(getWidth() / 2 - 123 - 197, 325);
                        } else if (xx > getWidth() / 2 - 150 && yy > 150 && xx < getWidth() / 2 + 150 && yy < 500) {
                            pathred.lineTo(getWidth() / 2, 325);
                        } else if (xx > getWidth() / 2 + 190 && yy > 150 && xx < getWidth() / 2 + 440 && yy < 500) {
                            pathred.lineTo(getWidth() / 2 + 123 + 197, 325);
                        }
                        pathMeasurereed = new PathMeasure(pathred, false);
                        pathLengthred = pathMeasurereed.getLength();
                        statred = false;
                        extrared = true;
                    } else {
                        statred = false;
                        pathred.reset();
                    }
                    invalidate();
                } else if (statblue) {
                    if ((xx > getWidth() / 2 - 440 && yy > 150 && xx < getWidth() / 2 - 190 && yy < 500) || (xx > getWidth() / 2 - 150 && yy > 150 && xx < getWidth() / 2 + 150 && yy < 500)
                            || (xx > getWidth() / 2 + 190 && yy > 150 && xx < getWidth() / 2 + 440 && yy < 500)) {
                        movestatblue = true;
                        matrixblue.postRotate(90);
                        if ((xx > getWidth() / 2 - 440 && yy > 150 && xx < getWidth() / 2 - 190 && yy < 500)) {
                            pathblue.lineTo(getWidth() / 2 - 123 - 197, 325);
                        } else if (xx > getWidth() / 2 - 150 && yy > 150 && xx < getWidth() / 2 + 150 && yy < 500) {
                            pathblue.lineTo(getWidth() / 2, 325);
                        } else if (xx > getWidth() / 2 + 190 && yy > 150 && xx < getWidth() / 2 + 440 && yy < 500) {
                            pathblue.lineTo(getWidth() / 2 + 123 + 197, 325);
                        }
                        pathMeasureblue = new PathMeasure(pathblue, false);
                        pathLengthblue = pathMeasureblue.getLength();
                        statblue = false;
                        extrablue = true;
                    } else {
                        statblue = false;
                        pathblue.reset();
                    }
                    invalidate();
                }
                if (movestatblue && movestatred && movestatgreen) {
                    extrablue = extragreen = extrared = false;
                    bmpposx1 = getWidth() / 2 - 123 - 197 - bitmapgreen.getWidth() / 2;
                    bmpposy1 = 325 - bitmapgreen.getHeight() / 2;
                    bmpposx2 = getWidth() / 2 - bitmapred.getWidth() / 2;
                    bmpposy2 = 325 - bitmapred.getHeight() / 2;
                    bmpposx3 = getWidth() / 2 + 123 + 197 - bitmapblue.getWidth() / 2;
                    bmpposy3 = 325 - bitmapblue.getHeight() / 2;
                    pathgreen.reset();
                    pathred.reset();
                    pathblue.reset();
                    invalidate();
                }
                return val;
            }
        }

        return val;
    }

    public void redo() {
        statgreen = false;
        statred = false;
        statblue = false;
        pathred.reset();
        pathgreen.reset();
        pathblue.reset();
        movestatgreen = false;
        movestatred = false;
        movestatblue = false;
        count = 0;
        distancegreen = 0;
        distancered = 0;
        distanceblue = 0;
        curXgreen = 0;
        curXred = 0;
        curXblue = 0;
        curYred = 0;
        curYgreen = 0;
        curYblue = 0;
        bm_offsetX = bitmapred.getWidth()/2;
        bm_offsetY = bitmapred.getHeight()/2;
        mediaPlayer.stop();
        try {
            mediaPlayer.prepare();
        } catch (IOException e) {
            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
        postInvalidate();

    }
}
