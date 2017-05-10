package com.example.yu.login;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentCammar extends Fragment {


    public FragmentCammar() {
        // Required empty public constructor
    }

    private Button click;
    private Button shareButton;

    private ImageView imageView;

    private Uri mediaStoreUri = null; //for sharing

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_fragment_cammar, container, false);

        click = (Button) rootview.findViewById(R.id.CammarButton);
        imageView  = (ImageView) rootview.findViewById(R.id.CammarView);

        // Take picture
        // TODO: Picture is being saved upside down.
        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = getFile();
                Uri pictureFileUri = Uri.fromFile(file);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                startActivityForResult(intent , 1);
            }
        });

        // Share picture
        shareButton = (Button) rootview.findViewById(R.id.sharePicture);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               sharePicture(getView());
            }
        });
        return rootview;
    }
    private File getFile() {
        File folder = new File("sdcard/camera_app");
        if (!folder.exists()) {
            folder.mkdir();

        }
        // Make a timestamp for image
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File image_file = new File(folder, "WARUC_" + timestamp + ".jpg");

        // For sharing the media (produces a Uri the Messenger has permissions for)
        MediaScannerConnection.scanFile(getActivity(), new String[] { image_file.toString() }, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        mediaStoreUri = uri;
                        Log.v(TAG, "MediaStore Uri: "+uri);
                    }
                });

        return image_file;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        String path = "sdcard/camera_app/cam_image.jpg";
        imageView.setImageDrawable(Drawable.createFromPath(path));


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
