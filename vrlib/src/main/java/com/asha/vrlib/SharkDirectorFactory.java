package com.asha.vrlib;

import android.opengl.Matrix;

/**
 * Created by hzqiujiadi on 16/3/13.
 * hzqiujiadi ashqalcn@gmail.com
 */
public abstract class SharkDirectorFactory {
    abstract public SharkDirector createDirector(int index);

    public static class DefaultImpl extends SharkDirectorFactory {
        @Override
        public SharkDirector createDirector(int index) {
            switch (index){
                // case 1:   return SharkDirector.builder().setEyeX(-2.0f).setLookX(-2.0f).build();
                default:  return SharkDirector.builder().build();
            }
        }
    }

    public static class OrthogonalImpl extends SharkDirectorFactory {

        @Override
        public SharkDirector createDirector(int index) {
            switch (index){
                default:  return new OrthogonalDirector(new SharkDirector.Builder());
            }
        }
    }


    private static class OrthogonalDirector extends SharkDirector {

        private OrthogonalDirector(Builder builder) {
            super(builder);
        }

        @Override
        public void setDeltaX(float mDeltaX) {
            // nop
        }

        @Override
        public void setDeltaY(float mDeltaY) {
            // nop
        }

        @Override
        public void updateSensorMatrix(float[] sensorMatrix) {
            // nop
        }

        @Override
        protected void updateProjection(){
            final float left = - 1f;
            final float right = 1f;
            final float bottom = - 1f;
            final float top = 1f;
            final float far = 500;
            Matrix.orthoM(getProjectionMatrix(), 0, left, right, bottom, top, getNear(), far);
        }
    }

}
