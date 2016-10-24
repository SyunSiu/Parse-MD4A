package com.asha.vrlib.common;


import android.util.Log;


/**
 * FPS 计算工具类
 */
public class FpsUtil {


    private final static String TAG = "fps";
    private int mFrameCount;
    private long mLastTimestamp;


    public void step() {
        if (mFrameCount % 120 == 0) {
            long current = System.currentTimeMillis();
            if (mLastTimestamp != 0) {
                float fps = mFrameCount * 1000.0f / (current - mLastTimestamp);
                Log.w(TAG, "fps = " + fps);
            }
            mFrameCount = 0;
            mLastTimestamp = current;
        }
        mFrameCount++;
    }
}
