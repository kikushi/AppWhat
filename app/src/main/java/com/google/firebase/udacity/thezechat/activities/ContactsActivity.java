package com.google.firebase.udacity.thezechat.activities;

import android.support.v7.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.udacity.thezechat.R;
import com.google.firebase.udacity.thezechat.adapters.ContactAdaptater;
import com.google.firebase.udacity.thezechat.adapters.ConversationAdapter;
import com.google.firebase.udacity.thezechat.models.Database;
import com.google.firebase.udacity.thezechat.models.User;

import java.util.ArrayList;
import java.util.List;

public class ContactsActivity extends AppCompatActivity {

    private static final String TAG = ContactsActivity.class.getSimpleName();

    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference dbUsers;
    private ValueEventListener mValueEventListener;

    private RecyclerView mContactRecyclerView ;
    private RecyclerView.LayoutManager layoutManager ;
    private RecyclerView.Adapter mContactAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_contact);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mContactRecyclerView = findViewById(R.id.contactList_recyclerview) ;

        LinearLayoutManager manager = new LinearLayoutManager(this) ;
        mContactRecyclerView.setHasFixedSize(true);
        mContactRecyclerView.setLayoutManager(manager);
    }

    @Override
    protected void onStart() {
        super.onStart();
        dbUsers = Database.getInstance().getUsers();
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void attachDatabaseListener(){
        if(mValueEventListener == null){

            mValueEventListener = new ValueEventListener() {
                @Override

                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.d(TAG, dataSnapshot.toString());

                    User userAuth = dataSnapshot.child(FirebaseAuth.getInstance().getUid()).getValue(User.class);

                    List<User> userList= new ArrayList<>();
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren())
                    {
                        User user = userSnapshot.getValue(User.class);
                        if(user != null)
                            userList.add(user);
                        else
                            Log.d(TAG,"Mais pourquoi!!!");
                    }
                    Log.d(TAG,"size " + userList.size());
                    mContactAdapter = new ContactAdaptater(ContactsActivity.this, userAuth, userList);
                    mContactRecyclerView.setAdapter(mContactAdapter);



                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };

        }
        dbUsers.addValueEventListener(mValueEventListener);
    }

    private void detachDatabaseListener(){
        if (mValueEventListener != null){
           dbUsers.removeEventListener(mValueEventListener);
            mValueEventListener = null;
        }
    }


}
