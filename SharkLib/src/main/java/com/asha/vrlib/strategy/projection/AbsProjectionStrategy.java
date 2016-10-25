package com.asha.vrlib.strategy.projection;

import android.content.Context;

import com.asha.vrlib.SharkDirectorFactory;
import com.asha.vrlib.model.SharkMainPluginBuilder;
import com.asha.vrlib.plugins.SharkAbsPlugin;
import com.asha.vrlib.strategy.IModeStrategy;

/**
 * Created by hzqiujiadi on 16/6/25.
 * hzqiujiadi ashqalcn@gmail.com
 */
public abstract class AbsProjectionStrategy implements IModeStrategy, IProjectionMode {

    @Override
    public void onResume(Context context) {

    }

    @Override
    public void onPause(Context context) {

    }

    protected SharkDirectorFactory hijackDirectorFactory(){ return null; }

    abstract SharkAbsPlugin buildMainPlugin(SharkMainPluginBuilder builder);
}
