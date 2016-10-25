package com.asha.vrlib.model;

import com.asha.vrlib.SharkLibrary;
import com.asha.vrlib.SharkRenderer;
import com.asha.vrlib.strategy.projection.ProjectionModeManager;
import com.asha.vrlib.texture.SharkTexture;

/**
 * Created by hzqiujiadi on 16/8/20.
 * hzqiujiadi ashqalcn@gmail.com
 */
public class SharkMainPluginBuilder {
    private SharkTexture texture;
    private int contentType = SharkLibrary.ContentType.DEFAULT;
    private ProjectionModeManager projectionModeManager;

    public SharkMainPluginBuilder() {
    }

    public SharkTexture getTexture() {
        return texture;
    }

    public int getContentType() {
        return contentType;
    }

    public ProjectionModeManager getProjectionModeManager() {
        return projectionModeManager;
    }


    public SharkMainPluginBuilder setContentType(int contentType) {
        this.contentType = contentType;
        return this;
    }

    /**
     * set surface{@link SharkTexture} to this render
     * @param texture {@link SharkTexture} surface may used by multiple render{@link SharkRenderer}
     * @return builder
     */
    public SharkMainPluginBuilder setTexture(SharkTexture texture){
        this.texture = texture;
        return this;
    }

    public SharkMainPluginBuilder setProjectionModeManager(ProjectionModeManager projectionModeManager) {
        this.projectionModeManager = projectionModeManager;
        return this;
    }
}
