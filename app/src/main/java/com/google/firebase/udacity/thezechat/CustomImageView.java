package com.google.firebase.udacity.thezechat;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.udacity.thezechat.utils.IntentHandler;

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

    public void setEditImageDialog(final Activity activity) {

        Bitmap image = ((BitmapDrawable) getDrawable()).getBitmap();

        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.custom_image_view);

        ImageView profileImage = dialog.findViewById(R.id.dialog_image);
        profileImage.setImageBitmap(image);

        Button openCamera = dialog.findViewById(R.id.open_camera);
        openCamera.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activity, "Not available", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        Button openGallery = dialog.findViewById(R.id.open_gallery);
        openGallery.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentHandler.getInstance().openGallery(activity);
                dialog.dismiss();
            }
        });

        Button openStorage = dialog.findViewById(R.id.open_storage);
        openStorage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentHandler.getInstance().openInternalStorage(activity, IntentHandler.getTypeImage());
                dialog.dismiss();
            }
        });

        Button delete = dialog.findViewById(R.id.delete);
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
