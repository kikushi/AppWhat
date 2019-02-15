package com.google.firebase.udacity.thezechat.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.udacity.thezechat.R;
import com.google.firebase.udacity.thezechat.models.User;

public class UserProfile extends AppCompatActivity {

    private static final String TAG = UserProfile.class.getSimpleName();

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

        mUserName = findViewById(R.id.user_profile_name);
        mUserBiography = findViewById(R.id.user_profile_biography);
        mUserEmail = findViewById(R.id.user_profile_email);
        mUserEditProfile = findViewById(R.id.user_profile_edit_account);

        if(fbUser != null) {
            mUserName.setText(fbUser.getDisplayName());
            mUserEmail.setText(fbUser.getEmail());
            mUserBiography.setText(fbUser.getUid());

            User user = new User();
            user.setUid(fbUser.getUid());
            user.setName(fbUser.getDisplayName());
            user.setBiography("");
            user.setEmail(fbUser.getEmail());
            user.setVerify(fbUser.isEmailVerified());
            user.setCreationTimestamp(fbUser.getMetadata().getCreationTimestamp());
            user.setLastSigninTimestamp(fbUser.getMetadata().getLastSignInTimestamp());

            FirebaseDatabase.getInstance().getReference()
                    .child("users")
                    .child(user.getUid())
                    .setValue(user);
        }


    }
}
