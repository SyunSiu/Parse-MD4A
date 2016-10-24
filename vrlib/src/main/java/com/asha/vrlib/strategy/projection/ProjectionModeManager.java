package com.asha.vrlib.strategy.projection;

import android.app.Activity;
import android.graphics.RectF;

import com.asha.vrlib.SharkDirector;
import com.asha.vrlib.SharkDirectorFactory;
import com.asha.vrlib.SharkLibrary;
import com.asha.vrlib.common.SharkDirection;
import com.asha.vrlib.common.SharkGLHandler;
import com.asha.vrlib.model.SharkMainPluginBuilder;
import com.asha.vrlib.model.SharkPosition;
import com.asha.vrlib.objects.SharkAbsObject3D;
import com.asha.vrlib.plugins.SharkAbsPlugin;
import com.asha.vrlib.strategy.ModeManager;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by hzqiujiadi on 16/6/25.
 * hzqiujiadi ashqalcn@gmail.com
 */
public class ProjectionModeManager extends ModeManager<AbsProjectionStrategy> implements IProjectionMode {

    public static int[] sModes = {SharkLibrary.PROJECTION_MODE_SPHERE, SharkLibrary.PROJECTION_MODE_DOME180, SharkLibrary.PROJECTION_MODE_DOME230};

    public static class Params{
        public RectF textureSize;
        public SharkDirectorFactory directorFactory;
        public SharkMainPluginBuilder mainPluginBuilder;
        public IMDProjectionFactory projectionFactory;
    }

    private List<SharkDirector> mDirectors = new LinkedList<>();

    private RectF mTextureSize;

    private SharkDirectorFactory mCustomDirectorFactory;

    private SharkAbsPlugin mMainPlugin;

    private SharkMainPluginBuilder mMainPluginBuilder;

    private IMDProjectionFactory mProjectionFactory;

    public ProjectionModeManager(int mode, SharkGLHandler handler, Params projectionManagerParams) {
        super(mode, handler);
        this.mTextureSize = projectionManagerParams.textureSize;
        this.mCustomDirectorFactory = projectionManagerParams.directorFactory;
        this.mProjectionFactory = projectionManagerParams.projectionFactory;
        this.mMainPluginBuilder = projectionManagerParams.mainPluginBuilder;
        this.mMainPluginBuilder.setProjectionModeManager(this);
    }

    public SharkAbsPlugin getMainPlugin() {
        if (mMainPlugin == null){
            mMainPlugin = getStrategy().buildMainPlugin(mMainPluginBuilder);
        }
        return mMainPlugin;
    }

    @Override
    public void switchMode(Activity activity, int mode) {
        super.switchMode(activity, mode);
    }

    @Override
    public void on(Activity activity) {
        super.on(activity);

        // destroy prev main plugin
        if( mMainPlugin != null){
            mMainPlugin.destroy();
            mMainPlugin = null;
        }

        mDirectors.clear();

        SharkDirectorFactory factory = getStrategy().hijackDirectorFactory();
        factory = factory == null ? mCustomDirectorFactory : factory;

        for (int i = 0; i < SharkLibrary.sMultiScreenSize; i++){
            mDirectors.add(factory.createDirector(i));
        }
    }

    @Override
    protected AbsProjectionStrategy createStrategy(int mode) {
        if (mProjectionFactory != null){
            AbsProjectionStrategy strategy = mProjectionFactory.createStrategy(mode);
            if (strategy != null) return strategy;
        }
        
        switch (mode){
            case SharkLibrary.PROJECTION_MODE_DOME180:
                return new DomeProjection(this.mTextureSize,180f,false);
            case SharkLibrary.PROJECTION_MODE_DOME230:
                return new DomeProjection(this.mTextureSize,230f,false);
            case SharkLibrary.PROJECTION_MODE_DOME180_UPPER:
                return new DomeProjection(this.mTextureSize,180f,true);
            case SharkLibrary.PROJECTION_MODE_DOME230_UPPER:
                return new DomeProjection(this.mTextureSize,230f,true);
            case SharkLibrary.PROJECTION_MODE_STEREO_SPHERE_HORIZONTAL:
                return new StereoSphereProjection(SharkDirection.HORIZONTAL);
            case SharkLibrary.PROJECTION_MODE_STEREO_SPHERE:
            case SharkLibrary.PROJECTION_MODE_STEREO_SPHERE_VERTICAL:
                return new StereoSphereProjection(SharkDirection.VERTICAL);
            case SharkLibrary.PROJECTION_MODE_PLANE_FIT:
            case SharkLibrary.PROJECTION_MODE_PLANE_CROP:
            case SharkLibrary.PROJECTION_MODE_PLANE_FULL:
                return PlaneProjection.create(mode,this.mTextureSize);
            case SharkLibrary.PROJECTION_MODE_MULTI_FISH_EYE_HORIZONTAL:
                return new MultiFishEyeProjection(1f, SharkDirection.HORIZONTAL);
            case SharkLibrary.PROJECTION_MODE_MULTI_FISH_EYE_VERTICAL:
                return new MultiFishEyeProjection(1f, SharkDirection.VERTICAL);
            case SharkLibrary.PROJECTION_MODE_SPHERE:
            default:
                return new SphereProjection();
        }
    }

    @Override
    protected int[] getModes() {
        return sModes;
    }

    @Override
    public SharkPosition getModelPosition() {
        return getStrategy().getModelPosition();
    }

    @Override
    public SharkAbsObject3D getObject3D() {
        return getStrategy().getObject3D();
    }

    public List<SharkDirector> getDirectors() {
        return mDirectors;
    }
}
