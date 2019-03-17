package com.google.firebase.udacity.thezechat.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Conversation implements Parcelable {

    private String cid;
    List<User> users = new ArrayList<>();
    List<FriendlyMessage> messages = new ArrayList<>();

    public Conversation() {
    }

    protected Conversation(Parcel in) {
        cid = in.readString();
        users = in.createTypedArrayList(User.CREATOR);
        messages = in.createTypedArrayList(FriendlyMessage.CREATOR);
    }

    public static final Creator<Conversation> CREATOR = new Creator<Conversation>() {
        @Override
        public Conversation createFromParcel(Parcel in) {
            return new Conversation(in);
        }

        @Override
        public Conversation[] newArray(int size) {
            return new Conversation[size];
        }
    };

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<FriendlyMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<FriendlyMessage> messages) {
        this.messages = messages;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(cid);
        parcel.writeTypedList(users);
        parcel.writeTypedList(messages);
    }
}
