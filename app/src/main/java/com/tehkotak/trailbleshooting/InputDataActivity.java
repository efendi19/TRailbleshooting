package com.tehkotak.trailbleshooting;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import static android.os.Environment.getExternalStoragePublicDirectory;

public class InputDataActivity extends AppCompatActivity {

    private static final String TAG = "";

    //declare object used
    private Button btn_sendData;
    private ImageView img_take;
    private TextView tv_lati, tv_longi;
    private EditText et_comment;

    private String pictureFilePath;

    //declare String for pathFileImage saving to phone storage
    private String pathToFile;
    private String deviceIdentifier;

    //declare uri path
    private Uri filePath;

    //declare fire base storage
    FirebaseStorage fbStorage;
    StorageReference storageRef;
    DatabaseReference databaseRef;
    FirebaseDatabase database;

    private static final int GALERY_REQ = 1;
    private static final int CAMERA_REQ = 1;

    int Image_Request_Code = 7;

    //progressDialog
    private ProgressDialog dialog;

    private static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    String latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_data);

        //permission camera access android SDK >= 23 to write external storage
        if (Build.VERSION.SDK_INT >= 23) {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        }

        /*locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //check GPS enable or not
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //write method to enable GPS
            enableGPS();
        } else {
            //GPS is already on then
            getLocation();
        }*/

        //permission GPS
        ActivityCompat.requestPermissions(this, new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);


        //init object
        btn_sendData = findViewById(R.id.btn_sendData);
        img_take = findViewById(R.id.picture_condition);
        tv_lati = findViewById(R.id.tv_lat_inp);
        tv_longi = findViewById(R.id.tv_long_inp);
        et_comment = findViewById(R.id.et_comment);

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
        storageRef = FirebaseStorage.getInstance().getReference("Gambar1" + UUID.randomUUID().toString());

        //realtime
        databaseRef = FirebaseDatabase.getInstance().getReference("Data_kerusakan");

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
                Toast.makeText(InputDataActivity.this, "Tidak bisa menemukan lokasi anda!", Toast.LENGTH_SHORT).show();
            }

            //run
        }
    }

    private void enableGPS() {
        final AlertDialog.Builder builde = new AlertDialog.Builder(this);
        builde.setMessage("GPS anda tidak aktif sekarang").setCancelable(false).setPositiveButton("Aktifkan", new DialogInterface.OnClickListener() {
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

        dialog.setTitle("Upload Data");
        dialog.setMessage("sedang mengirim");
        dialog.show();

        File f = new File(pictureFilePath);
        Uri picUri = Uri.fromFile(f);
        final String cloudFilePath = deviceIdentifier + picUri.getLastPathSegment();

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageRef = firebaseStorage.getReference();
        final StorageReference uploadeRef = storageRef.child("Data Gambar/" + UUID.randomUUID().toString());


        String etKomen = et_comment.getText().toString().trim();
        if (etKomen.matches("")) {
            dialog.dismiss();
            Snackbar.make(ctxView,
                    "komentar anda kosong!",
                    Snackbar.LENGTH_SHORT).show();
        } else
            uploadeRef.putFile(picUri).addOnFailureListener(new OnFailureListener() {
                public void onFailure(@NonNull Exception exception) {
                    Log.e(TAG, "Failed to upload picture to cloud storage");
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    String TempImageName = et_comment.getText().toString().trim();
                    String lat = tv_lati.getText().toString().trim();
                    String longi = tv_longi.getText().toString().trim();
                    dialog.dismiss();
                    Snackbar.make(ctxView,
                            "Gambar berhasil di upload ke Database!",
                            Snackbar.LENGTH_SHORT).show();

                    UploadInfo imageUploadInfo = new UploadInfo(TempImageName, (taskSnapshot.getUploadSessionUri().toString()), lat, longi);
                    Log.e(TAG, "Masalah di tv");
                    String ImageUploadId = databaseRef.push().getKey();
                    databaseRef.child(ImageUploadId).setValue(imageUploadInfo);

                    et_comment.setText("");
                    img_take.setImageResource(R.drawable.ic_camera_access);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.dismiss();
                    Toast.makeText(InputDataActivity.this, "Data gambar kosong!", Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    double dialogStatus = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    dialog.setMessage("Tunggu.. " + (int) dialogStatus + "%");
                }
            });
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

    /*private void uploadData() {
        final View ctxView = findViewById(R.id.layout_inputData);

        dialog.setTitle("Upload Data");
        dialog.setMessage("sedang mengirim");
        dialog.show();

        if (filePath != null) {
            dialog.setTitle("Upload Data");
            dialog.setMessage("Sedang mengirim data...");
            dialog.show();

            StorageReference ref = storageRef.child("Images/" + UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            dialog.dismiss();
                            Snackbar.make(ctxView,
                                    "Sukses!",
                                    Snackbar.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.dismiss();
                            Snackbar.make(ctxView,
                                    "Gagal mengirim data!",
                                    Snackbar.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            double dialogStatus = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            dialog.setMessage("Tunggu.." + (int) dialogStatus + "%");
                        }
                    });
        } else {
            //Snackbar.make(contextView, "Pressed!", Snackbar.LENGTH_SHORT).show();
            Toast.makeText(InputDataActivity.this, "Data gambar kosong!", Toast.LENGTH_SHORT).show();
        }
    }*/

    private void dispatchPictureTakerAction() {
        Intent takePic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePic.resolveActivity(getPackageManager()) != null) {
            File pictureFile = null;
            pictureFile = createPictureFile();

            if (pictureFile != null) {
                pathToFile = pictureFile.getAbsolutePath();
                Uri pictureUri = FileProvider.getUriForFile(InputDataActivity.this, "com.tehkotak.trailbleshooting.fileprovider", pictureFile);
                takePic.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri);
                startActivityForResult(takePic, 1);
            }
        }
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

        if (requestCode == 1 && resultCode == RESULT_OK) {
            File imgFile = new File(pictureFilePath);

            if (imgFile.exists()) {
                img_take.setImageURI(Uri.fromFile(imgFile));
            }
        }
    }
}


