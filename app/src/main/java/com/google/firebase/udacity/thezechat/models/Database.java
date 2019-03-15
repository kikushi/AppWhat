package com.google.firebase.udacity.thezechat.models;

import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
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

    private static final String IMAGES = "images";
    private static final String USERS_IMAGES = "users-images";


    private static Database ourInstance = null;

    private DatabaseReference rootDatabase;
    private StorageReference  rootStorage;

    public static synchronized Database getInstance() {
        if(ourInstance == null)
            ourInstance = new Database();
        return ourInstance;
    }

    private Database() {
        rootDatabase = FirebaseDatabase.getInstance().getReference();
        rootStorage  = FirebaseStorage.getInstance().getReference();
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

    public DatabaseReference getUserConversations(String uid) {
        return rootDatabase.
                child(USERS + "-" +CONVERSATIONS).
                child(uid);
    }

    public DatabaseReference getConversations(){
        Log.d(TAG, "getUserSubscriptions");
        return rootDatabase
                .child(CONVERSATIONS);
    }


    /*

            Update firebase database

     */

    public void createUserDatabase() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        try {

            if (firebaseUser == null)
                throw new NullPointerException("firebase user is null");

            User user = new User();
            user.setUid(firebaseUser.getUid());
            user.setName(firebaseUser.getDisplayName());
            user.setBiography("");
            user.setEmail(firebaseUser.getEmail());
            user.setVerify(firebaseUser.isEmailVerified());

            User.Metadata metadata = new User.Metadata();
            metadata.setCreationTimestamp(firebaseUser.getMetadata().getCreationTimestamp());
            metadata.setLastSigninTimestamp(firebaseUser.getMetadata().getLastSignInTimestamp());
            user.setMetadata(metadata);

            updateUserDatabase(user);

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void updateUserDatabase(User user) {
        Log.d(TAG, "updateUserDatabase");
        //getUserDatabase(user.getUid()).setValue(user);

        //  Transform user object to a map
        Map<String, Object> userValues = user.toMap();

        //  Put the user values where it must be updated
        Map<String, Object> childToUpdate = new HashMap<>();
        childToUpdate.put("/" + USERS + "/" + user.getUid(), userValues);

        //  Update Firebase
        rootDatabase.updateChildren(childToUpdate);
    }

    public void updateUserSubscriptions(String uid, String sid) {
        getUserSubscriptions(uid).child(IS_SUBSCRIBE_TO).child(sid).setValue(true);
        getUserSubscriptions(sid).child(SUBSCRIBERS).child(uid).setValue(true);
    }

    public  void updateConversation(String conversationId, List<User> users, FriendlyMessage message) {
        for (User user : users) {
            getUserConversations(user.getUid()).child(conversationId).child("messages").push().setValue(message);
        }
    }

    /*

            Access storage reference

    */

    public StorageReference getRootStorage() {
        return rootStorage;
    }

    public StorageReference getUserImages(String uid) {
        return rootStorage
                .child(USERS_IMAGES)
                .child(uid);
    }


    /*

            Update storage

     */

    public void updateUserImageStorage(final User user, Bitmap bitmap) {

        //List<String> userImages = user.getImages();
        //userImages.add(rootDatabase.push().getKey());
        //int lastImage = userImages.size() - 1;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 25, baos);
        final byte[] data = baos.toByteArray();

        final String imageId = "image" + rootDatabase.push().getKey();
        getUserImages(user.getUid()).child(imageId)
                .putBytes(data)
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.d(TAG, "Byte transferred: " + taskSnapshot.getBytesTransferred());
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        //Set images to false (because not use)
                        getUserDatabase(user.getUid()).child(IMAGES).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Log.d(TAG, dataSnapshot.toString());
                                Map<String,Boolean> images = (Map<String, Boolean>) dataSnapshot.getValue();
                                for (Map.Entry<String,Boolean> entry : images.entrySet()) {
                                    entry.setValue(false);
                                }
                                getUserDatabase(user.getUid())
                                        .child(IMAGES)
                                        .child(imageId)
                                        .setValue(true);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });



                        //updateUserDatabase(user);
                    }
                });

    }

}
