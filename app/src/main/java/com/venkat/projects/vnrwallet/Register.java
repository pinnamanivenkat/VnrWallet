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

import static com.venkat.projects.vnrwallet.LoginActivity.mFirebaseDatabaseReference;
import static com.venkat.projects.vnrwallet.LoginActivity.mSessionKey;

/**
 * Created by venkat on 27/7/17.
 */

public class Register extends AppCompatActivity {

    private static final String TAG = "CH: register";
    CircularProgressButton mRegisterButton;
    private FirebaseAuth mAuth;
    private EditText mEmail,mPassword,mRollNo,mContactNo;
    private final int DURATION = 2000;
    private Thread mSplashThread;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Log.i("Register",mSessionKey);
        loadResources();
        mRegisterButton.setIndeterminateProgressMode(true);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mRegisterButton.getProgress() == 0) {
                    mRegisterButton.setProgress(50);
                } else if (mRegisterButton.getProgress() == 100) {
                    mRegisterButton.setProgress(0);
                } else {
                    mRegisterButton.setProgress(0);
                }
                try {
                    createAccount(mEmail.getText().toString(),mPassword.getText().toString());
                } catch (Exception e) {
                    mRegisterButton.setProgress(-1);
                    e.printStackTrace();
                }
            }
        });
    }
    private void loadResources() {
        mAuth = FirebaseAuth.getInstance();
        mRegisterButton = (CircularProgressButton) findViewById(R.id.register_button);
        mEmail = (EditText) findViewById(R.id.register_email);
        mPassword = (EditText) findViewById(R.id.register_pass);
        mRollNo = (EditText) findViewById(R.id.register_rno);
        mContactNo = (EditText) findViewById(R.id.register_contact);
    }
    private void createAccount(String email,String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            mRegisterButton.setProgress(-1);
                            Toast.makeText(Register.this, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                        }
                        else {
                            mRegisterButton.setProgress(100);
                            User user= new User(
                                    mEmail.getText().toString(),
                                    mRollNo.getText().toString(),
                                    mContactNo.getText().toString()
                            );
                            mFirebaseDatabaseReference.child("users").child(mSessionKey).child("Email").setValue( mEmail.getText().toString());
                            mFirebaseDatabaseReference.child("users").child(mSessionKey).child("RollNo").setValue( mRollNo.getText().toString());
                            mFirebaseDatabaseReference.child("users").child(mSessionKey).child("PhoneNumber").setValue( mContactNo.getText().toString());
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
                                            FirebaseAuth.getInstance().signOut();
                                            startActivity(new Intent(Register.this,SignIn.class));
                                            finish();
                                        }
                                    }
                                }

                            };
                            mSplashThread.start();
                            Toast.makeText(Register.this, "Successfully registered", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}


