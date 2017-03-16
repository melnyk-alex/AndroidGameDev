package ua.com.codefire.gamedev.game;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.view.MotionEvent;

/**
 * Created by human on 2/12/17.
 */

public class JoystickRender implements TouchEvent {

    private final Paint paint;
    private final int bgColor;
    private final int fgColor;
    private final float flowRadius;
    private final float pinRadius;
    private PointF flowLocation;
    private PointF pinLocation;
    private PointF touchLocation;
    private boolean captured;

    private JoystickChangedListener changedListener;

    public JoystickRender(int bgColor, int fgColor, float flowRadius) {
        this.bgColor = bgColor;
        this.fgColor = fgColor;
        this.flowRadius = flowRadius;
        this.pinRadius = flowRadius / 2.f;

        this.flowLocation = new PointF();
        this.pinLocation = new PointF();
        this.touchLocation = new PointF();

        this.paint = new Paint();
        paint.setAntiAlias(true);
    }

    public float getFlowRadius() {
        return flowRadius;
    }

    public PointF getFlowLocation() {
        return flowLocation;
    }

    public void setFlowLocation(PointF flowLocation) {
        this.flowLocation = flowLocation;
        this.pinLocation = new PointF(flowLocation.x, flowLocation.y);
        this.touchLocation = new PointF(flowLocation.x, flowLocation.y);

        captured = false;
    }

    public void setChangedListener(JoystickChangedListener changedListener) {
        this.changedListener = changedListener;
    }

    public void draw(Canvas canvas) {
        validateLocation(touchLocation.x, touchLocation.y);

        paint.setColor(bgColor);

        canvas.drawCircle(flowLocation.x, flowLocation.y, flowRadius, paint);

        paint.setColor(fgColor);

        canvas.drawCircle(pinLocation.x, pinLocation.y, pinRadius, paint);
    }

    @Override
    public boolean onTouch(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (Math.abs(pinLocation.x - event.getX()) < pinRadius
                        && Math.abs(pinLocation.y - event.getY()) < pinRadius) {
                    captured = true;
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                if (captured) {
                    touchLocation = new PointF(event.getX(), event.getY());
//                    validateLocation(event.getX(), event.getY());
                }
                return true;
            case MotionEvent.ACTION_UP:
                pinLocation = new PointF(flowLocation.x, flowLocation.y);
                touchLocation = new PointF(flowLocation.x, flowLocation.y);
                captured = false;
                return true;
        }

        return false;
    }

    /**
     * @param x
     * @param y
     */
    private void validateLocation(float x, float y) {
        float dx = (float) Math.pow(x - flowLocation.x, 2); //delta of joystick movement (x)
        float dy = (float) Math.pow(y - flowLocation.y, 2); //delta of joystick movement (y)

        double distance = Math.sqrt(dx + dy);

        if (distance < flowRadius) {
            pinLocation.x = x; //pointF of joystick
            pinLocation.y = y; //pointF of joystick
        } else {
            double adx = x - flowLocation.x; // abs delta
            double ady = y - flowLocation.y; // abs delta

            double angle = Math.atan2(ady, adx);// * 180 / Math.PI;

            pinLocation.x = (float) (flowRadius * Math.cos(angle) + flowLocation.x);
            pinLocation.y = (float) (flowRadius * Math.sin(angle) + flowLocation.y);
        }

        // BASIC
//            if (adx > pinRadius) {
//                if (dx < .0f) {
//                    pinLocation.x = flowLocation.x + pinRadius;
//                } else if (dx > .0f) {
//                    pinLocation.x = flowLocation.x - pinRadius;
//                }
//            }
//
//            if (ady > pinRadius) {
//                if (dy < .0f) {
//                    pinLocation.y = flowLocation.y + pinRadius;
//                } else if (dy > .0f) {
//                    pinLocation.y = flowLocation.y - pinRadius;
//                }
//            }
        // BASIC

        if (distance == 0) {
            return;
        }

        if (changedListener != null) {
            changedListener.joystickChanged((pinLocation.x - flowLocation.x) / pinRadius,
                    (pinLocation.y - flowLocation.y) / pinRadius);
        }
    }
}