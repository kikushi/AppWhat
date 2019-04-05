package com.google.firebase.udacity.thezechat.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.udacity.thezechat.adapters.ConversationAdapter;
import com.google.firebase.udacity.thezechat.models.Database;
import com.google.firebase.udacity.thezechat.utils.CustomImageView;
import com.google.firebase.udacity.thezechat.R;
import com.google.firebase.udacity.thezechat.models.User;
import com.google.firebase.udacity.thezechat.utils.GlideApp;
import com.google.firebase.udacity.thezechat.utils.IntentHandler;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class UserProfileActivity extends AppCompatActivity {

    private static final String TAG = UserProfileActivity.class.getSimpleName();

    private FirebaseAuth mFirebaseAuth;
    private ValueEventListener mValueEventListener;

    private DatabaseReference dbUser;
    private StorageReference stUserImages;

    private boolean isUserAuth = true;
    private boolean isEditing = false;
    private User mUser;


    private LinearLayout mLayout;
    private CustomImageView mProfileImageView;
    private TextView mUserName, mUserOnlineStatus;
    private Button   mUserBiography, mUserEmail, mUserPhone;

    private LinearLayout mEditLayout;
    private CustomImageView mEditProfileImageView;
    private EditText mEditUserName, mEditUserBiography, mEditUserEmail, mEditUserPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mFirebaseAuth = FirebaseAuth.getInstance();

        //  Fixed view
        mLayout = findViewById(R.id.user_profile_layout);
        mProfileImageView = findViewById(R.id.activity_user_profile_image);
        mUserName = findViewById(R.id.user_profile_name);
        mUserOnlineStatus = findViewById(R.id.user_profile_online);
        mUserBiography = findViewById(R.id.user_profile_biography);
        mUserEmail = findViewById(R.id.user_profile_email);
        mUserPhone = findViewById(R.id.user_profile_phone);
        mLayout.setVisibility(View.VISIBLE);

        // Edit View
        mEditLayout = findViewById(R.id.user_profile_layout_edit);
        mEditProfileImageView = findViewById(R.id.activity_user_profile_image_edit);
        mEditUserName = findViewById(R.id.user_profile_name_edit);
        mEditUserBiography = findViewById(R.id.user_profile_biography_edit);
        mEditUserEmail = findViewById(R.id.user_profile_email_edit);
        mEditUserPhone = findViewById(R.id.user_profile_phone_edit);
        mEditLayout.setVisibility(View.GONE);
        mEditProfileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "click");
                mEditProfileImageView.setEditImageDialog(UserProfileActivity.this, mUser);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mFirebaseAuth == null)
            return;
        dbUser = Database.getInstance().getUserDatabase(mFirebaseAuth.getUid());
        stUserImages = Database.getInstance().getUserImages(mFirebaseAuth.getUid());

        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            isUserAuth  = true;
            attachDatabaseListener();
        } else {
            isUserAuth = false;
            mUser = bundle.getParcelable(ConversationAdapter.USER);

            if (mUser == null) {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
                this.finish();
            }
            updateUI(mUser);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        detachDatabaseListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.user_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (!isUserAuth) {
            menu.findItem(R.id.user_menu_edit).setVisible(false);
            menu.findItem(R.id.user_menu_done).setVisible(false);
        }
        else {

            if (mLayout.getVisibility() == View.VISIBLE) {
                isEditing = false;
                menu.findItem(R.id.user_menu_edit).setVisible(true);
            } else
                menu.findItem(R.id.user_menu_edit).setVisible(false);

            if (mEditLayout.getVisibility() == View.VISIBLE) {
                isEditing = true;
                menu.findItem(R.id.user_menu_done).setVisible(true);
            } else
                menu.findItem(R.id.user_menu_done).setVisible(false);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.user_menu_edit:
                mLayout.setVisibility(View.GONE);
                mEditLayout.setVisibility(View.VISIBLE);
                invalidateOptionsMenu();
                return true;
            case R.id.user_menu_done:
                mLayout.setVisibility(View.VISIBLE);
                mEditLayout.setVisibility(View.GONE);
                updateDB();
                invalidateOptionsMenu();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult " + requestCode + " " + resultCode);

        if (resultCode == RESULT_OK && data != null) {
            Log.d(TAG, "Request Code: " + requestCode);
            switch (requestCode) {
                case IntentHandler.REQUEST_CODE_INTERNAL_STORAGE:
                    //customImageView.load(data.getData()).setTimeShared("14.00").isEditable(true).setImageSize();
                    Glide.with(this).load(data.getData()).into(mProfileImageView);
                    break;
                case IntentHandler.REQUEST_CODE_GALLERY:
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                        Database.getInstance().updateUserImageStorage(mUser, bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Glide.with(this).load(bitmap).into(mProfileImageView);
                    Glide.with(this).load(bitmap).into(mEditProfileImageView);
                    break;
                case IntentHandler.REQUEST_CODE_CAMERA:
                    //nothing
                    break;
                default:
                    Log.e(TAG, "Request code doesn't correspond to anything " + resultCode);
                    break;
            }

        } else {
            Log.e(TAG, "some error occured " + resultCode);
        }
    }

    private void updateUI(User user) {

        if (user.getImages() != null && !user.getImages().isEmpty()) {
            Map.Entry<String, Boolean> entry = user.getImages().entrySet().iterator().next();
            StorageReference currentImageRef = stUserImages.child(entry.getKey());
            Glide.with(this).load(currentImageRef).into(mProfileImageView);
            Glide.with(this).load(currentImageRef).into(mEditProfileImageView);
        }
        mUserName.setText(user.getName());
        mUserEmail.setText(user.getEmail());
        mUserPhone.setText(user.getPhone());
        mUserBiography.setText(user.getBiography());

        if (user.getMetadata() != null) {
            String time = new SimpleDateFormat(
                    "HH:mm",
                    Locale.CANADA_FRENCH)
                    .format(new Date(mFirebaseAuth.getCurrentUser().getMetadata().getLastSignInTimestamp()));
            mUserOnlineStatus.setText("Last time online " + time);
        }

        // While editing data
        mEditUserName.setText(user.getName());
        mEditUserEmail.setText(user.getEmail());
        mEditUserPhone.setText(user.getPhone());
        mEditUserBiography.setText(user.getBiography());

    }

    private void updateDB() {

        if (mEditUserName.getText().toString().isEmpty())
            return;
        if (mEditUserEmail.getText().toString().isEmpty())
            return;

        //Update User object;
        mUser.setName(mEditUserName.getText().toString());
        mUser.setEmail(mEditUserEmail.getText().toString());
        mUser.setBiography(mEditUserBiography.getText().toString());
        mUser.setPhone(mEditUserPhone.getText().toString());

        Database.getInstance().updateUserDatabase(mUser);

    }

    private void attachDatabaseListener(){
        if(mValueEventListener == null){
            mValueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    mUser = dataSnapshot.getValue(User.class);

                    if (mUser == null)
                        Database.getInstance().initUserDatabase();
                    else
                        updateUI(mUser);

                    /*if (mUser != null) {

                        Database.getInstance().updateUserDatabase(mUser);

                        Database.getInstance().updateUserSubscriptions(mUser.getUid(), "1");
                        Database.getInstance().updateUserSubscriptions(mUser.getUid(), "2");
                        Database.getInstance().updateUserSubscriptions(mUser.getUid(), "3");

                        if (mUser.getCurrentImageProfile() != null) {
                            profileImage = Database.getInstance()
                                    .getUserImages(mUser.getUid())
                                    .child(mUser.getCurrentImageProfile());

                            GlideApp.with(UserProfileActivity.this)
                                    .load(profileImage)
                                    .placeholder(R.drawable.image_user_default)
                                    .error(R.drawable.image_user_default)
                                    .into(mProfileImageView);
                        } else {
                            Log.e(TAG, "current image == null");
                        }

                    }*/
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
        }
        dbUser.addValueEventListener(mValueEventListener);
    }

    private void detachDatabaseListener() {
        if (mValueEventListener != null){
            dbUser.removeEventListener(mValueEventListener);
            mValueEventListener = null;
        }
    }
}
