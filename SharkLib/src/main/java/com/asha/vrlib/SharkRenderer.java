package com.asha.vrlib;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.asha.vrlib.common.FpsUtil;
import com.asha.vrlib.common.SharkGLHandler;
import com.asha.vrlib.plugins.SharkAbsLinePipe;
import com.asha.vrlib.plugins.SharkAbsPlugin;
import com.asha.vrlib.plugins.SharkBarrelDistortionLinePipe;
import com.asha.vrlib.plugins.SharkPluginManager;
import com.asha.vrlib.strategy.display.DisplayModeManager;
import com.asha.vrlib.strategy.projection.ProjectionModeManager;

import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static com.asha.vrlib.common.GLUtils.glCheck;

/**
 * Created by hzqiujiadi on 16/1/22.
 * hzqiujiadi ashqalcn@gmail.com
 *
 * @see Builder
 * @see #with(Context)
 */
public class SharkRenderer implements GLSurfaceView.Renderer {

	private static final String TAG = "SharkRenderer";
	private DisplayModeManager mDisplayModeManager;
	private ProjectionModeManager mProjectionModeManager;
	private SharkPluginManager mPluginManager;
	private SharkAbsLinePipe mMainLinePipe;
	private SharkGLHandler mGLHandler;
	private FpsUtil mFpsUtil = new FpsUtil();
	private int mWidth;
	private int mHeight;

	// private MDBarrelDistortionPlugin mBarrelDistortionPlugin;

	// final
	private final Context mContext;

	private SharkRenderer(Builder params){
		mContext = params.context;
		mDisplayModeManager = params.displayModeManager;
		mProjectionModeManager = params.projectionModeManager;
		mPluginManager = params.pluginManager;
		mGLHandler = params.glHandler;

		mMainLinePipe = new SharkBarrelDistortionLinePipe(mDisplayModeManager);
	}

	@Override
	public void onSurfaceCreated(GL10 glUnused, EGLConfig config){
		// set the background clear color to black.
		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		
		// use culling to remove back faces.
		GLES20.glEnable(GLES20.GL_CULL_FACE);
		
		// enable depth testing
		// GLES20.glEnable(GLES20.GL_DEPTH_TEST);
	}

	@Override
	public void onSurfaceChanged(GL10 glUnused, int width, int height){
		this.mWidth = width;
		this.mHeight = height;

		mGLHandler.dealMessage();
	}

	@Override
	public void onDrawFrame(GL10 glUnused){
		// gl thread
		mGLHandler.dealMessage();

		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
		glCheck("SharkRenderer onDrawFrame 1");

		int size = mDisplayModeManager.getVisibleSize();

		int width = (int) (this.mWidth * 1.0f / size);
		int height = mHeight;

		// take over
		mMainLinePipe.setup(mContext);
		mMainLinePipe.takeOver(mWidth,mHeight,size);

		List<SharkDirector> directors = mProjectionModeManager.getDirectors();

		// main plugin
		SharkAbsPlugin mainPlugin = mProjectionModeManager.getMainPlugin();
		if (mainPlugin != null){
			mainPlugin.setup(mContext);
			mainPlugin.beforeRenderer(this.mWidth, this.mHeight);
		}

		for (SharkAbsPlugin plugin : mPluginManager.getPlugins()) {
			plugin.setup(mContext);
			plugin.beforeRenderer(this.mWidth, this.mHeight);
		}

		for (int i = 0; i < size; i++){
			if (i >= directors.size()) break;

			SharkDirector director = directors.get(i);
			GLES20.glViewport(width * i, 0, width, height);
			GLES20.glEnable(GLES20.GL_SCISSOR_TEST);
			GLES20.glScissor(width * i, 0, width, height);

			if (mainPlugin != null){
				mainPlugin.renderer(i, width, height, director);
			}

			for (SharkAbsPlugin plugin : mPluginManager.getPlugins()) {
				plugin.renderer(i, width, height, director);
			}

			GLES20.glDisable(GLES20.GL_SCISSOR_TEST);
		}

		mMainLinePipe.commit(mWidth,mHeight,size);
		// mFpsUtil.step();
	}

	public static Builder with(Context context) {
		Builder builder = new Builder();
		builder.context = context;
		return builder;
	}

	public static class Builder{
		private Context context;
		private DisplayModeManager displayModeManager;
		private ProjectionModeManager projectionModeManager;
		private SharkGLHandler glHandler;
		private SharkPluginManager pluginManager;

		private Builder() {
		}

		public SharkRenderer build(){
			return new SharkRenderer(this);
		}

		public Builder setGLHandler(SharkGLHandler glHandler){
			this.glHandler = glHandler;
			return this;
		}

		public Builder setPluginManager(SharkPluginManager pluginManager) {
			this.pluginManager = pluginManager;
			return this;
		}

		public Builder setDisplayModeManager(DisplayModeManager displayModeManager) {
			this.displayModeManager = displayModeManager;
			return this;
		}

		public Builder setProjectionModeManager(ProjectionModeManager projectionModeManager) {
			this.projectionModeManager = projectionModeManager;
			return this;
		}
	}
}
