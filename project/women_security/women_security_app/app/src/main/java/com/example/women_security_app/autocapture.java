package com.example.women_security_app;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class autocapture extends AppCompatActivity {


    private Button btn_front, btn_back;
    private String TAG = MainActivity.class.getName();
    private ImageView iv_image;
   String MY_PREFS_NAME = "MyPrefsFile";


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autocapture);

//        btn_front = findViewById(R.id.btn_front);
        btn_back = findViewById(R.id.btn_back);
        iv_image = findViewById(R.id.iv_image);

//
//        btn_front.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                requestAppPermissions("1");
//            }
//
//        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestAppPermissions("0");
            }

        });


    }

    private void requestAppPermissions(String s) {

        Dexter.withActivity(autocapture.this)
                .withPermissions(
                        Manifest.permission.CAMERA)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            if (s.equals("0")) {
                                CaptureBackPhoto();
                            } else {
//                                CaptureFrontPhoto();
                            }
                        }
                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // permission is denied permenantly, navigate user to app settings
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                })
                .onSameThread()
                .check();
    }

    private void CaptureBackPhoto() {
        Log.d(TAG, "Preparing to take photo");
        Camera camera = null;
        try {
            camera = Camera.open();
            camera.enableShutterSound(false);
        } catch (RuntimeException e) {
            Log.d(TAG, "Camera not available: " + 1);
            camera = null;
            e.printStackTrace();
        }
        try {
            if (null == camera) {
                Log.d(TAG, "Could not get camera instance");

            } else {
                Log.d(TAG, "Got the camera, creating the dummy surface texture");
                try {
                    camera.setPreviewTexture(new SurfaceTexture(0));
                    camera.startPreview();

                } catch (Exception e) {
                    Log.d(TAG, "Could not set the surface preview texture");
                    e.printStackTrace();
                }
                camera.takePicture(null, null, new Camera.PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] data, Camera camera) {
                        try {
                            Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                            iv_image.setImageBitmap(bmp);
                            savefile(bmp);
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                        camera.release();
                    }
                });
            }
        } catch (Exception e) {
            camera.release();
        }
    }

    private void savefile(Bitmap bmp) {

        if (Build.VERSION.SDK_INT >= 30){
            if (!Environment.isExternalStorageManager()){
                Intent getpermission = new Intent();
                getpermission.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivity(getpermission);
            }
        }

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG,40,bytes);
        String filepath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "test.jpeg";

        try {
            File f = new File(filepath);
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            String mail = String.valueOf(getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE));
            Toast.makeText(getApplicationContext(),mail.toString(),Toast.LENGTH_LONG).show();

           SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
           String email = prefs.getString("mail","default");
           Toast.makeText(getApplicationContext(),email.toString(),Toast.LENGTH_LONG).show();

           capturesend s = new capturesend(getApplicationContext(),email.toString(),"image",filepath.toString());
           s.execute();
           fo.close();
        }catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(autocapture.this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }

        });

        builder.show();


    }

    private void openSettings() {

        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }
}