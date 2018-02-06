package com.yunuo.scale;

import android.content.Context;
import android.view.WindowManager;

/**
 * Created by wangyu on 2017/12/20.
 */

class UIUtils {
    /**
     * 得到设备的密度
     */
    public static float getScreenDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    public static int getScreenWidth(Context context){
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }

    public static int dipToPx(Context context, int dp) {
        final float scale = getScreenDensity(context);
        return (int) (dp * scale + 0.5);
    }
}
