package ua.com.codefire.gamedev.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import ua.com.codefire.gamedev.util.MetricsUtil;

/**
 * Created by human on 2/4/17.
 */

public class GameRender implements Runnable, TouchEvent, JoystickChangedListener {
    public static final int FRAMES_PER_SECOND = 60;
    public static final int TURN_CENTER = 0;
    public static final int TURN_LEFT = 1;
    public static final int TURN_FULL_LEFT = 2;
    public static final int TURN_RIGHT = 3;
    public static final int TURN_FULL_RIGHT = 4;
    public static final float USER_OBJECT_RADIUS = 80.f;

    public static final int JOYSTICK_SIZE = 40;

    public static final int SRC_FRAME_SIZE_W = 200;
    public static final int SRC_FRAME_SIZE_H = 200;
    public static final int DST_FRAME_SIZE_W = 100;
    public static final int DST_FRAME_SIZE_H = 100;

    private static final String TAG = "GRP";
    private static final int COLOR_FLOW = 0x00607D8B;
    private static final int COLOR_PLAIN = 0xFFF44336;
    private static final int COLOR_JOYSTICK_BG = 0x9AFFFFFF;
    private static final int COLOR_JOYSTICK_FG = 0x9AFFAAAA;

    private final Context context;
    private final SurfaceHolder surfaceHolder;
    private boolean rendering;

    // GRAPHICS
    private Paint paintFlow;
    private Paint paintPlain;

    // LOCATION
    private float flowLocation = .0f;
    private Bitmap spritePlain;

    private int stateX = 2;
    private int stateY = 2;

    private PointF plainLocation;
    private PointF enemyLocation;

    private Bitmap spriteEnemy;

    private JoystickRender joystickRender;
    private AmmoRender ammoRender;

    public GameRender(Context context, SurfaceHolder surfaceHolder) {
        this.context = context;
        this.surfaceHolder = surfaceHolder;

        // INITIALIZE JOYSTICK
        this.joystickRender = new JoystickRender(COLOR_JOYSTICK_BG, COLOR_JOYSTICK_FG,
                MetricsUtil.convertDpToPixel(context, JOYSTICK_SIZE));
        this.joystickRender.setChangedListener(this);

        this.paintFlow = new Paint();
        paintFlow.setColor(COLOR_FLOW);

        this.paintPlain = new Paint();
        paintPlain.setAntiAlias(true);
        paintPlain.setColor(COLOR_PLAIN);

        this.plainLocation = new PointF();
        this.enemyLocation = new PointF(0.f, -150.f);
    }

    public PointF getPlainLocation() {
        return plainLocation;
    }

    public void setSpritePlain(Bitmap spritePlain) {
        this.spritePlain = spritePlain;
    }

    public void setSpriteWater(Bitmap spriteWater) {
//        paintFlow.setShader(new BitmapShader(spriteWater, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT));
    }

    public void setSpriteEnemy(Bitmap spriteEnemy) {
        this.spriteEnemy = spriteEnemy;
    }

    public void setSpriteAmmo(Bitmap sprite) {
        this.ammoRender = new AmmoRender(sprite, new Point(26,24), 14, 20, plainLocation, 64, 64);
    }

