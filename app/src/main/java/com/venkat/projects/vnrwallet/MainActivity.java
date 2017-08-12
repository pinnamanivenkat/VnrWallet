package com.venkat.projects.vnrwallet;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mxn.soul.flowingdrawer_core.ElasticDrawer;
import com.mxn.soul.flowingdrawer_core.FlowingDrawer;


import java.io.File;
import java.io.IOException;

import static com.venkat.projects.vnrwallet.LoginActivity.mFirebaseDatabaseReference;
import static com.venkat.projects.vnrwallet.LoginActivity.mSessionKey;

/**
 * Created by venkat on 27/7/17.
 */

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "CH: MainActivity";
    FlowingDrawer mDrawer;
    public String mEmail;
    private StorageReference mStorageRef;
    public static String mRollNo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("MainActivity: onCreate()");
        Log.i("MainActivity",mSessionKey);

        mStorageRef = FirebaseStorage.getInstance().getReference();

        getUser();
        if(mEmail!=null)
            System.out.println("Email not null");
        setupToolbar();
        setupMenu();

        mDrawer = (FlowingDrawer) findViewById(R.id.drawerlayout);
        mDrawer.setTouchMode(ElasticDrawer.TOUCH_MODE_BEZEL);
        mDrawer.setOnDrawerStateChangeListener(new ElasticDrawer.OnDrawerStateChangeListener() {
            @Override
            public void onDrawerStateChange(int oldState, int newState) {
                if (newState == ElasticDrawer.STATE_CLOSED) {
                    Log.i(TAG, "Drawer STATE_CLOSED");
                }
            }

            @Override
            public void onDrawerSlide(float openRatio, int offsetPixels) {
                Log.i(TAG, "openRatio=" + openRatio + " ,offsetPixels=" + offsetPixels);
            }
        });
    }

    private void getUser() {
        Query mQuery = mFirebaseDatabaseReference
                .child("users")
                .orderByChild("Email")
                .equalTo(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        mQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot temp:dataSnapshot.getChildren()) {
                    try {
                        Context mContext = getApplicationContext();
                        SharedPreferences mSharedPreferences =
                                mContext.getSharedPreferences(getString(R.string.shared_pref_file),Context.MODE_PRIVATE);
                        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
                        mEditor.putString(getString(R.string.rollno),(String)temp.child("RollNo").getValue());
                        mEditor.commit();
                        downloadData((String)temp.child("RollNo").getValue());
                    } catch (Exception e) {
                        System.out.println("Exception caught");
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void setupMenu() {
        FragmentManager fm = getSupportFragmentManager();
        MenuListFragment mMenuFragment = (MenuListFragment) fm.findFragmentById(R.id.id_container_menu);
        if (mMenuFragment == null) {
            mMenuFragment = new MenuListFragment();
            fm.beginTransaction().add(R.id.id_container_menu, mMenuFragment).commit();
        }
    }
    private void downloadData(String mRollNo) {
        this.mRollNo = mRollNo;
        System.out.println(this.mRollNo);
        if(downloadFileFromServer(mRollNo,"id_back.jpg"))
            System.out.println("Download file successful");
        else
            System.out.println("Download file failed");
        if(downloadFileFromServer(mRollNo,"id_front.jpg"))
            System.out.println("Download file successful");
    }

    private boolean downloadFileFromServer(String mRollNo,String type) {
        StorageReference riversRef = mStorageRef.child(mRollNo+"/"+mRollNo+"_"+type);
        System.out.println(getFilesDir()+"/"+type);
        final File localFile = new File(getFilesDir()+"/"+type);
        if(localFile.exists())
            return true;
        riversRef.getFile(localFile)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(getApplicationContext(),"Failed to download picture",Toast.LENGTH_SHORT).show();
                boolean deleted = localFile.delete();
                if(deleted) {
                    System.out.println("Deleted the file");
                }
            }
        });
        return false;
    }

    protected void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu_white);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawer.toggleMenu();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isMenuVisible()) {
            mDrawer.closeMenu();
        } else {
            super.onBackPressed();
        }
    }
}
