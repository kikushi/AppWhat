package com.google.firebase.udacity.thezechat.activities;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.udacity.thezechat.R;
import com.google.firebase.udacity.thezechat.adapters.ConversationAdapter;
import com.google.firebase.udacity.thezechat.adapters.MessageAdapter;
import com.google.firebase.udacity.thezechat.models.Conversation;
import com.google.firebase.udacity.thezechat.models.Database;
import com.google.firebase.udacity.thezechat.models.FriendlyMessage;
import com.google.firebase.udacity.thezechat.models.User;

import java.util.ArrayList;
import java.util.List;

import static com.google.firebase.udacity.thezechat.activities.MainActivity.DEFAULT_MSG_LENGTH_LIMIT;

public class MessagesActivity extends AppCompatActivity {

    private static final String TAG = MessagesActivity.class.getSimpleName();

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference dbUserConversation;
    private ChildEventListener mChildEventListener;

    private Conversation mConversation = new Conversation();
    private List<User> mUsers = new ArrayList<>();
    private List<FriendlyMessage> mMessages = new ArrayList<>();

    private ListView mMessageListView;
    private MessageAdapter mMessageAdapter;
    private ProgressBar mProgressBar;
    private ImageButton mPhotoPickerButton;
    private EditText mMessageEditText;
    private Button mSendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);


        Bundle in = getIntent().getExtras();
        if (in != null) {
            mConversation = in.getParcelable(ConversationAdapter.KEY);
            mUsers = mConversation.getUsers();
            Log.e(TAG, "Size users is " + mUsers.size());
        } else {
            Log.e(TAG, "bundle is null");
        }

        mFirebaseAuth = FirebaseAuth.getInstance();

        // Initialize references to views
        mProgressBar = findViewById(R.id.messages_activity_progress_bar);
        mMessageListView = findViewById(R.id.messages_activity_list_view);
        mPhotoPickerButton = findViewById(R.id.messages_activity_photo_picker_button);
        mMessageEditText = findViewById(R.id.messages_activity_message_edit_text);
        mSendButton = findViewById(R.id.messages_activity_send_message);

        // Initialize progress bar
        mProgressBar.setVisibility(ProgressBar.INVISIBLE);

        mMessageAdapter = new MessageAdapter(this, R.layout.item_message, mMessages);
        mMessageListView.setAdapter(mMessageAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mFirebaseAuth != null) {
            mFirebaseUser = mFirebaseAuth.getCurrentUser();
            dbUserConversation = Database.getInstance().getUserConversation(mFirebaseAuth.getUid(), mConversation.getCid());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    protected void onStop() {
        super.onStop();
        detachDatabaseListener();
    }

    private void updateUI() {

        attachDatabaseListener();

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FriendlyMessage friendlyMessage = new FriendlyMessage(
                        mMessageEditText.getText().toString(),
                        mFirebaseAuth.getCurrentUser().getDisplayName(),
                        null
                );
                dbUserConversation.child(Database.MESSAGES).push().setValue(friendlyMessage);

                // Clear input box
                mMessageEditText.setText("");
            }
        });

        // Enable Send button when there's text to send
        mMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    mSendButton.setEnabled(true);
                } else {
                    mSendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        mMessageEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DEFAULT_MSG_LENGTH_LIMIT)});
    }

    private void attachDatabaseListener(){
        if(mChildEventListener == null){
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    FriendlyMessage friendlyMessage = dataSnapshot.getValue(FriendlyMessage.class);
                    mMessageAdapter.add(friendlyMessage);
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
            dbUserConversation.child(Database.MESSAGES).addChildEventListener(mChildEventListener);
        }

    }

    private void detachDatabaseListener(){
        if (mChildEventListener != null){
            dbUserConversation.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }

}
