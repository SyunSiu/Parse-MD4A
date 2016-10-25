package com.asha.vrlib.plugins;

import android.content.Context;

import com.asha.vrlib.SharkDirector;
import com.asha.vrlib.model.SharkPosition;

/**
 * Created by hzqiujiadi on 16/7/21.
 * hzqiujiadi ashqalcn@gmail.com
 */
public abstract class SharkAbsPlugin {

    private boolean mIsInit;

    SharkPosition position = SharkPosition.sOriginalPosition;

    public final void setup(Context context){
        if (!mIsInit){
            init(context);
            mIsInit = true;
        }
    }

    abstract protected void init(Context context);

    abstract public void beforeRenderer(int totalWidth, int totalHeight);

    abstract public void renderer(int index, int itemWidth, int itemHeight, SharkDirector director);

    abstract public void destroy();

    protected SharkPosition getModelPosition(){
        return position;
    }

    public void setModelPosition(SharkPosition position) {
        this.position = position;
    }

    abstract protected boolean removable();

}
