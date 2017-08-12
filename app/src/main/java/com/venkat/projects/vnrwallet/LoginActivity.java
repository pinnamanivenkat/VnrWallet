package com.venkat.projects.vnrwallet;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.dd.CircularProgressButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by venkat on 27/7/17.
 */

public class LoginActivity extends AppCompatActivity {

    CircularProgressButton signIn,register;
    private String TAG = "CH:LoginActivity";
    public FirebaseAuth mAuth;
    public static FirebaseDatabase mFirebaseDatabase;
    public static DatabaseReference mFirebaseDatabaseReference,tempDataBaseReference;
    public static String mSessionKey;
    public FirebaseAuth.AuthStateListener mAuthListener;
    private ImageView vnr_logo;

    @Override
    protected void onCreate(Bundle loginActivity) {
        super.onCreate(loginActivity);
        setContentView(R.layout.login_activity);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseDatabaseReference = mFirebaseDatabase.getReference();
        mSessionKey = mFirebaseDatabaseReference.push().getKey();
        loadResources();

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.i("LoginActivity",mFirebaseDatabaseReference.getDatabase().toString());
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    finish();
                } else {
                    signIn.setVisibility(View.VISIBLE);
                    register.setVisibility(View.VISIBLE);
                    vnr_logo.setVisibility(View.VISIBLE);
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,SignIn.class));
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,Register.class));
            }
        });
    }
    protected void loadResources() {
        signIn = (CircularProgressButton) findViewById(R.id.sign_in);
        register = (CircularProgressButton) findViewById(R.id.register);
        vnr_logo = (ImageView) findViewById(R.id.vnr_logo);
    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
