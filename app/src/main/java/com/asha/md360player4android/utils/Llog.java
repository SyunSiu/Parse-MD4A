package com.asha.md360player4android.utils;

import com.asha.md360player4android.BuildConfig;
import com.orhanobut.logger.Logger;


/**
 * 自定义的Log工具类.
 * <p>
 * Created by Dwyane on 2016/7/26.
 */
public class Llog {


    private static final String TAG = "Tony";

    static {
        Logger.init(TAG);
        Llog.debug("调用日志工具类的 静态代码块  初始化logger");
    }


    public static String getTAG() {
        return TAG;
    }

    /**
     * 打印 debug Log 的方法
     */
    public static void debug(String s) {
        if (BuildConfig.LOG_DEBUG) {
            Logger.d(s);
        }
    }

    /**
     * 打印 info Log 的方法
     */
    public static void info(String s) {
        if (BuildConfig.LOG_DEBUG) {
            Logger.i(s);
        }
    }

    /**
     * 打印 waring Log 的方法
     */
    public static void waring(String s) {
        if (BuildConfig.LOG_DEBUG) {
            Logger.w(s);
        }
    }

    /**
     * 打印 error Log 的方法
     */
    public static void error(String s) {
        if (BuildConfig.LOG_DEBUG) {
            Logger.e(s);
        }
    }
}
