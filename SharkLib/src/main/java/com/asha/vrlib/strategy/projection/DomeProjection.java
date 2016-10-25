package com.asha.vrlib.strategy.projection;

import android.app.Activity;
import android.graphics.RectF;

import com.asha.vrlib.model.SharkMainPluginBuilder;
import com.asha.vrlib.model.SharkPosition;
import com.asha.vrlib.objects.SharkAbsObject3D;
import com.asha.vrlib.objects.SharkDome3D;
import com.asha.vrlib.objects.SharkObject3DHelper;
import com.asha.vrlib.plugins.SharkAbsPlugin;
import com.asha.vrlib.plugins.SharkPanoramaPlugin;

/**
 * Created by hzqiujiadi on 16/6/25.
 * hzqiujiadi ashqalcn@gmail.com
 */
public class DomeProjection extends AbsProjectionStrategy {

    SharkAbsObject3D object3D;

    private float mDegree;

    private boolean mIsUpper;

    private RectF mTextureSize;

    public DomeProjection(RectF textureSize, float degree, boolean isUpper) {
        this.mTextureSize = textureSize;
        this.mDegree = degree;
        this.mIsUpper = isUpper;
    }

    @Override
    public void on(Activity activity) {
        object3D = new SharkDome3D(mTextureSize, mDegree, mIsUpper);
        SharkObject3DHelper.loadObj(activity, object3D);
    }

    @Override
    public void off(Activity activity) {

    }

    @Override
    public boolean isSupport(Activity activity) {
        return true;
    }

    @Override
    public SharkAbsObject3D getObject3D() {
        return object3D;
    }

    @Override
    public SharkPosition getModelPosition() {
        return SharkPosition.sOriginalPosition;
    }

    @Override
    public SharkAbsPlugin buildMainPlugin(SharkMainPluginBuilder builder) {
        return new SharkPanoramaPlugin(builder);
    }
}
