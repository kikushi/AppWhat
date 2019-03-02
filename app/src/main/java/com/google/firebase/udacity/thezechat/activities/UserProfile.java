package com.google.firebase.udacity.thezechat.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.udacity.thezechat.utils.CustomImageView;
import com.google.firebase.udacity.thezechat.R;
import com.google.firebase.udacity.thezechat.models.User;
import com.google.firebase.udacity.thezechat.utils.IntentHandler;

import java.util.ArrayList;
import java.util.List;

public class UserProfile extends AppCompatActivity {

    private static final String TAG = UserProfile.class.getSimpleName();

    private CustomImageView mProfileImageView;
    private TextView mUserName;
    private TextView mUserBiography;
    private Button   mUserEmail;
    private Button   mUserEditProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser fbUser = auth.getCurrentUser();


        mProfileImageView = findViewById(R.id.activity_user_profile_image);
        mUserName = findViewById(R.id.user_profile_name);
        mUserBiography = findViewById(R.id.user_profile_biography);
        mUserEmail = findViewById(R.id.user_profile_email);
        mUserEditProfile = findViewById(R.id.user_profile_edit_account);

        mProfileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProfileImageView.setEditImageDialog(UserProfile.this);
            }
        });

        List<Integer> drawables = new ArrayList<>();
        drawables.add(R.drawable.ic_camera);
        drawables.add(R.drawable.ic_gallery);
        drawables.add(R.drawable.ic_storage);
        drawables.add(R.drawable.ic_delete);

        //mProfileImageView.setHorizontalIconDialog(drawables);

        if(fbUser != null) {
            User.Metadata metadata = new User.Metadata();
            metadata.setCreationTimestamp(fbUser.getMetadata().getCreationTimestamp());
            metadata.setLastSigninTimestamp(fbUser.getMetadata().getLastSignInTimestamp());
            mUserName.setText(fbUser.getDisplayName());
            mUserEmail.setText(fbUser.getEmail());
            mUserBiography.setText(fbUser.getUid());


            User user = new User();
            user.setUid(fbUser.getUid());
            user.setName(fbUser.getDisplayName());
            user.setBiography("");
            user.setEmail(fbUser.getEmail());
            user.setVerify(fbUser.isEmailVerified());
            user.setMetadata(metadata);

            FirebaseDatabase.getInstance().getReference()
                    .child("users")
                    .child(user.getUid())
                    .setValue(user);
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult " + requestCode + " " + resultCode);

        if (resultCode == RESULT_OK && data != null) {
            Log.d(TAG, "Request Code: " + requestCode);
            /*try {
                Bitmap image = getBitmapFromUri(data.getData());
                Glide.with(thisActivity).load(image).into(mTestImage);

            } catch (IOException e) {
                e.printStackTrace();
            }*/
            switch (requestCode) {
                case IntentHandler.REQUEST_CODE_INTERNAL_STORAGE:
                    //customImageView.load(data.getData()).setTimeShared("14.00").isEditable(true).setImageSize();
                    Glide.with(this).load(data.getData()).into(mProfileImageView);
                    break;
                case IntentHandler.REQUEST_CODE_GALLERY:
                    //Glide.with(thisActivity).load(data.getData()).into(mTestImage);
                    Glide.with(this).load(data.getData()).into(mProfileImageView);
                    //customImageView.load(data.getData()).setTimeShared("14:00").isEditable(true).setImageSize();
                    //Glide.with(thisActivity).load(data.getData()).into(mTestImage);
                    break;
                case IntentHandler.REQUEST_CODE_CAMERA:


                    Glide.with(this).load(data.getData()).into(mProfileImageView);
                    break;
                default:
                    Log.e(TAG, "Request code doesn't correspond to anything " + resultCode);
                    break;
            }

        } else {
            Log.e(TAG, "some error occured " + resultCode);
        }
    }
}
