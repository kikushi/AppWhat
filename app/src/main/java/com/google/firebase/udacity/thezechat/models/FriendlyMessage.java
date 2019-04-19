/**
 * Copyright Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.firebase.udacity.thezechat.models;

import android.os.Parcel;
import android.os.Parcelable;

public class FriendlyMessage implements Parcelable {

    private String text;
    private String name;
    private String photoUrl;
    private String nameD;
    private String userID ;
    private String date ;
    private String time ;

    public FriendlyMessage() {
    }

    public FriendlyMessage(String text, String name, String photoUrl, String uid, String d, String t) {
        this.text = text;
        this.name = name;
        this.photoUrl = photoUrl;
        this.userID = uid ;
        this.date = d ;
        this.time = t ;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getUserID() { return this.userID; }

    public void setUserID(String uid) { this.userID = uid ; }

    public String getDate() { return date ; }

    public void setDate(String newDate){ this.date = newDate ; }

    public String getTime() { return time ; }

    public void setTime(String newTime) { this.time = newTime ; }


    protected FriendlyMessage(Parcel in) {
        text = in.readString();
        name = in.readString();
        photoUrl = in.readString();
        userID = in.readString();
        date = in.readString();
        time = in.readString() ;
    }

    public static final Creator<FriendlyMessage> CREATOR = new Creator<FriendlyMessage>() {
        @Override
        public FriendlyMessage createFromParcel(Parcel in) {
            return new FriendlyMessage(in);
        }

        @Override
        public FriendlyMessage[] newArray(int size) {
            return new FriendlyMessage[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(text);
        parcel.writeString(name);
        parcel.writeString(photoUrl);
        parcel.writeString(userID);
        parcel.writeString(date);
        parcel.writeString(time);
    }
}
