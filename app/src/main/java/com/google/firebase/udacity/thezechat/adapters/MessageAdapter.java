package com.google.firebase.udacity.thezechat.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.udacity.thezechat.R;
import com.google.firebase.udacity.thezechat.models.FriendlyMessage;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends ArrayAdapter<FriendlyMessage> {

    private static final String TAG = MessageAdapter.class.getSimpleName();

    private List<FriendlyMessage> messages = new ArrayList<>();
    private FirebaseUser localUser ;

    public MessageAdapter(Context context, int resource, List<FriendlyMessage> objects, FirebaseUser local) {
        super(context, resource, objects);
        localUser = local ;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        FriendlyMessage message = getItem(position);
        String local = localUser.getUid() ;
        String sender = message.getUserID() ;
        boolean isSentByLocal = local.equals(sender) ;
        Log.d(TAG, "User id " + local + " message user id " + sender);
        convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.message_view_received, parent, false);
        updateUIMessage(convertView, local, sender);

        if (convertView == null) {

            /*if (isSentByLocal) {
                convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.message_view_sent, parent, false);
            }
            else {
                Log.e(TAG, "Receive by " + sender);
                convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.message_view_received, parent, false);
            }*/
        }

        ImageView photoImageView = (ImageView) convertView.findViewById(R.id.message_img) ;
        TextView messageTextView = (TextView) convertView.findViewById(R.id.message_text) ;
        TextView messageDateView = (TextView) convertView.findViewById(R.id.message_time) ;
        TextView messageNameView = (TextView) convertView.findViewById(R.id.message_author_name) ;

        boolean isPhoto = message.getPhotoUrl() != null;
        if (isPhoto) {
            messageTextView.setVisibility(View.GONE);
            photoImageView.setVisibility(View.VISIBLE);
            Glide.with(photoImageView.getContext())
                    .load(message.getPhotoUrl())
                    .into(photoImageView);
        } else {
            messageTextView.setVisibility(View.VISIBLE);
            photoImageView.setVisibility(View.GONE);
            messageTextView.setText(message.getText());
        }
        if (message.getTime() != null)
            messageDateView.setText(message.getTime());
        messageNameView.setText(message.getName());

        return convertView;
    }

    @Override
    public void add(@Nullable FriendlyMessage object) {
        super.add(object);
        Log.d(TAG, "MEssage " + object.getUserID());

   }

   private void updateUIMessage(View view, String idLocal, String idSender) {
       LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
       params.weight = 1.0f;
       params.gravity = Gravity.TOP;


        LinearLayout messageLayout = view.findViewById(R.id.message_layout);
        TextView auteur = view.findViewById(R.id.message_author_name);
        if (idLocal.equals(idSender)) {

            params.gravity = Gravity.END;
            messageLayout.setBackgroundColor(getContext().getResources().getColor(R.color.colorAppWhatGreen));
        } else {
            params.gravity = Gravity.START;
            messageLayout.setBackgroundColor(getContext().getResources().getColor(R.color.colorAppWhatGrey));
        }
       messageLayout.setLayoutParams(params);
        auteur.setLayoutParams(params);


   }
}