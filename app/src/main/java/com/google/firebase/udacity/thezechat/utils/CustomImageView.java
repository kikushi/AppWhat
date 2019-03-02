package com.google.firebase.udacity.thezechat.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.udacity.thezechat.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomImageView extends CircleImageView {

    private static final String TAG = CustomImageView.class.getSimpleName();

    public CustomImageView(Context context) {
        super(context);
    }

    public CustomImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setUsersDialog(final Activity activity, String name) {

        Bitmap image = ((BitmapDrawable) getDrawable()).getBitmap();

        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.dialog_users_card);

        TextView userName = dialog.findViewById(R.id.dialog_users_card_user_name);
        if (name != null)
            userName.setText(name);

        ImageView userImage = dialog.findViewById(R.id.dialog_users_card_user_image);
        userImage.setImageBitmap(image);

        ImageButton follow = dialog.findViewById(R.id.dialog_users_card_follow_user);
        follow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activity, "Not available", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        ImageButton sendMessage = dialog.findViewById(R.id.dialog_users_card_send_message);
        sendMessage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activity, "Not available", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        ImageButton aboutUser = dialog.findViewById(R.id.dialog_users_card_about_user);
        aboutUser.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activity, "Not available", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    public void setEditImageDialog(final Activity activity) {

        Bitmap image = ((BitmapDrawable) getDrawable()).getBitmap();

        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.dialog_edit_image);

        ImageView profileImage = dialog.findViewById(R.id.dialog_image);
        profileImage.setImageBitmap(image);

        ImageButton openCamera = dialog.findViewById(R.id.dialog_edit_image_open_camera);
        openCamera.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activity, "Not available", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        ImageButton openGallery = dialog.findViewById(R.id.dialog_edit_image_open_gallery);
        openGallery.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentHandler.getInstance().openGallery(activity);
                dialog.dismiss();
            }
        });

        ImageButton openStorage = dialog.findViewById(R.id.dialog_edit_image_open_storage);
        openStorage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentHandler.getInstance().openInternalStorage(activity, IntentHandler.getTypeImage());
                dialog.dismiss();
            }
        });

        ImageButton delete = dialog.findViewById(R.id.dialog_edit_image_delete_image);
        delete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setImageResource(R.drawable.image_user_default);
                dialog.dismiss();
            }
        });

        dialog.show();

    }
}
