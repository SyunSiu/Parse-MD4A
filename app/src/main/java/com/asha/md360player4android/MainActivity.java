package com.asha.md360player4android;


import android.content.ContentResolver;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


/**
 * 默认的主界面
 */
public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final EditText et = (EditText) findViewById(R.id.edit_text_url);

        SparseArray<String> data = new SparseArray<>();

        /** 播放裸帧数据 */
        data.put(data.size(), "play frame data");

        data.put(data.size(), "rtsp://218.204.223.237:554/live/1/66251FC11353191F/e7ooqwcfbqjoo80j.sdp");

        data.put(data.size(), "http://cache.utovr.com/201508270528174780.m3u8");
        data.put(data.size(), "http://10.240.131.39/vr/570624aae1c52.mp4");
        data.put(data.size(), "http://192.168.5.106/vr/570624aae1c52.mp4");

        data.put(data.size(), "file:///mnt/sdcard/vr/haha.mp4");
        data.put(data.size(), "file:///mnt/sdcard/vr/mine_app.mp4");

        data.put(data.size(), "file:///mnt/sdcard/vr/fisheye_square.jpg");
        data.put(data.size(), "file:///mnt/sdcard/vr/fish_1.jpg");
        data.put(data.size(), "file:///mnt/sdcard/vr/fish_2.jpg");
        data.put(data.size(), "file:///mnt/sdcard/vr/fish_1a.jpg");


        data.put(data.size(), getDrawableUri(R.drawable.bitmap360).toString());
        data.put(data.size(), getDrawableUri(R.drawable.texture).toString());
        data.put(data.size(), getDrawableUri(R.drawable.dome_pic).toString());
        data.put(data.size(), getDrawableUri(R.drawable.stereo).toString());
        data.put(data.size(), getDrawableUri(R.drawable.multifisheye).toString());
        data.put(data.size(), getDrawableUri(R.drawable.multifisheye2).toString());
        data.put(data.size(), getDrawableUri(R.drawable.fish2sphere180sx2).toString());
        data.put(data.size(), getDrawableUri(R.drawable.fish2sphere180s).toString());


//        data.put(data.size(), "file:///mnt/sdcard/vr/ch0_160701145544.ts");
//        data.put(data.size(), "file:///mnt/sdcard/vr/videos_s_4.mp4");
//        data.put(data.size(), "file:///mnt/sdcard/vr/28.mp4");
//        data.put(data.size(), "file:///mnt/sdcard/vr/halfdome.mp4");
//        data.put(data.size(), "file:///mnt/sdcard/vr/dome.mp4");
//        data.put(data.size(), "file:///mnt/sdcard/vr/stereo.mp4");
//        data.put(data.size(), "file:///mnt/sdcard/vr/video_31b451b7ca49710719b19d22e19d9e60.mp4");

//        data.put(data.size(), "file:///mnt/sdcard/vr/AGSK6416.jpg");
//        data.put(data.size(), "file:///mnt/sdcard/vr/IJUN2902.jpg");
//        data.put(data.size(), "file:///mnt/sdcard/vr/SUYZ2954.jpg");
//        data.put(data.size(), "file:///mnt/sdcard/vr/TEJD0097.jpg");
//        data.put(data.size(), "file:///mnt/sdcard/vr/WSGV6301.jpg");


        SpinnerHelper.with(this)
                .setData(data)
                .setClickHandler(new SpinnerHelper.ClickHandler() {
                    @Override
                    public void onSpinnerClicked(int index, int key, String value) {
                        et.setText(value);
                    }
                })
                .init(R.id.spinner_url);


        /**
         * 播放视频
         */
        findViewById(R.id.video_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = et.getText().toString();
                if (!TextUtils.isEmpty(url)) {
                    SharkBasePlayActivity.startVideo(MainActivity.this, Uri.parse(url));
                } else {
                    Toast.makeText(MainActivity.this, "empty url!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.bitmap_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = et.getText().toString();
                if (!TextUtils.isEmpty(url)) {
                    SharkBasePlayActivity.startBitmap(MainActivity.this, Uri.parse(url));
                } else {
                    Toast.makeText(MainActivity.this, "empty url!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.ijk_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = et.getText().toString();
                if (!TextUtils.isEmpty(url)) {
                    IjkPlayerDemoActivity.start(MainActivity.this, Uri.parse(url));
                } else {
                    Toast.makeText(MainActivity.this, "empty url!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    /**
     * 根据 资源文件id 获得文件的uri
     */
    private Uri getDrawableUri(@DrawableRes int resId) {
        Resources resources = getResources();
        return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + resources.getResourcePackageName(resId) + '/' + resources.getResourceTypeName(resId) + '/' + resources.getResourceEntryName(resId));
    }
}
