package com.asha.vrlib.strategy.projection;

import android.app.Activity;

import com.asha.vrlib.model.SharkMainPluginBuilder;
import com.asha.vrlib.model.SharkPosition;
import com.asha.vrlib.objects.SharkAbsObject3D;
import com.asha.vrlib.objects.SharkObject3DHelper;
import com.asha.vrlib.objects.SharkSphere3D;
import com.asha.vrlib.plugins.SharkAbsPlugin;
import com.asha.vrlib.plugins.SharkPanoramaPlugin;

/**
 * Created by hzqiujiadi on 16/6/25.
 * hzqiujiadi ashqalcn@gmail.com
 */
public class SphereProjection extends AbsProjectionStrategy {

    private SharkAbsObject3D object3D;

    public SphereProjection() {

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
    public void on(Activity activity) {
        object3D = new SharkSphere3D();
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
    public SharkAbsPlugin buildMainPlugin(SharkMainPluginBuilder builder) {
        return new SharkPanoramaPlugin(builder);
    }
}
