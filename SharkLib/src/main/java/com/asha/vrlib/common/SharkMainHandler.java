package com.asha.vrlib.common;


import android.os.Handler;
import android.os.Looper;


/**
 * 主消息处理者
 */
public class SharkMainHandler {


    private static Handler sMainHandler;


    public static void init() {
        if (sMainHandler == null) {
            sMainHandler = new Handler(Looper.getMainLooper());
        }
    }


    public static Handler sharedHandler() {
        return sMainHandler;
    }
}
