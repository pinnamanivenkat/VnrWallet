package com.venkat.projects.vnrwallet;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.ybq.android.spinkit.style.Wave;


public class SplashScreen extends AppCompatActivity {

    private final int DURATION = 1000;
    private Thread mSplashThread;
    private TextView mTextView;
    private ImageView vnr_logo;
    ProgressBar mProgressBar;
    Animation slide_up;

    private static final int REQUEST_PERM = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        loadResources();
        getAnimation();
        final int write_permission_check = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        final int read_permission_check = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);

        mProgressBar.setIndeterminateDrawable(new Wave());

        mSplashThread = new Thread() {

            @Override
            public void run() {
                synchronized (this) {
                    try {
                        vnr_logo.startAnimation(slide_up);
                        mProgressBar.setAnimation(slide_up);
                        mTextView.setAnimation(slide_up);
                        wait(DURATION);
                        if(write_permission_check != PackageManager.PERMISSION_GRANTED && read_permission_check== PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(SplashScreen.this,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    REQUEST_PERM);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        startActivity(new Intent(SplashScreen.this,LoginActivity.class));
                        finish();
                    }
                }
            }

        };
        mSplashThread.start();
    }
    private void loadResources() {
        mProgressBar = (ProgressBar)findViewById(R.id.progress);
        vnr_logo = (ImageView) findViewById(R.id.vnr_logo);
        mTextView = (TextView) findViewById(R.id.loading);
    }
    private void getAnimation() {
        slide_up = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if(requestCode == REQUEST_PERM) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            } else {
                finish();
            }
        }
    }
}