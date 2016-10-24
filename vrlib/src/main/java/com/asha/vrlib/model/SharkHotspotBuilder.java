package com.asha.vrlib.model;

import android.util.SparseArray;

import com.asha.vrlib.SharkLibrary;
import com.asha.vrlib.texture.SharkBitmapTexture;
import com.asha.vrlib.texture.SharkTexture;

/**
 * Created by hzqiujiadi on 16/8/10.
 * hzqiujiadi ashqalcn@gmail.com
 */
public class SharkHotspotBuilder {

    public float width = 2;

    public float height = 2;

    public String title;

    public SharkLibrary.ITouchPickListener clickListener;

    public SharkPosition position;

    public SparseArray<SharkTexture> textures = new SparseArray<>(6);

    public int[] statusList;

    public int[] checkedStatusList;

    public static SharkHotspotBuilder create(){
        return new SharkHotspotBuilder();
    }

    private SharkHotspotBuilder status(int normal, int focused, int pressed){
        statusList = new int[]{normal, focused, pressed};
        return this;
    }

    public SharkHotspotBuilder status(int normal, int focused){
        return status(normal, focused, focused);
    }

    public SharkHotspotBuilder status(int normal){
        return status(normal,normal);
    }

    private SharkHotspotBuilder checkedStatus(int normal, int focused, int pressed){
        checkedStatusList = new int[]{normal, focused, pressed};
        return this;
    }

    public SharkHotspotBuilder checkedStatus(int normal, int focused){
        return checkedStatus(normal, focused, focused);
    }

    public SharkHotspotBuilder checkedStatus(int normal){
        return checkedStatus(normal, normal);
    }

    public SharkHotspotBuilder title(String title){
        this.title = title;
        return this;
    }

    public SharkHotspotBuilder size(float width, float height){
        this.width = width;
        this.height = height;
        return this;
    }

    public SharkHotspotBuilder provider(SharkLibrary.IBitmapProvider provider){
        provider(0,provider);
        return this;
    }

    public SharkHotspotBuilder provider(int key, SharkLibrary.IBitmapProvider provider){
        textures.append(key,new SharkBitmapTexture(provider));
        return this;
    }

    public SharkHotspotBuilder position(SharkPosition position) {
        this.position = position;
        return this;
    }

    public SharkHotspotBuilder listenClick(SharkLibrary.ITouchPickListener listener){
        this.clickListener = listener;
        return this;
    }
}
