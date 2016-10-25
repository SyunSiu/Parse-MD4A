package com.asha.md360player4android;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.asha.vrlib.SharkLibrary;
import com.asha.vrlib.model.SharkHotspotBuilder;
import com.asha.vrlib.model.SharkPosition;
import com.asha.vrlib.model.SharkRay;
import com.asha.vrlib.plugins.ISharkHotspot;
import com.asha.vrlib.plugins.SharkAbsPlugin;
import com.asha.vrlib.plugins.SharkHotspotPlugin;
import com.asha.vrlib.plugins.SharkWidgetPlugin;
import com.asha.vrlib.texture.SharkBitmapTexture;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;


/**
 * using SharkRenderer
 */
public abstract class SharkBasePlayActivity extends Activity {


    public static void startVideo(Context context, Uri uri) {
        start(context, uri, VideoBasePlayActivity.class);
    }


    public static void startBitmap(Context context, Uri uri) {
        start(context, uri, BitmapBasePlayActivity.class);
    }


    private static void start(Context context, Uri uri, Class<? extends Activity> clazz) {
        Intent i = new Intent(context, clazz);
        i.setData(uri);
        context.startActivity(i);
    }


    private static final SparseArray<String> sDisplayMode = new SparseArray<>();
    private static final SparseArray<String> sInteractiveMode = new SparseArray<>();
    private static final SparseArray<String> sProjectionMode = new SparseArray<>();
    private static final SparseArray<String> sAntiDistortion = new SparseArray<>();


    static {
        sDisplayMode.put(SharkLibrary.DISPLAY_MODE_NORMAL, "NORMAL");
        sDisplayMode.put(SharkLibrary.DISPLAY_MODE_GLASS, "GLASS");

        sInteractiveMode.put(SharkLibrary.INTERACTIVE_MODE_MOTION, "MOTION");
        sInteractiveMode.put(SharkLibrary.INTERACTIVE_MODE_TOUCH, "TOUCH");
        sInteractiveMode.put(SharkLibrary.INTERACTIVE_MODE_MOTION_WITH_TOUCH, "M & T");

        sProjectionMode.put(SharkLibrary.PROJECTION_MODE_SPHERE, "SPHERE");
        sProjectionMode.put(SharkLibrary.PROJECTION_MODE_DOME180, "DOME 180");
        sProjectionMode.put(SharkLibrary.PROJECTION_MODE_DOME230, "DOME 230");
        sProjectionMode.put(SharkLibrary.PROJECTION_MODE_DOME180_UPPER, "DOME 180 UPPER");
        sProjectionMode.put(SharkLibrary.PROJECTION_MODE_DOME230_UPPER, "DOME 230 UPPER");
        sProjectionMode.put(SharkLibrary.PROJECTION_MODE_STEREO_SPHERE_HORIZONTAL, "STEREO H SPHERE");
        sProjectionMode.put(SharkLibrary.PROJECTION_MODE_STEREO_SPHERE_VERTICAL, "STEREO V SPHERE");
        sProjectionMode.put(SharkLibrary.PROJECTION_MODE_PLANE_FIT, "PLANE FIT");
        sProjectionMode.put(SharkLibrary.PROJECTION_MODE_PLANE_CROP, "PLANE CROP");
        sProjectionMode.put(SharkLibrary.PROJECTION_MODE_PLANE_FULL, "PLANE FULL");
        sProjectionMode.put(SharkLibrary.PROJECTION_MODE_MULTI_FISH_EYE_HORIZONTAL, "MULTI FISH EYE HORIZONTAL");
        sProjectionMode.put(SharkLibrary.PROJECTION_MODE_MULTI_FISH_EYE_VERTICAL, "MULTI FISH EYE VERTICAL");
        sProjectionMode.put(CustomProjectionFactory.CUSTOM_PROJECTION_FISH_EYE_RADIUS_VERTICAL, "CUSTOM MULTI FISH EYE");

        sAntiDistortion.put(1, "ANTI-ENABLE");
        sAntiDistortion.put(0, "ANTI-DISABLE");
    }


