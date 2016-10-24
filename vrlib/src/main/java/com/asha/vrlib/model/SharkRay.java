package com.asha.vrlib.model;

/**
 * Created by hzqiujiadi on 16/8/5.
 * hzqiujiadi ashqalcn@gmail.com
 */
public class SharkRay {
    private SharkVector3D mOrig;
    private SharkVector3D mDir;

    public SharkRay(SharkVector3D mOrig, SharkVector3D mDir) {
        this.mOrig = mOrig;
        this.mDir = mDir;
    }

    public SharkVector3D getOrig() {
        return mOrig;
    }

    public void setOrig(SharkVector3D mOrig) {
        this.mOrig = mOrig;
    }

    public SharkVector3D getDir() {
        return mDir;
    }

    public void setDir(SharkVector3D mDir) {
        this.mDir = mDir;
    }

    @Override
    public String toString() {
        return "SharkRay{" +
                ", mDir=" + mDir +
                ", mOrig=" + mOrig +
                '}';
    }
}
