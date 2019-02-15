package com.google.firebase.udacity.thezechat.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jul_samson on 19-02-07.
 */

public class User implements Parcelable {

    private String uid;
    private String name;
    private String username;
    private String biography;
    private String email;
    private String phone;
    private boolean isVerify;
    private long creationTimestamp;
    private long lastSigninTimestamp;

    public User() {
        //Empty constructor
    }


    protected User(Parcel in) {
        uid = in.readString();
        name = in.readString();
        username = in.readString();
        biography = in.readString();
        email = in.readString();
        phone = in.readString();
        isVerify = in.readByte() != 0;
        creationTimestamp = in.readLong();
        lastSigninTimestamp = in.readLong();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    /**
     *
     *      Setters & Getters
     *
     */

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isVerify() {
        return isVerify;
    }

    public void setVerify(boolean verify) {
        isVerify = verify;
    }

    public long getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(long creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public long getLastSigninTimestamp() {
        return lastSigninTimestamp;
    }

    public void setLastSigninTimestamp(long lastSigninTimestamp) {
        this.lastSigninTimestamp = lastSigninTimestamp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(uid);
        parcel.writeString(name);
        parcel.writeString(username);
        parcel.writeString(biography);
        parcel.writeString(email);
        parcel.writeString(phone);
        parcel.writeByte((byte) (isVerify ? 1 : 0));
        parcel.writeLong(creationTimestamp);
        parcel.writeLong(lastSigninTimestamp);
    }
}
