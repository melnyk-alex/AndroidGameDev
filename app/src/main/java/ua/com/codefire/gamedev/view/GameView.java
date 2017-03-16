package ua.com.codefire.gamedev.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import ua.com.codefire.gamedev.R;
import ua.com.codefire.gamedev.game.GameRender;

import static ua.com.codefire.gamedev.game.GameRender.TURN_CENTER;
import static ua.com.codefire.gamedev.game.GameRender.TURN_FULL_LEFT;
import static ua.com.codefire.gamedev.game.GameRender.TURN_FULL_RIGHT;
import static ua.com.codefire.gamedev.game.GameRender.TURN_LEFT;
import static ua.com.codefire.gamedev.game.GameRender.TURN_RIGHT;

/**
 * Created by human on 2/4/17.
 */

public class GameView extends SurfaceView implements SurfaceHolder.Callback, SensorEventListener {

    private static final String TAG = "GV";
    private static final double MAX = Math.PI;

    private GameRender render;
    private boolean captured;
    private float previousX = .0f;

    public GameView(Context context) {
        super(context);

        setZOrderOnTop(true);
        getHolder().setFormat(PixelFormat.TRANSPARENT);

        this.render = new GameRender(context, getHolder());

        getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Bitmap spritePlain = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.plane);
        Bitmap spriteWater = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.water);
        Bitmap spriteEnemy = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.enemy);
        Bitmap spriteAmmo = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.ammo);

        render.setSpritePlain(spritePlain);
        render.setSpriteWater(spriteWater);
        render.setSpriteEnemy(spriteEnemy);
        render.setSpriteAmmo(spriteAmmo);

        new Thread(render).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        render.sizeChanged();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        render.stopRendering();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return render.onTouch(event);


//        PointF userLocation = render.getPlainLocation();
//
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                if (Math.abs(userLocation.x - event.getX()) < GameRender.USER_OBJECT_RADIUS
//                        && Math.abs(userLocation.y - event.getY()) < GameRender.USER_OBJECT_RADIUS) {
//                    captured = true;
//                }
//
//                return true;
//            case MotionEvent.ACTION_MOVE:
//                if (captured) {
//                    // TODO: Fix BUG.
//                    userLocation.set(event.getX(), event.getY());
//                }
//
//                int centerX = getWidth() / 2;
//                int quarterX = getWidth() / 4;
//
//                if (event.getX() < centerX - quarterX) {
//                    render.turn(TURN_FULL_LEFT);
//                } else if (event.getX() < centerX) {
//                    render.turn(TURN_LEFT);
//                } else if (event.getX() > centerX + quarterX) {
//                    render.turn(TURN_FULL_RIGHT);
//                } else if (event.getX() > centerX) {
//                    render.turn(TURN_RIGHT);
//                } else {
//                    render.turn(TURN_CENTER);
//                }
//                return true;
//            case MotionEvent.ACTION_UP:
//                captured = false;
//
//                render.turn(TURN_CENTER);
//                return true;
//        }
//
//        return super.onTouchEvent(event);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0] * -1;
//        float y = event.values[1];

        float delta = Math.abs(x);
//        Log.i(TAG, delta + "");

        boolean flag = false;

        if (delta <= MAX) {
            if (x < -1.f) {
                render.turn(TURN_LEFT);
                flag = true;
            } else if (x > 1.f) {
                render.turn(TURN_RIGHT);
                flag = true;
            } else {
                render.turn(TURN_CENTER);
            }
        } else {
            if (x < -1.f) {
                render.turn(TURN_FULL_LEFT);
            } else if (x > 1.f) {
                render.turn(TURN_FULL_RIGHT);
            }
            flag = true;
        }

        if (flag) {
            PointF userLocation = render.getPlainLocation();
//            userLocation.x += x;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        ;
    }
}