    @Override
    public void run() {
        rendering = true;

        while (rendering) {
            try {
                Canvas canvas = surfaceHolder.lockCanvas();

                if (canvas == null) {
                    break;
                }

                synchronized (surfaceHolder) {
                    update();
                    draw(canvas);

                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                rendering = false;
                break;
            }

            try {
                Thread.sleep(1000 / FRAMES_PER_SECOND);
            } catch (InterruptedException e) {
                e.printStackTrace();
                rendering = false;
                break;
            }
        }
    }

    /**
     * Update mechanism.
     */
    private void update() {
        validateLocation();

        enemyLocation.y += 5.f;

        if (enemyLocation.y > surfaceHolder.getSurfaceFrame().height() + 150) {
            enemyLocation.y = -150;
        }

        flowLocation += 2.5f;

        if (flowLocation == 256.f) {
            flowLocation = .0f;
        }
    }

    /**
     * Validate frame out.
     */
    private void validateLocation() {
        Rect rect = surfaceHolder.getSurfaceFrame();

        float w = DST_FRAME_SIZE_W / 2;
        float h = DST_FRAME_SIZE_H / 2;

        if (plainLocation.x < w) {
            plainLocation.x = w;
        } else if (plainLocation.x > rect.width() - w) {
            plainLocation.x = rect.width() - w;
        }

        if (plainLocation.y < h) {
            plainLocation.y = h;
        } else if (plainLocation.y > rect.height() - h) {
            plainLocation.y = rect.height() - h;
        }
    }

    /**
     * Draw mechanism.
     */
    private void draw(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

        drawFlow(canvas);
        drawRightJoystick(canvas);

//        {
//            canvas.save();
//            float cx = surfaceHolder.getSurfaceFrame().exactCenterX();
//
//            int size = 150;
//
//            Rect srcRect = new Rect(0, size * 2, size, (size * 2) + size);
//
//            float fsize = MetricsUtil.convertDpToPixel(context, 150);
//
//            RectF dstRect = new RectF(cx - fsize / 2, enemyLocation.y, cx + fsize / 2, enemyLocation.y + fsize);
//
//            canvas.drawBitmap(spriteEnemy, srcRect, dstRect, paintPlain);
//            canvas.restore();
//        }

//        canvas.drawRect(surfaceHolder.getSurfaceFrame(), paintFlow);
//        canvas.drawCircle(plainLocation.x, plainLocation.y, USER_OBJECT_RADIUS, paintPlain);

        Rect srcRect = new Rect(stateX * SRC_FRAME_SIZE_W, stateY * SRC_FRAME_SIZE_H,
                stateX * SRC_FRAME_SIZE_W + SRC_FRAME_SIZE_W, stateY * SRC_FRAME_SIZE_H + SRC_FRAME_SIZE_H);

        float ddw = MetricsUtil.convertDpToPixel(context, DST_FRAME_SIZE_W) / 2;
        float ddh = MetricsUtil.convertDpToPixel(context, DST_FRAME_SIZE_H) / 2;

        RectF dstRect = new RectF(plainLocation.x - ddw, plainLocation.y - ddh,
                plainLocation.x + ddw, plainLocation.y + ddh);

        canvas.drawBitmap(spritePlain, srcRect, dstRect, paintPlain);

//        ammoRender.draw(canvas);


        drawRightJoystick(canvas);
    }

    /**
     * Drawing flow function.
     *
     * @param canvas
     */
    private void drawFlow(Canvas canvas) {
        canvas.save();
        canvas.translate(.0f, flowLocation);
        canvas.drawPaint(paintFlow);
        canvas.restore();
    }

    private void drawRightJoystick(Canvas canvas) {
        joystickRender.draw(canvas);
    }

    /**
     *
     */
    public void sizeChanged() {
        Rect rect = surfaceHolder.getSurfaceFrame();

        float diameter = joystickRender.getFlowRadius() * 2;

        PointF joystickLocation = new PointF(rect.right - diameter, rect.bottom - diameter);

        joystickRender.setFlowLocation(joystickLocation);
//
//        plainLocation.set(rect.exactCenterX(), rect.exactCenterY() + DST_FRAME_SIZE_H);
    }

    /**
     * @param side
     */
    public void turn(int side) {
        switch (side) {
            case TURN_CENTER:
                stateX = 2;
                break;
            case TURN_LEFT:
                stateX = 1;
                break;
            case TURN_FULL_LEFT:
                stateX = 0;
                break;
            case TURN_RIGHT:
                stateX = 3;
                break;
            case TURN_FULL_RIGHT:
                stateX = 4;
                break;
        }
    }

    public void stopRendering() {
        rendering = false;
    }

    @Override
    public boolean onTouch(MotionEvent event) {
        return joystickRender.onTouch(event);
    }

    @Override
    public void joystickChanged(double x, double y) {
        plainLocation.x += x * 5;
        plainLocation.y += y * 5;

        ammoRender.setLocation(plainLocation);
    }
}
