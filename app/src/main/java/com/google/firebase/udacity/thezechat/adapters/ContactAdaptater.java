package com.google.firebase.udacity.thezechat.adapters;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.udacity.thezechat.R;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.udacity.thezechat.activities.MessagesActivity;
import com.google.firebase.udacity.thezechat.activities.UserProfileActivity;
import com.google.firebase.udacity.thezechat.models.Conversation;
import com.google.firebase.udacity.thezechat.models.Database;
import com.google.firebase.udacity.thezechat.models.FriendlyMessage;
import com.google.firebase.udacity.thezechat.models.User;
import com.google.firebase.udacity.thezechat.utils.CustomImageView;
import com.google.firebase.udacity.thezechat.utils.IntentHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
public class ContactAdaptater extends RecyclerView.Adapter<ContactAdaptater.ViewHolder> {
    private static final String TAG = ContactAdaptater.class.getSimpleName();
    public static final String KEY = "conversation";
    public static final String USER = "user";

    public static class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout mainLayout;
        CustomImageView profileImage;
        TextView profileNom ;
        TextView email ;


        public ViewHolder(View itemView) {
            super(itemView);
            mainLayout = itemView.findViewById(R.id.one_user);
            profileImage = itemView.findViewById(R.id.contact_activity_user_profile_image) ;
            profileNom = itemView.findViewById(R.id.user_profile_name) ;
            email = itemView.findViewById(R.id.user_email_profile) ;

        }
    }


    private Activity activity ;
    private User us;
    private List<Conversation> conversations = new ArrayList<>();
    private List<User> contactAppWhatList = new ArrayList<>();
    private List<FriendlyMessage> messages = new ArrayList<>();




    public ContactAdaptater (Activity activity, User us,List<User> mUsers) {
        this.contactAppWhatList = mUsers ;
        this.activity = activity ;
        this.us=us;
    }

    @NonNull
    @Override
    public ContactAdaptater.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.one_user_state, parent, false);
        return new ContactAdaptater.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactAdaptater.ViewHolder holder, int position) {

        setContentView(holder);

        setOnClickListener(holder);

    }

    @Override
    public int getItemCount() {
        return contactAppWhatList.size();
    }

    private void setContentView(ContactAdaptater.ViewHolder holder) {



        User user = contactAppWhatList.get(holder.getAdapterPosition());
        Log.d("contact", user.getUid());

            if (user.getImages() != null && !user.getImages().isEmpty()) {
                Map.Entry<String, Boolean> entry = user.getImages().entrySet().iterator().next();
                StorageReference currentImageRef = Database.getInstance().getUserImages(user.getUid()).child(entry.getKey());
                Glide.with(activity).load(currentImageRef).into(holder.profileImage);
            }


        holder.profileNom.setText(user.getName());
        holder.email.setText(user.getEmail());



    }

    private void setOnClickListener(final ContactAdaptater.ViewHolder holder) {

        final User userSelected = contactAppWhatList.get(holder.getAdapterPosition());

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doesConversationExist(userSelected);
            }
        });
    }

    private void doesConversationExist(final User userSelected) {
        Log.d(TAG, "click " + userSelected.getName());
        final List<User> userList= new ArrayList<>();

        userList.add(userSelected);

        Database.getInstance().getUserConversations(us.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, dataSnapshot.toString());

                boolean conversationExist = false;
                String cid = "";

                for (DataSnapshot conversationSnapshot : dataSnapshot.getChildren()) {

                    List<User> usersInConversation = new ArrayList<>();
                    for (DataSnapshot userSnapshot : conversationSnapshot.child(Database.USERS).getChildren()) {

                        User user = userSnapshot.getValue(User.class);

                        if (user.getUid().equals(userSelected.getUid())) {

                            conversationExist = true;

                            cid = conversationSnapshot.getKey();

                        } else {
                            Log.e(TAG, "null");
                        }


                    }

                }
                if (!conversationExist) {
                    Database.getInstance().createConversation(activity, us, userList);
                } else { Log.d(TAG, "click " + userSelected.getName() + " " + conversationExist);
                    Conversation conversation = new Conversation();
                    conversation.setCid(cid);

                    Bundle out = new Bundle();
                    out.putParcelable(KEY, conversation);
                    IntentHandler.getInstance().openActivity(activity, MessagesActivity.class, out);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }



}
