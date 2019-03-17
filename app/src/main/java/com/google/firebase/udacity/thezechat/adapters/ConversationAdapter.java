package com.google.firebase.udacity.thezechat.adapters;

import android.app.Activity;
import android.content.Context;
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
import com.google.firebase.udacity.thezechat.R;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.udacity.thezechat.activities.MessagesActivity;
import com.google.firebase.udacity.thezechat.models.Conversation;
import com.google.firebase.udacity.thezechat.models.Database;
import com.google.firebase.udacity.thezechat.models.FriendlyMessage;
import com.google.firebase.udacity.thezechat.models.User;
import com.google.firebase.udacity.thezechat.utils.CustomImageView;
import com.google.firebase.udacity.thezechat.utils.IntentHandler;

import java.util.ArrayList;
import java.util.List;

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ViewHolder> {

    private static final String TAG = ConversationAdapter.class.getSimpleName();
    public static final String KEY = "conversation";

    public static class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout mainLayout;
        CustomImageView profileImage;
        TextView profileNom ;
        TextView dernierTexte ;
        TextView dateContact ;

        public ViewHolder(View itemView) {
            super(itemView);
            mainLayout = itemView.findViewById(R.id.contact_view_main_layout);
            profileImage = itemView.findViewById(R.id.contact_view_profile_image) ;
            profileNom = itemView.findViewById(R.id.contact_view_name) ;
            dernierTexte = itemView.findViewById(R.id.contact_view_last_message) ;
            dateContact = itemView.findViewById(R.id.contact_view_last_message_date);
        }
    }


    private Activity activity ;
    private List<Conversation> conversations = new ArrayList<>();
    private List<User> contactAppWhatList = new ArrayList<>();
    private List<FriendlyMessage> messages = new ArrayList<>();


    public ConversationAdapter() {}

    public ConversationAdapter (Activity activity, List<Conversation> conversations) {
        this.conversations = conversations ;
        this.activity = activity ;
    }

    @NonNull
    @Override
    public ConversationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.contact_view, parent, false);
        return new ConversationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConversationAdapter.ViewHolder holder, int position) {

        setContentView(holder);

        setOnClickListener(holder);

    }

    @Override
    public int getItemCount() {
        return conversations.size();
    }

    private void setContentView(ConversationAdapter.ViewHolder holder) {

        Conversation conversation = conversations.get(holder.getAdapterPosition());

        if (conversation == null)
            throw new NullPointerException("Conversation is null at " + holder.getAdapterPosition());

        if (conversation.getUsers() == null)
            throw new NullPointerException("No users in conversation " + holder.getAdapterPosition());

        StringBuilder names = new StringBuilder();
        List<User> users = conversation.getUsers();

        for (int i = 0 ; i < users.size(); i++) {

            names.append(users.get(i).getName());

            if (i < users.size() - 1)
                names.append(", ");
        }
        holder.profileNom.setText(names.toString());


        if (conversation.getMessages() == null)
            throw new NullPointerException("No message in conversation " + holder.getAdapterPosition());
        if (conversation.getMessages().size() != 0)
            holder.dernierTexte.setText(conversation.getMessages().get(0).getText());
        else
            holder.dernierTexte.setText("(Vide)");

    }

    private void setOnClickListener(ConversationAdapter.ViewHolder holder) {

        final Conversation conversation = conversations.get(holder.getAdapterPosition());


        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle out = new Bundle();
                Log.e(TAG, "Size of messages is " + conversation.getCid());
                out.putParcelable(KEY, conversation);
                IntentHandler.getInstance().openActivity(activity, MessagesActivity.class, out);
            }
        });

        //if (conversation.getUsers().size() <= 2)
        //    holder.profileImage.setUsersDialog(activity, conversation.getUsers().get(0).getName());


    }

}
