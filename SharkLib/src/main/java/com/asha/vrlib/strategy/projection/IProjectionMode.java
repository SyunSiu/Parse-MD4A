package com.asha.vrlib.strategy.projection;

import com.asha.vrlib.model.SharkPosition;
import com.asha.vrlib.objects.SharkAbsObject3D;

/**
 * Created by hzqiujiadi on 16/6/25.
 * hzqiujiadi ashqalcn@gmail.com
 */
public interface IProjectionMode {
    SharkAbsObject3D getObject3D();
    SharkPosition getModelPosition();
}
