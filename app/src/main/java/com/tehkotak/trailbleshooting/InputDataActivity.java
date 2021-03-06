package com.tehkotak.trailbleshooting;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.images.ImageManager;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.iceteck.silicompressorr.FileUtils;
import com.iceteck.silicompressorr.SiliCompressor;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import static android.os.Environment.getExternalStoragePublicDirectory;

public class InputDataActivity extends AppCompatActivity {

    private static final String TAG = "";

    //declare object used
    private Button btn_sendData;
    private ImageView img_take, icBack;
    private TextView tv_lati, tv_longi, tvdateTime;
    private EditText et_comment;

    private String pictureFilePath;

    //declare String for pathFileImage saving to phone storage
    private String pathToFile;
    private String deviceIdentifier, date;

    //declare imageURI
    private Uri imageURI;

    //declare fire base storage
    FirebaseStorage fbStorage;
    StorageReference storageRef;
    DatabaseReference databaseRef;
    FirebaseDatabase database;

    //==================khusus buat ambil images dari storage Fire base===============//
    public String storage_path = "Gambar_kerusakan/";
    public String storage_imageAnu = "Image_anu/";
    public static final String database_path = "Data_kerusakan";
    //==================khusus buat ambil images dari storage Fire base===============//

    private static final int GALERY_REQ = 1;
    private static final int CAMERA_REQ = 1;

    int Image_Request_Code = 7;
    private static final int IMAGE_CAPTURE_CODE = 1001;

    //progressDialog
    private ProgressDialog dialog;

