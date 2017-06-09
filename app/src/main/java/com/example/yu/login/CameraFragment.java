package com.example.yu.login;


import android.content.Intent;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class CameraFragment extends Fragment {

    private static final String TAG = "Camera";
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    public CameraFragment() {
        // Required empty public constructor
    }

    private ImageButton click;
    private ImageButton shareButton;


    private Uri mediaStoreUri = null; //for sharing

    private String fileName;

    private File imageFile;

    private Uri pictureFileUri; //for saving


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_fragment_cammar, container, false);

        click = (ImageButton) rootview.findViewById(R.id.CammarButton);


        // Take picture
        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    File file = null;

                    try {
                        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()); //include timestamp

                        File dir = getActivity().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
                        if (!dir.exists()) {
                            dir.mkdirs(); //make Documents directory if doesn't otherwise exist
                        }
                        file = new File(dir, "PIC_" + timestamp + ".jpg");
                        imageFile = file; // just testing can delete later
                        boolean created = file.createNewFile(); //actually make the file!
                        Log.v(TAG, "File created: " + created);

                    } catch (IOException ioe) {
                        Log.d(TAG, "IOE CAMERA: " + Log.getStackTraceString(ioe));
                    }

                    if (file != null) {
                        pictureFileUri = FileProvider.getUriForFile(getActivity(), BuildConfig.APPLICATION_ID, file);
                        Log.v(TAG, "Uri: "+pictureFileUri);

                        // For sharing the media (produces a Uri the Messenger has permissions for)
                        MediaScannerConnection.scanFile(getActivity(), new String[] { file.toString() }, null,
                                new MediaScannerConnection.OnScanCompletedListener() {
                                    public void onScanCompleted(String path, Uri uri) {
                                        mediaStoreUri = uri;
                                        Log.v(TAG, "MediaStore Uri: "+uri);
                                    }
                                });
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, pictureFileUri);
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }
                }

            }
        });

        // Share picture
        shareButton = (ImageButton) rootview.findViewById(R.id.sharePicture);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               sharePicture(getView());
            }
        });
        return rootview;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            ImageView imageView = (ImageView) getActivity().findViewById(R.id.CammarView);

            imageView.setImageURI(pictureFileUri);

            // Adjust rotation
            Log.d(TAG, "ROTATED PRE: " + imageView.getRotation());
            int exifOrientation = 0;

            try { // Try to get the orientation of the image

                ExifInterface exif = new ExifInterface(imageFile.getPath());
                exifOrientation = Integer.valueOf(exif.getAttribute(ExifInterface.TAG_ORIENTATION));
                Log.d(TAG, "EXIF ORIENTATION: " + exifOrientation);

            } catch (IOException ioe) {

                Log.e(TAG, Log.getStackTraceString(ioe));

            }

            // Check and set orientation
            if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
                imageView.setRotation(90);

            } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
                imageView.setRotation(180);

            } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
                imageView.setRotation(270);

            } else {
                imageView.setRotation(0); // Rotation is normal
            }

            Log.d(TAG, "ROTATED POST: " + imageView.getRotation());
            Log.e(TAG, "IMAGE URI: " + pictureFileUri);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    // Share the picture
    public void sharePicture(View v) {
        Log.v(TAG, "Sharing picture...");

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_STREAM, mediaStoreUri);
        Log.v(TAG, "Uri: " + mediaStoreUri);

        // Get an app that can send pictures (Faccebook, Gmail, native SMS, etc..)
        Intent chooser = Intent.createChooser(intent, "Share Picture");

        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(chooser);
        }
    }

    // Check if SD card is mounted (external storage)
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

}