    private SharkLibrary mVRLibrary;
    private SharkPosition logoPosition = SharkPosition.newInstance().setY(-8.0f).setYaw(-90.0f);
    private List<SharkAbsPlugin> plugins = new LinkedList<>();


    private SharkPosition[] positions = new SharkPosition[]{
            SharkPosition.newInstance().setZ(-8.0f).setYaw(-45.0f),
            SharkPosition.newInstance().setZ(-18.0f).setYaw(15.0f).setAngleX(15),
            SharkPosition.newInstance().setZ(-10.0f).setYaw(-10.0f).setAngleX(-15),
            SharkPosition.newInstance().setZ(-10.0f).setYaw(30.0f).setAngleX(30),
            SharkPosition.newInstance().setZ(-10.0f).setYaw(-30.0f).setAngleX(-30),
            SharkPosition.newInstance().setZ(-5.0f).setYaw(30.0f).setAngleX(60),
            SharkPosition.newInstance().setZ(-3.0f).setYaw(15.0f).setAngleX(-45),
            SharkPosition.newInstance().setZ(-3.0f).setYaw(15.0f).setAngleX(-45).setAngleY(45),
            SharkPosition.newInstance().setZ(-3.0f).setYaw(0.0f).setAngleX(90),
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_md_using_surface_view);
//        setContentView(R.layout.activity_md_using_texture_view);


        /**
         * 初始化3D库
         */
        mVRLibrary = createSharkLibrary();

        final List<View> hotspotPoints = new LinkedList<>();
        hotspotPoints.add(findViewById(R.id.hotspot_point1));
        hotspotPoints.add(findViewById(R.id.hotspot_point2));

        SpinnerHelper.with(this)
                .setData(sDisplayMode)
                .setDefault(mVRLibrary.getDisplayMode())
                .setClickHandler(new SpinnerHelper.ClickHandler() {
                    @Override
                    public void onSpinnerClicked(int index, int key, String value) {
                        mVRLibrary.switchDisplayMode(SharkBasePlayActivity.this, key);
                        int i = 0;
                        for (View point : hotspotPoints) {
                            point.setVisibility(i < mVRLibrary.getScreenSize() ? View.VISIBLE : View.GONE);
                            i++;
                        }
                    }
                })
                .init(R.id.spinner_display);

        SpinnerHelper.with(this)
                .setData(sInteractiveMode)
                .setDefault(mVRLibrary.getInteractiveMode())
                .setClickHandler(new SpinnerHelper.ClickHandler() {
                    @Override
                    public void onSpinnerClicked(int index, int key, String value) {
                        mVRLibrary.switchInteractiveMode(SharkBasePlayActivity.this, key);
                    }
                })
                .init(R.id.spinner_interactive);

        SpinnerHelper.with(this)
                .setData(sProjectionMode)
                .setDefault(mVRLibrary.getProjectionMode())
                .setClickHandler(new SpinnerHelper.ClickHandler() {
                    @Override
                    public void onSpinnerClicked(int index, int key, String value) {
                        mVRLibrary.switchProjectionMode(SharkBasePlayActivity.this, key);
                    }
                })
                .init(R.id.spinner_projection);

        SpinnerHelper.with(this)
                .setData(sAntiDistortion)
                .setDefault(mVRLibrary.isAntiDistortionEnabled() ? 1 : 0)
                .setClickHandler(new SpinnerHelper.ClickHandler() {
                    @Override
                    public void onSpinnerClicked(int index, int key, String value) {
                        mVRLibrary.setAntiDistortionEnabled(key != 0);
                    }
                })
                .init(R.id.spinner_distortion);

