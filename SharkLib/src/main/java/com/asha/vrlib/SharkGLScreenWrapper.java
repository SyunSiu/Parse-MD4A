package com.asha.vrlib;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.View;

import com.google.android.apps.muzei.render.GLTextureView;

/**
 * Created by hzqiujiadi on 16/7/11.
 * hzqiujiadi ashqalcn@gmail.com
 */
public abstract class SharkGLScreenWrapper {
    abstract public View getView();
    abstract public void setRenderer(GLSurfaceView.Renderer renderer);
    abstract public void init(Context context);
    abstract public void onResume();
    abstract public void onPause();

    public static SharkGLScreenWrapper wrap(GLSurfaceView glSurfaceView){
        return new SharkGLSurfaceViewImpl(glSurfaceView);
    }

    public static SharkGLScreenWrapper wrap(GLTextureView glTextureView){
        return new SharkGLTextureViewImpl(glTextureView);
    }

    private static class SharkGLTextureViewImpl extends SharkGLScreenWrapper {

        GLTextureView glTextureView;

        public SharkGLTextureViewImpl(GLTextureView glTextureView) {
            this.glTextureView = glTextureView;
        }

        @Override
        public View getView() {
            return glTextureView;
        }

        @Override
        public void setRenderer(GLSurfaceView.Renderer renderer) {
            glTextureView.setRenderer(renderer);
        }

        @Override
        public void init(Context context) {
            glTextureView.setEGLContextClientVersion(2);
            glTextureView.setPreserveEGLContextOnPause(true);
        }

        @Override
        public void onResume() {
            glTextureView.onResume();
        }

        @Override
        public void onPause() {
            glTextureView.onPause();
        }
    }

    private static class SharkGLSurfaceViewImpl extends SharkGLScreenWrapper {

        GLSurfaceView glSurfaceView;

        private SharkGLSurfaceViewImpl(GLSurfaceView glSurfaceView) {
            this.glSurfaceView = glSurfaceView;
        }

        @Override
        public View getView() {
            return glSurfaceView;
        }

        @Override
        public void setRenderer(GLSurfaceView.Renderer renderer) {
            glSurfaceView.setRenderer(renderer);
        }

        @Override
        public void init(Context context) {
            glSurfaceView.setEGLContextClientVersion(2);
            glSurfaceView.setPreserveEGLContextOnPause(true);
        }

        @Override
        public void onResume() {
            glSurfaceView.onResume();
        }

        @Override
        public void onPause() {
            glSurfaceView.onPause();
        }
    }
}