    private static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    String latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_data);

        //date time init
        setUpDatedanTime();

        //permission camera access android SDK >= 23 to write external storage
        if (Build.VERSION.SDK_INT >= 23) {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        }

        //permission GPS
        ActivityCompat.requestPermissions(this, new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        //init object
        btn_sendData = findViewById(R.id.btn_sendData);
        img_take = findViewById(R.id.picture_condition);
        tv_lati = findViewById(R.id.tv_lat_inp);
        tv_longi = findViewById(R.id.tv_long_inp);
        et_comment = findViewById(R.id.et_comment);
        tvdateTime = findViewById(R.id.tv_dateTime);
        icBack = findViewById(R.id.ic_back_input_kerusakan);

        icBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        et_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                //check GPS enable or not
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    //write method to enable GPS
                    enableGPS();
                } else {
                    //GPS is already on then
                    getLocation();
                }
            }
        });

        //init for SnackBar
        final View contextView = findViewById(R.id.layout_inputData);

        dialog = new ProgressDialog(this);

        //init Fire Base
        database = FirebaseDatabase.getInstance();
        //storage
        //storageRef = FirebaseStorage.getInstance().getReference("Gambar1" + UUID.randomUUID().toString());
        storageRef = FirebaseStorage.getInstance().getReference();

        //realtime
        databaseRef = FirebaseDatabase.getInstance().getReference(database_path);

        img_take.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchPictureTakerAction();
            }
        });

        btn_sendData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //uploadData();

                //upload data to Fire base
                addToCloud();
            }
        });

    }

    private void setUpDatedanTime() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                long date = System.currentTimeMillis();
                                SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM/dd/yyyy hh:mm:ss a");
                                String dateString = sdf.format(date);
                                tvdateTime.setText(dateString);
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };
        thread.start();
    }

    private void getLocation() {
        //check permission again
        if (ActivityCompat.checkSelfPermission(InputDataActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(InputDataActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location locationNetwork = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Location locationPassive = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if (locationGPS != null) {
                double lat = locationGPS.getLatitude();
                double longi = locationGPS.getLongitude();

                latitude = String.valueOf(lat);
                longitude = String.valueOf(longi);

                //setText TextView Latitude and Longitude
                tv_lati.setText(latitude);
                tv_longi.setText(longitude);

            } else if (locationNetwork != null) {
                double lat = locationNetwork.getLatitude();
                double longi = locationNetwork.getLongitude();

                latitude = String.valueOf(lat);

                longitude = String.valueOf(longi);

                //setText TextView Latitude and Longitude
                tv_lati.setText(latitude);
                tv_longi.setText(longitude);

            } else if (locationPassive != null) {
                double lat = locationPassive.getLatitude();
                double longi = locationPassive.getLongitude();

                latitude = String.valueOf(lat);
                longitude = String.valueOf(longi);

                //setText TextView Latitude and Longitude
                tv_lati.setText(latitude);
                tv_longi.setText(longitude);

            } else {
                Toast.makeText(InputDataActivity.this, "Sorry, could not find your location!", Toast.LENGTH_SHORT).show();
            }

            //run
        }
    }

    private void enableGPS() {
        final AlertDialog.Builder builde = new AlertDialog.Builder(this);
        builde.setMessage("Your GPS is off").setCancelable(false).setPositiveButton("Activate now", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        });
        final AlertDialog alertDialog = builde.create();
        alertDialog.show();
    }

    private void addToCloud() {

        final View ctxView = findViewById(R.id.layout_inputData);

        //start form oct
        final StorageReference ImageName = storageRef.child(storage_imageAnu + imageURI.getLastPathSegment());
        dialog.setTitle("Uploading..");
        dialog.setMessage("Please wait..");
        dialog.show();
        File file = new File(SiliCompressor.with(this).compress(FileUtils.getPath(this, imageURI), new File(this.getCacheDir(), "temp")));
        Uri uri = Uri.fromFile(file);

        ImageName.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                ImageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("datetime", tvdateTime.getText().toString());
                        hashMap.put("imageURL", String.valueOf(uri));
                        hashMap.put("komentar", et_comment.getText().toString());
                        hashMap.put("latitude", tv_lati.getText().toString());
                        hashMap.put("longitude", tv_longi.getText().toString());
                        dialog.dismiss();
                        String ImageUploadId = databaseRef.push().getKey();

                        databaseRef.child(ImageUploadId).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                dialog.dismiss();
                                Snackbar.make(ctxView,
                                        "Successfully yeah",
                                        Snackbar.LENGTH_SHORT).show();

                                et_comment.setText("");
                                img_take.setImageResource(R.drawable.ic_camera_access);
                            }
                        });
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                double dialogStatus = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                dialog.setMessage("Please wait.. " + (int) dialogStatus + " %");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                Toast.makeText(InputDataActivity.this, "Something wrong!", Toast.LENGTH_SHORT).show();
            }
        });


        /*final View ctxView = findViewById(R.id.layout_inputData);

        dialog.setTitle("Upload Data");
        dialog.setMessage("sedang mengirim");
        dialog.show();

        File f = new File(pictureFilePath);
        final Uri picUri = Uri.fromFile(f);
        final String cloudFilePath = deviceIdentifier + picUri.getLastPathSegment();

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        final StorageReference storageRef = firebaseStorage.getReference();
        final StorageReference uploadeRef = storageRef.child(storage_path + UUID.randomUUID().toString());

        UploadTask uploadTask = null;

        uploadeRef.putFile(picUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                String datetime = tvdateTime.getText().toString().trim();
                String TempImageName = et_comment.getText().toString().trim();
                String lat = tv_lati.getText().toString().trim();
                String longi = tv_longi.getText().toString().trim();

                dialog.dismiss();
                Snackbar.make(ctxView,
                        "Gambar berhasil di upload ke Database!",
                        Snackbar.LENGTH_SHORT).show();

                //UploadInfo imageUploadInfo = new UploadInfo(TempImageName, (taskSnapshot.getMetadata().toString()), lat, longi, datetime);
                DataModel dataUpload = new DataModel(datetime, (taskSnapshot.getUploadSessionUri().toString()), TempImageName, lat, longi);
                Log.e(TAG, "Berhasil upload data...");
                String ImageUploadId = databaseRef.push().getKey();
                databaseRef.child(ImageUploadId).setValue(dataUpload);

                et_comment.setText("");
                img_take.setImageResource(R.drawable.ic_camera_access);
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                double dialogStatus = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                dialog.setMessage("Tunggu.. " + (int) dialogStatus + "%");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                Toast.makeText(InputDataActivity.this, "Data gambar kosong!", Toast.LENGTH_SHORT).show();
            }
        }); */
    }

    protected synchronized String getInstallationIdentifier() {
        if (deviceIdentifier == null) {
            SharedPreferences sharedPrefs = this.getSharedPreferences(
                    "DEVICE_ID", Context.MODE_PRIVATE);
            deviceIdentifier = sharedPrefs.getString("DEVICE_ID", null);
            if (deviceIdentifier == null) {
                deviceIdentifier = UUID.randomUUID().toString();
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString("DEVICE_ID", deviceIdentifier);
                editor.commit();
            }
        }
        return deviceIdentifier;
    }

    private void dispatchPictureTakerAction() {

        // start from oct
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "New Picture");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "From Camera");
        imageURI = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

        Intent takePic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePic.putExtra(MediaStore.EXTRA_OUTPUT, imageURI);
        startActivityForResult(takePic, IMAGE_CAPTURE_CODE);
        //end form oct

        /*if (takePic.resolveActivity(getPackageManager()) != null) {
            File pictureFile = null;
            pictureFile = createPictureFile();

            if (pictureFile != null) {
                pathToFile = pictureFile.getAbsolutePath();
                Uri pictureUri = FileProvider.getUriForFile(InputDataActivity.this, "com.tehkotak.trailbleshooting.fileprovider", pictureFile);
                takePic.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri);
                startActivityForResult(takePic, 1);
            }
        }*/
    }

    private File createPictureFile() {
        String detailName = new SimpleDateFormat("yyyMMdd-HHmmss").format(new Date());
        File storageDirectory = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File pictureName = null;

        try {
            pictureName = File.createTempFile(detailName, ".jpg", storageDirectory);
            pictureFilePath = pictureName.getAbsolutePath();
        } catch (IOException e) {
            Log.d("Log gambar kamera", "Exception : " + e.getMessage());
        }
        return pictureName;
    }

    //get data result and set imageView
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //start from oct
        if (resultCode == RESULT_OK) {
            img_take.setImageURI(imageURI);
        }
        //end form oct

        /*if (requestCode == 1 && resultCode == RESULT_OK) {
            File imgFile = new File(pictureFilePath);

            if (imgFile.exists()) {
                img_take.setImageURI(Uri.fromFile(imgFile));
            }
        }*/
    }

    public String getFileExtention(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
}


