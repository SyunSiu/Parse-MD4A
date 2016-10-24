package com.asha.vrlib.model;

import android.opengl.Matrix;

/**
 * Created by hzqiujiadi on 16/8/3.
 * hzqiujiadi ashqalcn@gmail.com
 */
public class SharkPosition {

    public static final SharkPosition sOriginalPosition = SharkPosition.newInstance();

    private float[] mCurrentRotation = new float[16];
    private float mX;
    private float mY;
    private float mZ;
    private float mAngleX;
    private float mAngleY;
    private float mAngleZ;
    private float mPitch; // x-axis
    private float mYaw; // y-axis
    private float mRoll; // z-axis

    private SharkPosition() {
        mX = mY = mZ = 0;
        mAngleX = mAngleY = mAngleZ = 0;
        mPitch = mYaw = mRoll = 0;
    }

    public float getPitch() {
        return mPitch;
    }

    public SharkPosition setPitch(float pitch) {
        this.mPitch = pitch;
        return this;
    }

    public float getYaw() {
        return mYaw;
    }

    public SharkPosition setYaw(float yaw) {
        this.mYaw = yaw;
        return this;
    }

    public float getRoll() {
        return mRoll;
    }

    public SharkPosition setRoll(float roll) {
        this.mRoll = roll;
        return this;
    }

    public float getX() {
        return mX;
    }

    public SharkPosition setX(float x) {
        this.mX = x;
        return this;
    }

    public float getY() {
        return mY;
    }

    public SharkPosition setY(float y) {
        this.mY = y;
        return this;
    }

    public float getZ() {
        return mZ;
    }

    public SharkPosition setZ(float z) {
        this.mZ = z;
        return this;
    }

    public float getAngleX() {
        return mAngleX;
    }

    /**
     * setAngleX
     * @param angleX in degree
     * @return self
     */
    public SharkPosition setAngleX(float angleX) {
        this.mAngleX = angleX;
        return this;
    }

    public float getAngleY() {
        return mAngleY;
    }

    /**
     * setAngleY
     * @param angleY in degree
     * @return self
     */
    public SharkPosition setAngleY(float angleY) {
        this.mAngleY = angleY;
        return this;
    }

    public float getAngleZ() {
        return mAngleZ;
    }

    /**
     * setAngleZ
     * @param angleZ in degree
     * @return self
     */
    public SharkPosition setAngleZ(float angleZ) {
        this.mAngleZ = angleZ;
        return this;
    }

    public static SharkPosition newInstance(){
        return new SharkPosition();
    }

    @Override
    public String toString() {
        return "SharkPosition{" +
                "mX=" + mX +
                ", mY=" + mY +
                ", mZ=" + mZ +
                ", mAngleX=" + mAngleX +
                ", mAngleY=" + mAngleY +
                ", mAngleZ=" + mAngleZ +
                ", mPitch=" + mPitch +
                ", mYaw=" + mYaw +
                ", mRoll=" + mRoll +
                '}';
    }

    private void update(){
        // model
        Matrix.setIdentityM(mCurrentRotation, 0);

        Matrix.rotateM(mCurrentRotation, 0, getAngleY(), 1.0f, 0.0f, 0.0f);
        Matrix.rotateM(mCurrentRotation, 0, getAngleX(), 0.0f, 1.0f, 0.0f);
        Matrix.rotateM(mCurrentRotation, 0, getAngleZ(), 0.0f, 0.0f, 1.0f);

        Matrix.translateM(mCurrentRotation, 0, getX(),getY(),getZ());

        Matrix.rotateM(mCurrentRotation, 0, getYaw(), 1.0f, 0.0f, 0.0f);
        Matrix.rotateM(mCurrentRotation, 0, getPitch(), 0.0f, 1.0f, 0.0f);
        Matrix.rotateM(mCurrentRotation, 0, getRoll(), 0.0f, 0.0f, 1.0f);
    }

    public float[] getMatrix() {
        update();
        return mCurrentRotation;
    }
}
