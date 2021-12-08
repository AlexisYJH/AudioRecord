package com.example.record;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "Recorder";
    private static final String RECORD_FILE_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getPath()
            + "/sound.amr";

    private MediaRecorder mRecorder;
    private boolean isRecording;
    private File mSoundFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermissions();
        ImageView iv = findViewById(R.id.iv_record);
        iv.setOnClickListener(this);
    }

    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 0);
            }
        }
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "isRecording: " + isRecording);
        if (isRecording) {
            stopRecord();
        } else {
            try {
                mSoundFile = new File(RECORD_FILE_PATH);
                Log.d(TAG, "mSoundFile: " + mSoundFile.getAbsolutePath());
                //1. 创建 MediaRecorder 对象。
                mRecorder = new MediaRecorder();
                //2. 设置声音来源，一般传入 MediaRecorder. AudioSource.MIC参数指定录制来自麦克风的声音
                mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                //3. 设置录制的声音的输出格式（必须在设置声音编码格式之前设置）
                mRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
                //4. 设置声音编码的格式
                mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                //5. 设置音频文件的保存位置
                mRecorder.setOutputFile(mSoundFile.getAbsolutePath());
                //6. 准备录制
                mRecorder.prepare();
                //7. 开始录音
                mRecorder.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ImageView iv = (ImageView) v;
        isRecording = isRecording ? false : true;
        iv.setImageDrawable(getDrawable(isRecording ? R.drawable.stop : R.drawable.start));
    }

    private void stopRecord() {
        if (mSoundFile != null && mSoundFile.exists()) {
            //8. 停止录音
            mRecorder.stop();
            //9. 释放资源
            mRecorder.release();
            mRecorder = null;
        }
    }

    @Override
    protected void onDestroy() {
        stopRecord();
        super.onDestroy();
    }
}