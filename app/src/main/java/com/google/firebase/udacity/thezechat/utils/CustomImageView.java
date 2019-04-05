package com.google.firebase.udacity.thezechat.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.udacity.thezechat.R;
import com.google.firebase.udacity.thezechat.models.Database;
import com.google.firebase.udacity.thezechat.models.User;

import java.util.Map;

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


        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.dialog_users_card);

        TextView userName = dialog.findViewById(R.id.dialog_users_card_user_name);
        if (name != null)
            userName.setText(name);

        //ImageView userImage = dialog.findViewById(R.id.dialog_users_card_user_image);
        //userImage.setImageBitmap(image);

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

    public void setEditImageDialog(final Activity activity, User user) {

        Bitmap image = ((BitmapDrawable) getDrawable()).getBitmap();
        scaleBitmap(image, this.getWidth(), this.getHeight());


        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.dialog_edit_image);

        if (!user.getImages().isEmpty()) {
            Map.Entry<String, Boolean> entry = user.getImages().entrySet().iterator().next();
            StorageReference currentImageRef = Database.getInstance().getUserImages(user.getUid()).child(entry.getKey());
            Glide.with(this).load(currentImageRef).into(this);
        }


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

    @Override
    public void setDrawingCacheEnabled(boolean enabled) {
        super.setDrawingCacheEnabled(enabled);
    }

    @Override
    public void buildDrawingCache() {
        super.buildDrawingCache();
    }

    public static Bitmap loadBitmapFromView(View v) {
        Bitmap b = Bitmap.createBitmap( v.getLayoutParams().width, v.getLayoutParams().height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.layout(0, 0, v.getLayoutParams().width, v.getLayoutParams().height);
        v.draw(c);
        return b;
    }

    public static Bitmap scaleBitmap(Bitmap bitmap, int newWidth, int newHeight) {
        Bitmap scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);

        float scaleX = newWidth / (float) bitmap.getWidth();
        float scaleY = newHeight / (float) bitmap.getHeight();
        float pivotX = 0;
        float pivotY = 0;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(scaleX, scaleY, pivotX, pivotY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bitmap, 0, 0, new Paint(Paint.FILTER_BITMAP_FLAG));

        return scaledBitmap;
    }

}
