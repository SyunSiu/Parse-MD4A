package com.asha.vrlib.strategy;

import android.app.Activity;

import com.asha.vrlib.SharkLibrary;
import com.asha.vrlib.common.SharkGLHandler;
import com.asha.vrlib.common.SharkMainHandler;

import java.util.Arrays;

/**
 * Created by hzqiujiadi on 16/3/19.
 * hzqiujiadi ashqalcn@gmail.com
 */
public abstract class ModeManager<T extends IModeStrategy> {
    private int mMode;
    private T mStrategy;
    private SharkLibrary.INotSupportCallback mCallback;
    private SharkGLHandler mGLHandler;

    public ModeManager(int mode, SharkGLHandler handler) {
        this.mGLHandler = handler;
        this.mMode = mode;
    }

    /**
     * must call after new instance
     * @param activity activity
     */
    public void prepare(Activity activity, SharkLibrary.INotSupportCallback callback){
        mCallback = callback;
        initMode(activity,mMode);
    }

    abstract protected T createStrategy(int mode);

    abstract protected int[] getModes();

    private void initMode(Activity activity, final int mode){
        if (mStrategy != null){
            off(activity);
        }
        mStrategy = createStrategy(mode);
        if (!mStrategy.isSupport(activity)){
            SharkMainHandler.sharedHandler().post(new Runnable() {
                @Override
                public void run() {
                    if (mCallback != null) mCallback.onNotSupport(mode);
                }
            });
        } else {
            on(activity);
        }
    }

    public void switchMode(final Activity activity){
        mGLHandler.post(new Runnable() {
            @Override
            public void run() {
                int[] modes = getModes();
                int mode = getMode();
                int index = Arrays.binarySearch(modes, mode);
                int nextIndex = (index + 1) %  modes.length;
                int nextMode = modes[nextIndex];

                switchMode(activity, nextMode);
            }
        });
    }

    public void switchMode(final Activity activity, final int mode){
        mGLHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mode == getMode()) return;
                mMode = mode;

                initMode(activity, mMode);
            }
        });
    }

    public void on(Activity activity) {
        if (mStrategy.isSupport(activity))
            mStrategy.on(activity);
    }

    public void off(Activity activity) {
        if (mStrategy.isSupport(activity))
            mStrategy.off(activity);
    }

    protected T getStrategy() {
        return mStrategy;
    }

    public int getMode() {
        return mMode;
    }

    public SharkGLHandler getGLHandler() {
        return mGLHandler;
    }
}
