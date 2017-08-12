package com.venkat.projects.vnrwallet;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.coolbong.barcodegenerator.model.Code128;

//import com.venkat.projects.vnrwallet.MainActivity.mRollNo;

/**
 * Created by venkat on 8/8/17.
 */

public class qrcode extends AppCompatActivity {

    public String mRollNo;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qrcode);

        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.shared_pref_file),getApplicationContext().MODE_PRIVATE);
        String mDefaultValue=""+-1;
        mRollNo = sharedPref.getString(getString(R.string.rollno),mDefaultValue);
        Code128 code = new Code128(this);
        code.setData(mRollNo);
        Bitmap bitmap = code.getBitmap(700, 350);
        ImageView ivBarcode = (ImageView)findViewById(R.id.qrcodeview);
        ivBarcode.setImageBitmap(bitmap);
    }
}
