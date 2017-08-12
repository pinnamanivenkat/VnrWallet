package com.venkat.projects.vnrwallet;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.dd.CircularProgressButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by venkat on 27/7/17.
 */

public class SignIn extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private String TAG = "CH:SignIn";
    private final int DURATION = 4000;
    private Thread mSplashThread;
    private CircularProgressButton mSignIn;
    private EditText mUsernameEditText,mPasswordEditText;
    private DatabaseReference mDatabase;
    private FirebaseDatabase database;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        mAuth = FirebaseAuth.getInstance();
        loadResources();
        mSignIn.setIndeterminateProgressMode(true);
        mSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSignIn.getProgress() == 0) {
                    mSignIn.setProgress(50);
                } else if (mSignIn.getProgress() == 100) {
                    mSignIn.setProgress(0);
                } else {
                    mSignIn.setProgress(0);
                }
                Log.i(TAG,mUsernameEditText.getText().toString());
                Log.i(TAG,mPasswordEditText.getText().toString());
                try {
                    signIn(mUsernameEditText.getText().toString(),mPasswordEditText.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }
    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            mSignIn.setProgress(-1);
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(SignIn.this, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                        }
                        else {
                            mSignIn.setProgress(100);
                            Toast.makeText(SignIn.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                            mSplashThread = new Thread() {

                                @Override
                                public void run() {
                                    synchronized (this) {
                                        try {
                                            wait(DURATION);
                                            Log.d(TAG, "createUserWithEmail:success");
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        } finally {
                                            startActivity(new Intent(SignIn.this,MainActivity.class));
                                            finish();
                                        }
                                    }
                                }

                            };
                            mSplashThread.start();
                        }
                    }
                });

    }
    private void loadResources() {
        mSignIn = (CircularProgressButton) findViewById(R.id.signin);
        mUsernameEditText = (EditText) findViewById(R.id.username_edittext);
        mPasswordEditText = (EditText) findViewById(R.id.password_edittext);
    }
}
