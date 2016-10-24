package com.asha.vrlib.strategy.projection;

import android.app.Activity;

import com.asha.vrlib.SharkDirector;
import com.asha.vrlib.SharkDirectorFactory;
import com.asha.vrlib.common.SharkDirection;
import com.asha.vrlib.model.SharkMainPluginBuilder;
import com.asha.vrlib.model.SharkPosition;
import com.asha.vrlib.objects.SharkAbsObject3D;
import com.asha.vrlib.objects.SharkObject3DHelper;
import com.asha.vrlib.objects.SharkStereoSphere3D;
import com.asha.vrlib.plugins.SharkAbsPlugin;
import com.asha.vrlib.plugins.SharkPanoramaPlugin;

/**
 * Created by hzqiujiadi on 16/6/26.
 * hzqiujiadi ashqalcn@gmail.com
 */
public class StereoSphereProjection extends AbsProjectionStrategy {

    private static class FixedDirectorFactory extends SharkDirectorFactory {
        @Override
        public SharkDirector createDirector(int index) {
            return SharkDirector.builder().build();
        }
    }

    private SharkDirection direction;

    private SharkAbsObject3D object3D;

    public StereoSphereProjection(SharkDirection direction) {
        this.direction = direction;
    }

    @Override
    public void on(Activity activity) {
        object3D = new SharkStereoSphere3D(direction);
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
    protected SharkDirectorFactory hijackDirectorFactory() {
        return new FixedDirectorFactory();
    }

    @Override
    public SharkAbsPlugin buildMainPlugin(SharkMainPluginBuilder builder) {
        return new SharkPanoramaPlugin(builder);
    }
}
