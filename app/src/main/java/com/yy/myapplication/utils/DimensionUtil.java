package com.yy.myapplication.utils;

/**
 * Created by andy on 2020/9/13.
 */

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

public class DimensionUtil {

    public static int dipToPx(Context context, float dpValue) {
        if (context == null) {
            return 0;
        }
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int pxToDip(Context context, float pxValue) {
        if (context == null) {
            return 0;
        }
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int px2sp(Context context, float pxValue) {
        if (context == null) {
            return 0;
        }
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public static int sp2px(Context context, float spValue) {
        if (context == null) {
            return 0;
        }
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static int sp2Px(int sp) {
        return (int) (sp * Resources.getSystem().getDisplayMetrics().density);
    }

    /**
     * dp转为px
     *
     * @param dp dp值
     * @return px值
     */
    public static int dip2Px(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    /**
     * Gets the width of the display, in pixels.
     * <p/>
     * Note that this value should not be used for computing layouts, since a
     * device will typically have screen decoration (such as a status bar) along
     * the edges of the display that reduce the amount of application space
     * available from the size returned here. Layouts should instead use the
     * window size.
     * <p/>
     * The size is adjusted based on the current rotation of the display.
     * <p/>
     * The size returned by this method does not necessarily represent the
     * actual raw size (native resolution) of the display. The returned size may
     * be adjusted to exclude certain system decoration elements that are always
     * visible. It may also be scaled to provide compatibility with older
     * applications that were originally designed for smaller displays.
     *
     * @return Screen width in pixels.
     */
    public static int getScreenWidth(Context context) {
        return getScreenSize(context).x;
    }

    /**
     * Gets the height of the display, in pixels.
     * <p/>
     * Note that this value should not be used for computing layouts, since a
     * device will typically have screen decoration (such as a status bar) along
     * the edges of the display that reduce the amount of application space
     * available from the size returned here. Layouts should instead use the
     * window size.
     * <p/>
     * The size is adjusted based on the current rotation of the display.
     * <p/>
     * The size returned by this method does not necessarily represent the
     * actual raw size (native resolution) of the display. The returned size may
     * be adjusted to exclude certain system decoration elements that are always
     * visible. It may also be scaled to provide compatibility with older
     * applications that were originally designed for smaller displays.
     *
     * @return Screen height in pixels.
     */
    public static int getScreenHeight(Context context) {
        return getScreenSize(context).y;
    }

    /**
     * Gets the size of the display, in pixels.
     * <p/>
     * Note that this value should not be used for computing layouts, since a
     * device will typically have screen decoration (such as a status bar) along
     * the edges of the display that reduce the amount of application space
     * available from the size returned here. Layouts should instead use the
     * window size.
     * <p/>
     * The size is adjusted based on the current rotation of the display.
     * <p/>
     * The size returned by this method does not necessarily represent the
     * actual raw size (native resolution) of the display. The returned size may
     * be adjusted to exclude certain system decoration elements that are always
     * visible. It may also be scaled to provide compatibility with older
     * applications that were originally designed for smaller displays.
     *
     * @return Screen size in pixels, the x is the width, the y is the height.
     */
    private static Point getScreenSize(Context context) {
        if (context == null) {
            return new Point();
        }
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point ret = new Point();
        final Display defaultDisplay;
        if (wm != null) {
            defaultDisplay = wm.getDefaultDisplay();
            defaultDisplay.getSize(ret);
        }
        return ret;
    }

    public static int getDpi(Context context) {
        if (context == null) {
            return 0;
        }
        return context.getResources().getDisplayMetrics().densityDpi;
    }

    public static float getDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    public static int getRealHeight(Context context) {
        if (context == null) {
            return 0;
        }
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            display.getRealMetrics(dm);
        } else {
            display.getMetrics(dm);
        }
        return dm.heightPixels;
    }

    public static int getRealWidth(Context context) {
        if (context == null) {
            return 0;
        }
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            display.getRealMetrics(dm);
        } else {
            display.getMetrics(dm);
        }
        return dm.widthPixels;
    }

    public static boolean checkHasNavigationBar(Activity context) {
        WindowManager windowManager = context.getWindowManager();
        Display d = windowManager.getDefaultDisplay();

        DisplayMetrics realDisplayMetrics = new DisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            d.getRealMetrics(realDisplayMetrics);
        }

        int realHeight = realDisplayMetrics.heightPixels;
        int realWidth = realDisplayMetrics.widthPixels;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        d.getMetrics(displayMetrics);

        int displayHeight = displayMetrics.heightPixels;
        int displayWidth = displayMetrics.widthPixels;

        return (realWidth - displayWidth) > 0 || (realHeight - displayHeight) > 0;
    }

    public static int getVirtualBarHeight(Context context) {
        if (context == null) {
            return 0;
        }
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height",
                "dimen", "android");
        int height = 0;
        if (resourceId > 0) {
            //获取NavigationBar的高度
            height = resources.getDimensionPixelSize(resourceId);
        }
        return height;
    }
}
