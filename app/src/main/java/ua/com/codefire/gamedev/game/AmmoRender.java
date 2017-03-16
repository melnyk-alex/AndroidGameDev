package ua.com.codefire.gamedev.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by human on 2/12/17.
 */

public class AmmoRender {

    private static final String TAG = "GAR";
    private final Bitmap ammo;
    private PointF location;
    private int frameWidth;
    private int frameHeight;
    private float drawWidth;
    private float drawHeight;
    private Paint paint;
    private float dw;
    private float dh;
    private Rect srcRect;
    private Point spriteLocation;

    public AmmoRender(Bitmap sprite, Point spriteLocation, int frameWidth, int frameHeight,
                      PointF location, float drawWidth, float drawHeight) {
        this.ammo = sprite;
        this.spriteLocation = spriteLocation;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        this.drawWidth = drawWidth;
        this.drawHeight = drawHeight;
        this.location = location;

        init();
    }

    private void init() {
        this.paint = new Paint();
        paint.setAntiAlias(true);

        this.dw = drawWidth / 2;
        this.dh = drawHeight / 2;

        this.srcRect = new Rect(spriteLocation.x, spriteLocation.y,
                spriteLocation.x + frameWidth, spriteLocation.y + frameHeight);
    }

    public PointF getLocation() {
        return location;
    }

    public void setLocation(PointF location) {
        this.location = location;
    }

    public void draw(Canvas canvas) {
        RectF dstRect = new RectF(location.x - dw, location.y - dh, location.x + dw, location.y + dh);
        Log.i(TAG, "SRC: " + srcRect + " DST: " + dstRect);

        canvas.drawBitmap(ammo, srcRect, dstRect, paint);
    }
}