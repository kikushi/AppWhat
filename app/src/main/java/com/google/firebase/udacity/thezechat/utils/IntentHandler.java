package com.google.firebase.udacity.thezechat.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

public class IntentHandler {

    private static final String TAG = IntentHandler.class.getSimpleName();
    private static final int REQUEST_CODE_CAMERA = 1;
    private static final int REQUEST_CODE_GALLERY = 2;
    private static final int REQUEST_CODE_INTERNAL_STORAGE = 3;
    private static final String TYPE_IMAGE = "image";
    private static final String TYPE_AUDIO = "audio";

    private static final IntentHandler mInstance = new IntentHandler();

    public static IntentHandler getInstance() {
        return mInstance;
    }

    private IntentHandler() {
        //Default constructor
    }

    /**
     * Start a new activity
     * @param fromActivity  : Activity from which you launch a new activity
     * @param toActivity    : Activity (Class) where you want to navigate
     * @param extras        : Bundle, send data through activity (with Parcelable)
     *                      1. If you send an object, make sure it is Parcelable
     *                      2. In start activity, create new Bundle
     *                      3. Add [key, object] with bundle.putParcelable
     *                      4. Add the bundle to extras in openActivity
     *                      5. In new activity, get the bundle with getIntent.getExtras
     *                      6. Get the object with bundle.getParcelable using the key
     *                      7. Always if check if Bundle or Object is null
     */

    public void openActivity(Activity fromActivity, Class toActivity, Bundle extras) {
        Log.d(TAG, "openActivity from " + fromActivity.getLocalClassName() + " to " + toActivity.getSimpleName());
        Intent intent = new Intent(fromActivity, toActivity);
        if(extras != null)
            intent.putExtras(extras);
        fromActivity.startActivity(intent);
    }

    /**
     * Open internal storage of the phone
     * @param fromActivity  : Activity where you want to open internal storage
     * @param fileType      : Type of file you want to open
     */

    public void openInternalStorage(Activity fromActivity, String fileType) {
        Log.d(TAG, "openInternalStorage " + fromActivity.getLocalClassName() + " of type " + fileType);

        if (isExternalStorageWritable()) {

            if (isExternalStorageReadable()) {

                Intent openInternalStorage = new Intent(Intent.ACTION_GET_CONTENT);

                openInternalStorage.addCategory(Intent.CATEGORY_OPENABLE);

                switch (fileType) {
                    case TYPE_IMAGE:
                        openInternalStorage.setType("image/*");
                        break;
                    case TYPE_AUDIO:
                        openInternalStorage.setType("audio/*");
                        break;
                    default:
                        openInternalStorage.setType("/*");
                        break;
                }

                if (openInternalStorage.resolveActivity(fromActivity.getPackageManager()) != null) {
                    fromActivity.startActivityForResult(openInternalStorage, REQUEST_CODE_INTERNAL_STORAGE );
                }

            } else {
                Log.e(TAG, "Storage not readable");
            }
        } else {
            Log.e(TAG, "Storage not writable");
        }
    }

    /**
     * Open the default gallery used by the user
     * @param activity from which the user launch the gallery
     */

    public void openGallery(Activity activity) {
        Log.d(TAG, "openGallery from " + activity.getLocalClassName());

        Intent openGalleryIntent = new Intent(Intent.ACTION_PICK);

        if(openGalleryIntent.resolveActivity(activity.getPackageManager()) != null)
            openGalleryIntent.setType("image/*");

        activity.startActivityForResult(openGalleryIntent, REQUEST_CODE_GALLERY);
    }

    // Doesn't not work work for the moment
    /*public void openCamera(Activity activity) {
        Log.d(TAG, "openCamera from " + activity.getLocalClassName());
        try {

            if(ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED)
                ActivityCompat.requestPermissions(activity, new String[] {
                        Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA);

            Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            if (openCameraIntent.resolveActivity(activity.getPackageManager()) != null) {

                activity.startActivityForResult(openCameraIntent, REQUEST_CODE_CAMERA);
            }

        } catch (SecurityException e) {
            Log.e(TAG, e.getMessage());
        }
    }*/

    /**
     * Check if external storage is available for read and write
     * @return  true if the storage is writable; false if not
     **/

    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /**
     * Check if external storage is avaible to read at least
     * @return  true if storage readable; false if not
     */
    private boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }


    /**
     * @return  request code of the camera
     */
    public static int getRequestCodeCamera() {
        return REQUEST_CODE_CAMERA;
    }

    /**
     * @return  request code of the gallery
     */
    public static int getRequestCodeGallery() {
        return REQUEST_CODE_GALLERY;
    }

    /**
     * @return  request code of the phone internal storage
     */
    public static int getRequestCodeInternalStorage() {
        return REQUEST_CODE_INTERNAL_STORAGE;
    }

    /**
     * @return  return image type
     */
    public static String getTypeImage() {
        return TYPE_IMAGE;
    }

    /**
     * @return  return audio type
     */
    public static String getTypeAudio() {
        return TYPE_AUDIO;
    }
}
