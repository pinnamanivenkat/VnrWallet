package com.venkat.projects.vnrwallet;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {
    public String Email;
    public String RollNo;
    public String PhoneNumber;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String mEmail,String mRollNo,String mPhoneNumber) {
        Email = mEmail;
        RollNo = mRollNo;
        PhoneNumber = mPhoneNumber;
    }
}