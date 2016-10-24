package com.asha.vrlib.plugins;

import com.asha.vrlib.model.SharkRay;

/**
 * Created by hzqiujiadi on 16/8/5.
 * hzqiujiadi ashqalcn@gmail.com
 */
public interface ISharkHotspot {
    boolean hit(SharkRay ray);
    void onEyeHitIn(long timestamp);
    void onEyeHitOut();
    void onTouchHit(SharkRay ray);
    String getTitle();
    void useTexture(int key);
}
