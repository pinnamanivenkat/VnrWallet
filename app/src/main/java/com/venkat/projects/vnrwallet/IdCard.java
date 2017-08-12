package com.venkat.projects.vnrwallet;

import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by venkat on 1/8/17.
 */

public class IdCard extends AppCompatActivity {
    ImageView mFront,mBack;
    String mFilesPath;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idcard);
        mFilesPath = getFilesDir().toString();
        Log.i("IdCard",mFilesPath);
        mFront = (ImageView) findViewById(R.id.sample);
        Uri mFrontUri = Uri.parse(getFilesDir()+"/id_front.jpg");
        mFront.setImageURI(mFrontUri);
        mBack = (ImageView) findViewById(R.id.sample1);
        Log.i("IdCard",getFilesDir()+ "/id_back.jpg");
        Uri mBackUri = Uri.parse(getFilesDir()+ "/id_back.jpg");
        mBack.setImageURI(mBackUri);
    }

}