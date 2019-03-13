package com.example.sadul.whatsupp.Activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;

import com.example.sadul.whatsupp.R;
import com.example.sadul.whatsupp.Util.ImageDownload;
import com.jsibbold.zoomage.ZoomageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ResimActivity extends AppCompatActivity {
    private String imageLink;
    ;
    ImageView kaydet,cik;
    ZoomageView resim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resim);
        imageLink=getIntent().getExtras().getString("imageLink");
        kaydet=(ImageView)findViewById(R.id.saveImg);
        cik=(ImageView)findViewById(R.id.backBigImg);
        resim=(ZoomageView) findViewById(R.id.imgBuyuk);
        Picasso.get().load(imageLink).into(resim);

        cik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        kaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageDownload ımageDownload= new ImageDownload();
                ımageDownload.imageDownload(getApplicationContext(),imageLink);

            }
        });
    }





}
