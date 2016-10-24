package com.asha.vrlib.plugins;

import android.content.Context;
import android.graphics.RectF;
import android.opengl.GLES20;
import android.util.SparseArray;

import com.asha.vrlib.SharkDirector;
import com.asha.vrlib.SharkProgram;
import com.asha.vrlib.SharkLibrary;
import com.asha.vrlib.common.SharkUtil;
import com.asha.vrlib.model.SharkHotspotBuilder;
import com.asha.vrlib.model.SharkPosition;
import com.asha.vrlib.model.SharkRay;
import com.asha.vrlib.model.SharkVector3D;
import com.asha.vrlib.objects.SharkAbsObject3D;
import com.asha.vrlib.objects.SharkObject3DHelper;
import com.asha.vrlib.objects.SharkPlane;
import com.asha.vrlib.texture.SharkTexture;

import java.nio.FloatBuffer;
import java.util.LinkedList;
import java.util.List;

import static com.asha.vrlib.common.GLUtils.glCheck;

/**
 * Created by hzqiujiadi on 16/8/2.
 * hzqiujiadi ashqalcn@gmail.com
 */
public class SharkHotspotPlugin extends SharkAbsPlugin implements ISharkHotspot {

    private static final String TAG = "MDSimplePlugin";

    private SharkLibrary.ITouchPickListener clickListener;

    private SharkAbsObject3D object3D;

    private SharkProgram program;

    private SparseArray<SharkTexture> textures;

    private RectF size;

    private String title;

    private int mCurrentTextureKey = 0;

    public SharkHotspotPlugin(SharkHotspotBuilder builder) {
        textures = builder.textures;
        size = new RectF(0, 0, builder.width, builder.height);
        clickListener = builder.clickListener;
        setTitle(builder.title);
        setModelPosition(builder.position == null ? SharkPosition.sOriginalPosition : builder.position);
    }

    @Override
    public void init(Context context) {

        program = new SharkProgram(SharkLibrary.ContentType.BITMAP);
        program.build(context);

        for (int i = 0; i < textures.size(); i++) {
            textures.valueAt(i).create();
        }

        object3D = new SharkPlane(size);
        SharkObject3DHelper.loadObj(context,object3D);

    }

    @Override
    public void beforeRenderer(int totalWidth, int totalHeight) {

    }

    @Override
    public void renderer(int index, int width, int height, SharkDirector director) {

        SharkTexture texture = textures.get(mCurrentTextureKey);
        if (texture == null) return;

        texture.texture(program);
        if (texture.isReady()){
            // Update Projection
            director.updateViewport(width, height);

            // Set our per-vertex lighting program.
            program.use();
            glCheck("MDSimplePlugin mProgram use");

            object3D.uploadVerticesBufferIfNeed(program, index);

            object3D.uploadTexCoordinateBufferIfNeed(program, index);

            // Pass in the combined matrix.
            director.shot(program, getModelPosition());

            GLES20.glEnable(GLES20.GL_BLEND);
            GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);


            object3D.draw();
            GLES20.glDisable(GLES20.GL_BLEND);
        }

    }

    @Override
    public void destroy() {

    }

    @Override
    protected boolean removable() {
        return true;
    }

    @Override
    public boolean hit(SharkRay ray) {
        if (object3D == null || object3D.getVerticesBuffer(0) == null){
            return false;
        }

        SharkPosition position = getModelPosition();
        float[] model = position.getMatrix();

        List<SharkVector3D> points = new LinkedList<>();

        FloatBuffer buffer = object3D.getVerticesBuffer(0);
        int numPoints = buffer.capacity() / 3;

        for (int i = 0; i < numPoints; i++){
            SharkVector3D v = new SharkVector3D();
            v.setX(buffer.get(i * 3)).setY(buffer.get(i * 3 + 1)).setZ(buffer.get(i * 3 + 2));
            v.multiplyMV(model);
            points.add(v);
        }

        boolean hit = false;
        if (points.size() == 4){
            hit = SharkUtil.intersectTriangle(ray, points.get(0), points.get(1), points.get(2));
            hit |= SharkUtil.intersectTriangle(ray,points.get(1), points.get(2), points.get(3));
        }

        // Log.d(TAG,"Ray:" + ray);
        // Log.e(TAG,"MDSimplePlugin hit:" + hit);

        return hit;
    }

    @Override
    public void onEyeHitIn(long timestamp) {

    }

    @Override
    public void onEyeHitOut() {

    }

    @Override
    public void onTouchHit(SharkRay ray) {
        if (clickListener != null){
            clickListener.onHotspotHit(this, ray);
        }
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void useTexture(int key) {
        mCurrentTextureKey = key;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