        findViewById(R.id.button_add_plugin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int index = (int) (Math.random() * 100) % positions.length;
                SharkPosition position = positions[index];
                SharkHotspotBuilder builder = SharkHotspotBuilder.create()
                        .size(4f, 4f)
                        .provider(0, new AndroidDrawableProvider(android.R.drawable.star_off))
                        .provider(1, new AndroidDrawableProvider(android.R.drawable.star_on))
                        .provider(10, new AndroidDrawableProvider(android.R.drawable.checkbox_off_background))
                        .provider(11, new AndroidDrawableProvider(android.R.drawable.checkbox_on_background))
                        .listenClick(new SharkLibrary.ITouchPickListener() {
                            @Override
                            public void onHotspotHit(ISharkHotspot hitHotspot, SharkRay ray) {
                                if (hitHotspot instanceof SharkWidgetPlugin) {
                                    SharkWidgetPlugin widgetPlugin = (SharkWidgetPlugin) hitHotspot;
                                    widgetPlugin.setChecked(!widgetPlugin.getChecked());
                                }
                            }
                        })
                        .title("star" + index)
                        .position(position)
                        .status(0, 1)
                        .checkedStatus(10, 11);

                SharkWidgetPlugin plugin = new SharkWidgetPlugin(builder);

                plugins.add(plugin);
                getVRLibrary().addPlugin(plugin);
                Toast.makeText(SharkBasePlayActivity.this, "add plugin position:" + position, Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.button_add_plugin_logo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharkHotspotBuilder builder = SharkHotspotBuilder.create()
                        .size(4f, 4f)
                        .provider(new SharkLibrary.IBitmapProvider() {
                            @Override
                            public void onProvideBitmap(SharkBitmapTexture.Callback callback) {
                                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.moredoo_logo);
                                callback.texture(bitmap);
                            }
                        })
                        .title("logo")
                        .position(logoPosition)
                        .listenClick(new SharkLibrary.ITouchPickListener() {
                            @Override
                            public void onHotspotHit(ISharkHotspot hitHotspot, SharkRay ray) {
                                Toast.makeText(SharkBasePlayActivity.this, "click logo", Toast.LENGTH_SHORT).show();
                            }
                        });
                SharkHotspotPlugin plugin = new SharkHotspotPlugin(builder);
                plugins.add(plugin);
                getVRLibrary().addPlugin(plugin);
                Toast.makeText(SharkBasePlayActivity.this, "add plugin logo", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.button_remove_plugin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (plugins.size() > 0) {
                    SharkAbsPlugin plugin = plugins.remove(plugins.size() - 1);
                    getVRLibrary().removePlugin(plugin);
                }
            }
        });

        findViewById(R.id.button_remove_plugins).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plugins.clear();
                getVRLibrary().removePlugins();
            }
        });

        final TextView hotspotText = (TextView) findViewById(R.id.hotspot_text);
        getVRLibrary().setEyePickChangedListener(new SharkLibrary.IEyePickListener() {
            @Override
            public void onHotspotHit(ISharkHotspot hotspot, long hitTimestamp) {
                String text = hotspot == null ? "nop" : String.format(Locale.CHINESE, "%s  %fs", hotspot.getTitle(), (System.currentTimeMillis() - hitTimestamp) / 1000.0f);
                hotspotText.setText(text);

                if (System.currentTimeMillis() - hitTimestamp > 5000) {
                    getVRLibrary().resetEyePick();
                }
            }
        });
    }

    abstract protected SharkLibrary createSharkLibrary();

    public SharkLibrary getVRLibrary() {
        return mVRLibrary;
    }


    @Override
    protected void onResume() {
        super.onResume();
        mVRLibrary.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mVRLibrary.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mVRLibrary.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mVRLibrary.onOrientationChanged(this);
    }

    protected Uri getUri() {
        Intent i = getIntent();
        if (i == null || i.getData() == null) {
            return null;
        }
        return i.getData();
    }

    public void cancelBusy() {
        findViewById(R.id.progress).setVisibility(View.GONE);
    }

    public void busy() {
        findViewById(R.id.progress).setVisibility(View.VISIBLE);
    }


    /**
     * 内部类
     */
    private class AndroidDrawableProvider implements SharkLibrary.IBitmapProvider {

        private int res;

        public AndroidDrawableProvider(int res) {
            this.res = res;
        }

        @Override
        public void onProvideBitmap(SharkBitmapTexture.Callback callback) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), this.res);
            callback.texture(bitmap);
        }
    }
}