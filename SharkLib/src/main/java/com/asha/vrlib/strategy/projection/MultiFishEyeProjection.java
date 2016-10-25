package com.asha.vrlib.strategy.projection;

import com.asha.vrlib.common.SharkDirection;
import com.asha.vrlib.model.SharkMainPluginBuilder;
import com.asha.vrlib.plugins.SharkAbsPlugin;
import com.asha.vrlib.plugins.SharkMultiFishEyePlugin;

/**
 * Created by hzqiujiadi on 16/7/29.
 * hzqiujiadi ashqalcn@gmail.com
 */
public class MultiFishEyeProjection extends SphereProjection {

    private float radius;
    private SharkDirection direction;

    public MultiFishEyeProjection(float radius, SharkDirection direction) {
        this.radius = radius;
        this.direction = direction;
    }

    @Override
    public SharkAbsPlugin buildMainPlugin(SharkMainPluginBuilder builder) {
        return new SharkMultiFishEyePlugin(builder, radius, direction);
    }
}
