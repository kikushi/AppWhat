package com.google.firebase.udacity.thezechat.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jul_samson on 19-02-07.
 */

public class User implements Parcelable {

    public static class Metadata implements Parcelable {

        private long creationTimestamp;
        private long lastSigninTimestamp;

        public Metadata() {}

        protected Metadata(Parcel in) {
            creationTimestamp = in.readLong();
            lastSigninTimestamp = in.readLong();
        }

        public static final Creator<Metadata> CREATOR = new Creator<Metadata>() {
            @Override
            public Metadata createFromParcel(Parcel in) {
                return new Metadata(in);
            }

            @Override
            public Metadata[] newArray(int size) {
                return new Metadata[size];
            }
        };

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
            parcel.writeLong(creationTimestamp);
            parcel.writeLong(lastSigninTimestamp);
        }
    }

    private String uid;
    private String name;
    private String username;
    private String biography;
    private String email;
    private String phone;
    private boolean isVerify;
    private Metadata metadata;
    private List<String> images;


    public User() {
        //Empty constructor
        metadata = new Metadata();
        images = new ArrayList<>();
    }


    protected User(Parcel in) {
        uid = in.readString();
        name = in.readString();
        username = in.readString();
        biography = in.readString();
        email = in.readString();
        phone = in.readString();
        isVerify = in.readByte() != 0;
        //metadata = in.readValue();
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

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadatas) {
        this.metadata = metadatas;
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
        //parcel.writeValue(metadata);
    }


    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("name", name);
        result.put("username", username);
        result.put("biography", biography);
        result.put("email", email);
        result.put("phone", phone);
        result.put("isVerify", isVerify);
        result.put("metadata", metadata);
        result.put("images", images);
        return result;
    }
}
