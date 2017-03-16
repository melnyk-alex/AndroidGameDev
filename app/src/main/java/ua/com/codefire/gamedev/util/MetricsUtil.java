package ua.com.codefire.gamedev.util;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by human on 2/12/17.
 */

public final class MetricsUtil {
    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param context Context to get resources and device specific display metrics
     * @param dp      A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPixel(Context context, float dp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param context Context to get resources and device specific display metrics
     * @param px      A value in px (pixels) unit. Which we need to convert into db
     * @return A float value to represent dp equivalent to px value
     */
    public static float convertPixelsToDp(Context context, float px) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
}
