package com.google.firebase.udacity.thezechat.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.udacity.thezechat.adapters.ConversationAdapter;
import com.google.firebase.udacity.thezechat.models.Database;
import com.google.firebase.udacity.thezechat.models.FriendlyMessage;
import com.google.firebase.udacity.thezechat.models.User;
import com.google.firebase.udacity.thezechat.R;

import java.util.ArrayList;
import java.util.List;

public class MessagesActivity extends AppCompatActivity {

    private RecyclerView recyclerView ;
    private RecyclerView.LayoutManager layoutManager ;
    private ConversationAdapter contactAdapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        DatabaseReference dbUserConversation = Database.getInstance().getUserConversations(FirebaseAuth.getInstance().getUid());

        dbUserConversation.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> conversations = new ArrayList<>();
                for (DataSnapshot conversationSnapshot : dataSnapshot.getChildren())
                    conversations.add(conversationSnapshot.getValue(String.class));

                List<User> users  = new ArrayList<>();
                User user1 = new User();
                user1.setUid("3243dsfsdfdsf"); user1.setName("JCAQUE");
                User user2 = new User();
                user2.setUid("fbsdifbshbfsdfbjs"); user2.setName("GEOREGE");
                User user3 = new User();
                user3.setUid(FirebaseAuth.getInstance().getUid()); user3.setName(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                users.add(user1);
                users.add(user2);
                users.add(user3);

                FriendlyMessage message = new FriendlyMessage();
                message.setName(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                message.setText("ALLO");

                if(conversations.isEmpty()){
                    Database.getInstance().updateConversation("1", users, message );
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        recyclerView = findViewById(R.id.contact_recyclerview) ;
        layoutManager = new LinearLayoutManager(this) ;
        recyclerView.setLayoutManager(layoutManager) ;
        //contactAdapter = new ConversationAdapter(this,listeContact) ;
        recyclerView.setAdapter(contactAdapter);
    }
}
