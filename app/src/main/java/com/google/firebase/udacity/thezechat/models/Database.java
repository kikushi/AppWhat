package com.google.firebase.udacity.thezechat.models;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Database  {

    private static String TAG = Database.class.getSimpleName();

    private static final String USERS = "users";
    private static final String CONVERSATIONS = "conversations";

    private static final String SUBSCRIPTIONS = "subscriptions";
    private static final String SUBSCRIBERS = "subscribers";
    private static final String IS_SUBSCRIBE_TO = "is-subscribe-to";


    private static Database ourInstance = null;

    private DatabaseReference rootDatabase;

    public static synchronized Database getInstance() {
        if(ourInstance == null)
            ourInstance = new Database();
        return ourInstance;
    }

    private Database() {
        rootDatabase = FirebaseDatabase.getInstance().getReference();
    }

    /*

            Access specific database

     */

    public DatabaseReference getRootDatabase() {
        return rootDatabase;
    }

    public DatabaseReference getUserDatabase(String uid) {
        Log.d(TAG, "getUserDatabase");
        return rootDatabase
                .child(USERS)
                .child(uid);
    }

    public DatabaseReference getConversationDatabase(String conversationId) {
        Log.d(TAG, "getConversationDatabase");
        return rootDatabase
                .child(CONVERSATIONS)
                .child(conversationId);
    }

    public DatabaseReference getUserSubscriptions(String uid) {
        Log.d(TAG, "getUserSubscriptions");
        return rootDatabase
                .child(USERS + "-" + SUBSCRIPTIONS)
                .child(uid);
    }

    /*

            Update firebase database

     */

    public void updateUserDatabase(User user) {
        Log.d(TAG, "updateUserDatabase");

        //  Transform user object to a map
        Map<String, Object> userValues = user.toMap();

        //  Put the user values where it must be updated
        Map<String, Object> childToUpdate = new HashMap<>();
        childToUpdate.put("/" + USERS + "/" + user.getUid(), userValues);

        //  Update Firebase
        rootDatabase.updateChildren(childToUpdate);
    }

    public void updateUserSubscriptions(final String uid, final String subid) {
        getUserSubscriptions(uid).child(IS_SUBSCRIBE_TO).child(subid).setValue(true);
        getUserSubscriptions(subid).child(SUBSCRIBERS).child(uid).setValue(true);
    }


}
