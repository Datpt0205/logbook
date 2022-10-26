package com.example.excersise4;

import static android.os.Environment.getExternalStoragePublicDirectory;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.example.excersise4.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.DexterBuilder;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public class MainActivity extends AppCompatActivity {

    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    public static final int GALLERY_REQUEST_CODE = 105;
//    @BindView(R.id.cameraBtn)
//    Button cameraBtn;
//    @BindView(R.id.image_view)
//    ImageView image_view;
//
//    private Uri imageUri;
//
//    @OnClick(R.id.cameraBtn)
//            void onCaptureImageClick(){
//        Dexter.withContext(this)
//                .withPermissions(Arrays.asList(
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                        Manifest.permission.READ_EXTERNAL_STORAGE,
//                        Manifest.permission.CAMERA
//                ))
//                .withListener(new MultiplePermissionsListener(){
//
//                                  @Override
//                                  public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
//                                      if(multiplePermissionsReport.areAllPermissionsGranted()){
//                                          ContentValues cv = new ContentValues();
//                                          cv.put(MediaStore.Images.Media.TITLE, "New Picture");
//                                          cv.put(MediaStore.Images.Media.DESCRIPTION,"My Camera");
//                                          imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv);
//                                          Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                                          i.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//                                          startActivityForResult(i, CAMERA_REQUEST_CODE);
//                                      }else{
//                                          Toast.makeText(MainActivity.this, "You must accept all permission", Toast.LENGTH_SHORT).show();
//                                      }
//                                  }
//
//                                  @Override
//                                  public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
//
//                                  }
//                              }
//                );
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == CAMERA_REQUEST_CODE){
//            if(resultCode == Activity.RESULT_OK){
//                try{
//                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
//                    image_view.setImageBitmap(bitmap);
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }

    ImageView imageView;
    DatabaseHelper db;
    EditText url;
    ArrayList<String> list = new ArrayList<String>();
    ArrayList<String> id = new ArrayList<String>();
    int index = 0;
    Button addUrl, btn_next, btn_previous, cameraBtn, galleryBtn;
    String currentPhotoPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.image_view);
        url = findViewById(R.id.url);
        btn_next = findViewById(R.id.btn_next);
        btn_previous = findViewById(R.id.btn_previous);
        galleryBtn = findViewById(R.id.galleryBtn);
        cameraBtn = findViewById(R.id.cameraBtn);
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askCameraPermissions();
            }
        });

        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery, GALLERY_REQUEST_CODE);
            }
        });

        db = new DatabaseHelper(MainActivity.this);
        storeUrl();

        Glide.with(MainActivity.this)
                .load(list.get(index))
                .centerCrop()
                .into(imageView);

        addUrl = findViewById(R.id.addUrl);
        addUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper db = new DatabaseHelper(MainActivity.this);
                String url_image = url.getText().toString().trim();
                if (url_image.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Required Url", Toast.LENGTH_SHORT).show();
                } else {
                    db.addUrl(url_image);
                    Toast.makeText(MainActivity.this, "Add url image successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index--;
                Glide.with(MainActivity.this)
                        .load(list.get(index))
                        .centerCrop()
                        .into(imageView);
                if (index == 0) {
                    btn_previous.setEnabled(false);
                }
                if (index > 0) {
                    btn_previous.setEnabled(true);
                }
                if (index == Integer.valueOf(list.size() - 1)) {
                    btn_next.setEnabled(false);
                } else {
                    btn_next.setEnabled(true);
                }
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index++;
                Glide.with(MainActivity.this)
                        .load(list.get(index))
                        .centerCrop()
                        .into(imageView);
                if (index == 0) {
                    btn_previous.setEnabled(false);
                }
                if (index > 0) {
                    btn_previous.setEnabled(true);
                }
                if (index == Integer.valueOf(list.size() - 1)) {
                    btn_next.setEnabled(false);
                } else {
                    btn_next.setEnabled(true);
                }

            }
        });
    }

        private void askCameraPermissions () {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
            } else {
                dispatchTakePictureIntent();
            }
        }

        @SuppressLint("MissingSuperCall")
        @Override
        public void onRequestPermissionsResult ( int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            if (requestCode == CAMERA_PERM_CODE) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    dispatchTakePictureIntent();
                } else {
                    Toast.makeText(this, "Camera Permission is required to use Camera",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                File f = new File(currentPhotoPath);
                imageView.setImageURI(Uri.fromFile(f));
                Log.d("tag", "Absolute Url of Image is " + Uri.fromFile(f));

                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(f);
                mediaScanIntent.setData(contentUri);
                this.sendBroadcast(mediaScanIntent);
            }
        }
        if (requestCode == GALLERY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri contentUri = data.getData();
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "JPEG_" + timeStamp + "." + getFileExt(contentUri);
                Log.d("tag", "onActivityResult: Gallery Image Uri:  " + imageFileName);
                imageView.setImageURI(contentUri);
            }

        }
    }

    private String getFileExt(Uri contentUri) {
        ContentResolver c = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(c.getType(contentUri));
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "net.smallacademy.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }


//        list.add(0, "https://i.imgur.com/Q9WPlWA.jpeg");
//        list.add(1, "https://i.imgur.com/oPj4A8u.jpeg");
//        list.add(2, "https://i.imgur.com/MWAcQRM.jpeg");
//        list.add(3, "https://i.imgur.com/Lnt9K7l.jpeg");
//        list.add(4, "https://i.imgur.com/Gg6BpGn.jpeg");


    void storeUrl ()
    {
        Cursor cursor = db.showImage();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No Data Available", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                id.add(cursor.getString(0));
                list.add(cursor.getString(1));
            }
        }
    }

}