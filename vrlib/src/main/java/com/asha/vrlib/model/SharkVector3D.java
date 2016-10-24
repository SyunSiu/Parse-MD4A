package com.asha.vrlib.model;


import android.opengl.Matrix;


/**
 * 3D向量对象
 */
public class SharkVector3D {


    private float[] values;


    public SharkVector3D() {
        values = new float[4];
        values[3] = 1.0f;
    }


    public SharkVector3D setX(float x) {
        values[0] = x;
        return this;
    }


    public SharkVector3D setY(float y) {
        values[1] = y;
        return this;
    }


    public SharkVector3D setZ(float z) {
        values[2] = z;
        return this;
    }


    public float getX() {
        return values[0];
    }


    public float getY() {
        return values[1];
    }


    public float getZ() {
        return values[2];
    }


    public void multiplyMV(float[] mat) {
        Matrix.multiplyMV(values, 0, mat, 0, values, 0);
    }


    @Override
    public String toString() {
        return "SharkVector3D{" +
                "x=" + getX() +
                ", y=" + getY() +
                ", z=" + getZ() +
                '}';
    }
}
