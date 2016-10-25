package com.asha.vrlib.plugins;

import android.content.Context;

import com.asha.vrlib.SharkDirector;
import com.asha.vrlib.SharkProgram;
import com.asha.vrlib.model.SharkMainPluginBuilder;
import com.asha.vrlib.model.SharkPosition;
import com.asha.vrlib.objects.SharkAbsObject3D;
import com.asha.vrlib.strategy.projection.ProjectionModeManager;
import com.asha.vrlib.texture.SharkTexture;

import static com.asha.vrlib.common.GLUtils.glCheck;

/**
 * Created by hzqiujiadi on 16/7/22.
 * hzqiujiadi ashqalcn@gmail.com
 */
public class SharkPanoramaPlugin extends SharkAbsPlugin {

    private SharkProgram mProgram;

    private SharkTexture mTexture;

    private ProjectionModeManager mProjectionModeManager;

    public SharkPanoramaPlugin(SharkMainPluginBuilder builder) {
        mTexture = builder.getTexture();
        mProgram = new SharkProgram(builder.getContentType());
        mProjectionModeManager = builder.getProjectionModeManager();
    }

    @Override
    public void init(Context context) {
        mProgram.build(context);
        mTexture.create();
    }

    @Override
    public void beforeRenderer(int totalWidth, int totalHeight) {

    }

    @Override
    public void renderer(int index, int width, int height, SharkDirector director) {

        SharkAbsObject3D object3D = mProjectionModeManager.getObject3D();
        // check obj3d
        if (object3D == null) return;

        // Update Projection
        director.updateViewport(width, height);

        // Set our per-vertex lighting program.
        mProgram.use();
        glCheck("SharkPanoramaPlugin mProgram use");

        mTexture.texture(mProgram);

        object3D.uploadVerticesBufferIfNeed(mProgram, index);

        object3D.uploadTexCoordinateBufferIfNeed(mProgram, index);

        // Pass in the combined matrix.
        director.shot(mProgram, getModelPosition());
        object3D.draw();

    }

    @Override
    public void destroy() {
        mTexture = null;
    }

    @Override
    protected SharkPosition getModelPosition() {
        return mProjectionModeManager.getModelPosition();
    }

    @Override
    protected boolean removable() {
        return false;
    }

}
