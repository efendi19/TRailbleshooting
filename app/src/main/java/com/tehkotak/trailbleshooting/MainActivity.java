package com.tehkotak.trailbleshooting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.robertlevonyan.views.customfloatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.os.Environment.getExternalStoragePublicDirectory;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;

    ImageView img_take;

    private int[] tabIcons = {
            R.drawable.ic_home1,
            R.drawable.ic_profile1
    };

    Dialog dialogCamera;

    private FloatingActionButton fabOpenActInputData;
    String pathToFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home);

        if (Build.VERSION.SDK_INT >= 23) {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        }

        dialogCamera = new Dialog(this);

        tabLayout = findViewById(R.id.tab_id);
        viewPager = findViewById(R.id.vp_id);
        fabOpenActInputData = findViewById(R.id.fab_id);

        img_take = findViewById(R.id.imageeeee_id);

        adapter = new ViewPagerAdapter(getSupportFragmentManager(), this);

        //Adding fragments
        adapter.AddFragment(new HomeFragment(), "Home", tabIcons[0]);
        adapter.AddFragment(new ProfileFragment(), "Profile", tabIcons[1]);

        //adapter set up
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        highLightCurrentTab(0);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

        });

        fabOpenActInputData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ShowDialog(view);
                startActivity(new Intent(MainActivity.this, InputDataActivity.class));
            }
        });
    }

    public void ShowDialog(View v) {
        TextView btnClose, btnOpen;
        dialogCamera.setContentView(R.layout.custom_dialog);
        btnClose = (TextView) dialogCamera.findViewById(R.id.btnClose_id);
        btnOpen = (TextView) dialogCamera.findViewById(R.id.btnOpen_id);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogCamera.dismiss();
            }
        });

        btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //startActivity(new Intent(MediaStore.ACTION_IMAGE_CAPTURE));
                dispatchPictureTakerAction();

                dialogCamera.dismiss();
            }
        });

        dialogCamera.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogCamera.show();
    }


    private void dispatchPictureTakerAction() {
        Intent takePic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePic.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            photoFile = createPhotoFile();

            if (photoFile != null) {
                pathToFile = photoFile.getAbsolutePath();
                Uri photoUri = FileProvider.getUriForFile(MainActivity.this, "com.tehkotak.trailbleshooting.fileprovider", photoFile);
                takePic.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePic, 1);
            }

        }

        /*Intent ganti = new Intent(MainActivity.this, InputDataActivity.class);
        ganti.putExtra("photo", R.id.imageeeee_id);
        ganti.putExtra("coba", "Kangeeen");
        startActivity(ganti);*/
    }

    private File createPhotoFile() {
        String name = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File storageDir = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            image = File.createTempFile(name, ".jpg", storageDir);
        } catch (IOException e) {
            Log.d("myLog", "Excep" + e.toString());
        }
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ImageView mg = findViewById(R.id.imageeeee_id);

        if (resultCode == RESULT_OK) {

            if (requestCode == 1) {

                Bitmap bitmap = BitmapFactory.decodeFile(pathToFile);
                mg.setImageBitmap(bitmap);

            }

        }

    }

    private void highLightCurrentTab(int position) {
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            assert tab != null;
            tab.setCustomView(null);
            tab.setCustomView(adapter.getTabView(i));
        }

        TabLayout.Tab tab = tabLayout.getTabAt(position);
        assert tab != null;
        tab.setCustomView(null);
        tab.setCustomView(adapter.getSelectedTabView(position));
    }
}
