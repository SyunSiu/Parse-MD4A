package com.asha.md360player4android;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Surface;
import android.view.View;
import android.widget.Toast;

import com.asha.md360player4android.utils.Llog;
import com.asha.vrlib.MD360Director;
import com.asha.vrlib.MD360DirectorFactory;
import com.asha.vrlib.MDVRLibrary;
import com.asha.vrlib.model.BarrelDistortionConfig;
import com.asha.vrlib.model.MDPinchConfig;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * Created by hzqiujiadi on 16/4/5.
 * hzqiujiadi ashqalcn@gmail.com
 */
public class VideoPlayerActivity extends MD360PlayerActivity {


    private MediaPlayerWrapper mMediaPlayerWrapper = new MediaPlayerWrapper();
    private boolean playFrame;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mMediaPlayerWrapper.init();
        mMediaPlayerWrapper.setPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer mp) {
                cancelBusy();
                if (getVRLibrary() != null) {
                    getVRLibrary().notifyPlayerChanged();
                }
            }
        });
        mMediaPlayerWrapper.getPlayer().setOnErrorListener(new IMediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(IMediaPlayer mp, int what, int extra) {
                String error = String.format("Play Error what=%d extra=%d", what, extra);
                Toast.makeText(VideoPlayerActivity.this, error, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        mMediaPlayerWrapper.getPlayer().setOnVideoSizeChangedListener(new IMediaPlayer.OnVideoSizeChangedListener() {
            @Override
            public void onVideoSizeChanged(IMediaPlayer mp, int width, int height, int sar_num, int sar_den) {
                getVRLibrary().onTextureResize(width, height);
            }
        });


        String uriStr;
        Uri uri = getUri();
        if (uri != null) {
            uriStr = uri.toString();
            if (!TextUtils.isEmpty(uriStr)) {
                if (uriStr.equals("play frame data")) {
                    Llog.debug("播放裸帧数据");
                    playFrame = true;
                } else {
                    playFrame = false;
                    mMediaPlayerWrapper.openRemoteFile(uriStr);
                    mMediaPlayerWrapper.prepare();
                }
            }
        }


        /** 下一个按钮 */
        findViewById(R.id.control_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getVRLibrary().setPinchScale(7);
                mMediaPlayerWrapper.pause();
                mMediaPlayerWrapper.destroy();
                mMediaPlayerWrapper.init();
                mMediaPlayerWrapper.openRemoteFile("file:///mnt/sdcard/vr/video_31b451b7ca49710719b19d22e19d9e60.mp4");
                mMediaPlayerWrapper.prepare();
            }
        });

    }

    @Override
    protected MDVRLibrary createVRLibrary() {
        return MDVRLibrary.with(this)
                .displayMode(MDVRLibrary.DISPLAY_MODE_NORMAL)
                .interactiveMode(MDVRLibrary.INTERACTIVE_MODE_MOTION)
                .asVideo(new MDVRLibrary.IOnSurfaceReadyCallback() {
                    @Override
                    public void onSurfaceReady(Surface surface) {

                        Llog.debug("surfaceReady， 开始setSurface");
                        if (playFrame) {
                            // TODO: 2016/10/18  测试裸数据
                            playWithMediaCodec(surface);
                        } else {
                            mMediaPlayerWrapper.setSurface(surface);
                        }

                    }
                })
                .ifNotSupport(new MDVRLibrary.INotSupportCallback() {
                    @Override
                    public void onNotSupport(int mode) {
                        String tip = mode == MDVRLibrary.INTERACTIVE_MODE_MOTION
                                ? "onNotSupport:MOTION" : "onNotSupport:" + String.valueOf(mode);
                        Toast.makeText(VideoPlayerActivity.this, tip, Toast.LENGTH_SHORT).show();
                    }
                })
                .pinchConfig(new MDPinchConfig().setMin(1.0f).setMax(8.0f).setDefaultValue(0.1f))
                .pinchEnabled(true)
                .directorFactory(new MD360DirectorFactory() {
                    @Override
                    public MD360Director createDirector(int index) {
                        return MD360Director.builder().setPitch(90).build();
                    }
                })
                .projectionFactory(new CustomProjectionFactory())
                .barrelDistortionConfig(new BarrelDistortionConfig().setDefaultEnabled(false).setScale(0.95f))
                .build(R.id.gl_view);
    }


    private boolean isInitCodec;
    private boolean isDecoding = true;
    private boolean isFirstDecode = true;
    private boolean isPause;
    private MediaCodec mediaCodec;
    private String mimeType = "video/avc";
    private MediaFormat mediaFormat;
    private static InputStream mInputStream = null; // 读取文件

    String fileNameHead = "vvv";
    String fileNameEnd = ".h264";

    private long frameTimeInterval = 70;

    private void playWithMediaCodec(Surface surface) {
        Llog.info("进入硬解码");
        if (!isInitCodec) {
            initDecoder(surface);
            isInitCodec = true;
        }
        new CodecThread().start();
    }


    /**
     * 初始化解码器
     */
    protected void initDecoder(Surface surface) {
        Llog.info("初始化 解码器");
        try {
            mediaCodec = MediaCodec.createDecoderByType(mimeType);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        mediaFormat = MediaFormat.createVideoFormat(mimeType, 640, 360);
//        mediaFormat = MediaFormat.createVideoFormat(mimeType, 1280, 720);
        mediaFormat = MediaFormat.createVideoFormat(mimeType, 0, 0);

//        mediaFormat.setInteger(MediaFormat.KEY_FRAME_RATE, 15);   // 编码使用
//        mediaFormat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 5);   // 编码使用
//        mediaFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_Format16bitRGB565);
//        mediaFormat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 15);
//        mediaFormat.setInteger(MediaFormat.KEY_BIT_RATE, 12500);

//        mMediaCodec.configure(mediaFormat, mSurfaceView.getHolder().getSurface(), null, 0);
        mediaCodec.configure(mediaFormat, surface, null, 0);
        mediaCodec.start();
    }


    class CodecThread extends Thread {
        @Override
        public void run() {
            int bufLength;
            int loseCount = 0;
            int fileNameCount = 0;
            String filePath;
            byte[] buffer = new byte[100000];

            while (!Thread.interrupted() && isDecoding) {
                if (!isPause) {
                    try {
                        /** 改成从文件获取帧数据 */
                        filePath = Environment.getExternalStorageDirectory().getPath() + File.separator + "single2" + File.separator
//                                + fileNameHead + fileNameCount++ + fileNameEnd;
                                + fileNameHead + ++fileNameCount + fileNameEnd;
                        mInputStream = new BufferedInputStream(new FileInputStream(filePath));

                        int length = mInputStream.available();
                        if (0 < length) {
                            /** 读取文件 并 写入缓冲区 ;Read file and fill buffer。*/
                            int thisCount = mInputStream.read(buffer);

//                            Log.i("文件次序：", fileNameCount + "  ;读取单帧文件 长度thisCount = " + thisCount);
//                            Log.i("文件次序：", fileNameCount + "  ;读取单帧文件 长度thisCount = " + thisCount + " ;\n" +
//                                    " 内容(buffer长度=" + buffer.length + ")：" + Arrays.toString(buffer));


                            // TODO: 2016/10/18  读取裸数据
//                        buffer = uuDataSource.read();
                            if (0 >= (bufLength = buffer.length)) {
                                Llog.info("读取到的buffer为null");
                                loseCount++;
                                Llog.info("================buffer 的长度 = " + bufLength + " ;读取到buffer，取空：" + loseCount);
                                continue;
                            }
                            /** 填充帧数据缓冲 ，Fill frameBuffer */
                            onFrame(buffer, 0, bufLength);
                            Thread.sleep(frameTimeInterval);
                        }
                    } catch (Exception e) {
                        isDecoding = false;
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (null != mediaCodec) {
                mediaCodec.stop();      /** IllegalStateException */
                mediaCodec.release();
            }
        }
    }


    protected boolean onFrame(byte[] buf, int offset, int length) {
        /** 新版本的方法 */
//        int inputBufferIndex = mediaCodec.dequeueInputBuffer(-1);
//        ByteBuffer inputBuffer = mediaCodec.getInputBuffer(inputBufferIndex);
//        if (inputBuffer != null) {
//            inputBuffer.clear();
//            inputBuffer.put(buf, offset, length);
//            mediaCodec.queueInputBuffer(inputBufferIndex, 0, length, mCount * TIME_INTERNAL, 0);
//            mCount++;
//        } else {
//            return false;
//        }

//        Llog.error("=============硬件解码帧数据onFrame================");
        /** 老版本的方法 */
        int inputBufferIndex = mediaCodec.dequeueInputBuffer(0);
        ByteBuffer[] inputBuffers = mediaCodec.getInputBuffers();                   /** Get input buffer index */

        if (inputBufferIndex >= 0) {
            ByteBuffer inputBuffer = inputBuffers[inputBufferIndex];
            inputBuffer.clear();
            inputBuffer.put(buf, offset, length);
            mediaCodec.queueInputBuffer(inputBufferIndex, 0, length, System.currentTimeMillis(), 0);
        } else {
            return false;
        }

        MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();             /** Get output buffer index */
        int outputBufferIndex = mediaCodec.dequeueOutputBuffer(bufferInfo, 50);

        while (outputBufferIndex >= 0) {
            mediaCodec.releaseOutputBuffer(outputBufferIndex, true);
            outputBufferIndex = mediaCodec.dequeueOutputBuffer(bufferInfo, 0);

            if (isFirstDecode) {
                isFirstDecode = false;
                Message msg = new Message();
                msg.obj = true;
                Llog.info("硬解 第一次有输出 mediacodec ： decode success first frame");
                videoReadyHandler.sendMessage(msg);
            }
        }
//        Llog.info("硬解     onFrame end");
        return true;
    }


    private Handler videoReadyHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if ((boolean) msg.obj) {
                cancelBusy();
            }
        }
    };


    ////////////////


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMediaPlayerWrapper.destroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMediaPlayerWrapper.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMediaPlayerWrapper.resume();
    }
}
