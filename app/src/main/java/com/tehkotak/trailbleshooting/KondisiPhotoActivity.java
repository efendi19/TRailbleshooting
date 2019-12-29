package com.tehkotak.trailbleshooting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class KondisiPhotoActivity extends AppCompatActivity {

    ImageView img;
    TextView tv_;

    String image, kosong;
    int imagessss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kondisi_photo);

        img = findViewById(R.id.img_take_id);
        tv_ = findViewById(R.id.tv_kosong);

        /*if (getIntent().hasExtra("photo")) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(getIntent().getByteArrayExtra("photo"), 0,getIntent().getByteArrayExtra("photo").length );
            img.setImageBitmap(bitmap);
        }*/

        /*Intent intent = new Intent();
        Bitmap bitmap = (Bitmap) intent.getParcelableExtra("photo");
        img.setImageBitmap(bitmap);*/

        Intent intent = getIntent();
        //image = intent.getStringExtra("photo");
        imagessss = intent.getIntExtra("photo", 1);

        Picasso.get().load(imagessss).into(img);
        kosong = intent.getStringExtra("coba");

        tv_.setText(kosong);
    }
}
