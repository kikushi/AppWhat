package com.google.firebase.udacity.thezechat.activities;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.udacity.thezechat.adapters.ConversationAdapter;
import com.google.firebase.udacity.thezechat.models.Conversation;
import com.google.firebase.udacity.thezechat.models.Database;
import com.google.firebase.udacity.thezechat.models.FriendlyMessage;
import com.google.firebase.udacity.thezechat.models.User;
import com.google.firebase.udacity.thezechat.R;

import java.util.ArrayList;
import java.util.List;

public class ConversationsActivity extends AppCompatActivity {

    private static final String TAG = ConversationsActivity.class.getSimpleName();

    private List<Conversation> mConversations = new ArrayList<>();

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference  dbUserConversation;
    private ValueEventListener mValueEventListener;


    private RecyclerView mConversationRecyclerView ;
    private RecyclerView.LayoutManager layoutManager ;
    private RecyclerView.Adapter mConversationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversations);

        mFirebaseAuth = FirebaseAuth.getInstance();

        mConversationRecyclerView = findViewById(R.id.contact_recyclerview) ;

        LinearLayoutManager manager = new LinearLayoutManager(this) ;
        mConversationRecyclerView.setHasFixedSize(true);
        mConversationRecyclerView.setLayoutManager(manager);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mFirebaseAuth != null) {
            mFirebaseUser = mFirebaseAuth.getCurrentUser();
            dbUserConversation = Database.getInstance().getUserConversations(mFirebaseAuth.getUid());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        attachDatabaseListener();
    }

    @Override
    protected void onStop(){
        super.onStop();
        detachDatabaseListener();
    }

    private void test() {
        List<User> users  = new ArrayList<>();
        User user1 = new User();
        user1.setUid("3243dsfsdfdsf"); user1.setName("JCAQUE");
        User user3 = new User();
        user3.setUid(FirebaseAuth.getInstance().getUid()); user3.setName(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        users.add(user1);
        users.add(user3);

        FriendlyMessage message = new FriendlyMessage();
        message.setName(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        message.setText("ALLO");

        if(mConversations.isEmpty()){
            Database.getInstance().updateConversation(users, message);
        }

    }

    private void attachDatabaseListener(){
        if(mValueEventListener == null){


            mValueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.d(TAG + "Child", dataSnapshot.toString());
                    mConversations = new ArrayList<>();
                    for (DataSnapshot conversationSnapshot :  dataSnapshot.getChildren()) {

                        Log.d(TAG , conversationSnapshot.toString());
                        Conversation conversation = new Conversation();
                        conversation.setCid(conversationSnapshot.getKey());

                        List<User> users = new ArrayList<>();
                        for (DataSnapshot userSnapshot : conversationSnapshot.child(Database.USERS).getChildren())
                            users.add(userSnapshot.getValue(User.class));
                        conversation.setUsers(users);


                        List<FriendlyMessage> messages = new ArrayList<>();
                        for (DataSnapshot messageSnapshot : conversationSnapshot.child(Database.MESSAGES).getChildren())
                            messages.add(messageSnapshot.getValue(FriendlyMessage.class));
                        conversation.setMessages(messages);
                        Log.e(TAG, "Size message is " + messages.size());
                        mConversations.add(conversation);
                    }
                    mConversationAdapter = new ConversationAdapter(ConversationsActivity.this, mConversations);
                    mConversationRecyclerView.setAdapter(mConversationAdapter);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            /*mValueEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Log.d(TAG + "Child", dataSnapshot.toString());

                    Conversation conversation = new Conversation();

                    List<User> users = new ArrayList<>();
                    for (DataSnapshot userSnapshot : dataSnapshot.child(Database.USERS).getChildren())
                        users.add(userSnapshot.getValue(User.class));
                    conversation.setUsers(users);


                    List<FriendlyMessage> messages = new ArrayList<>();
                    for (DataSnapshot messageSnapshot : dataSnapshot.child(Database.MESSAGES).getChildren())
                        messages.add(messageSnapshot.getValue(FriendlyMessage.class));
                    conversation.setMessages(messages);

                    mConversations.add(0, conversation);
                    mConversationAdapter.notifyItemInserted(0);

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
            };*/
            dbUserConversation.addValueEventListener(mValueEventListener);
        }
    }

    private void detachDatabaseListener(){
        if (mValueEventListener != null){
            dbUserConversation.removeEventListener(mValueEventListener);
            mValueEventListener = null;
        }
    }

}
