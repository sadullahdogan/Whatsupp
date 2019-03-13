package com.example.sadul.whatsupp.Util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageDownload {



    public  void imageDownload(Context ctx,String url) {
        try {


            Picasso.get()
                    .load(url)
                    .into(getTarget(getFileName("ImageName")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public  String getFileName(String fileName) {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + fileName);
        return uriSting;

    }


    //target to save
    private  Target getTarget(final String fileName) {
        Target target = new Target() {

            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            File file = new File(getFileName(fileName));
                            if (file.exists()) {
                                file.delete();
                            }
                            file.createNewFile();
                            FileOutputStream fileoutputstream = new FileOutputStream(file);
                            ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.PNG, 60, bytearrayoutputstream);
                            fileoutputstream.write(bytearrayoutputstream.toByteArray());
                            fileoutputstream.close();
                        } catch (IOException e) {

                        }
                    }
                }).start();

            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }



            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        return target;
    }



    public static String getFileFullPath(String fileName) {
        try {
            if (fileName != null && !fileName.isEmpty()) {
                String base = Environment.getExternalStorageDirectory().getAbsolutePath().toString();
                return "file://" + base + "/Images/" + fileName;
            } else return "";
        } catch (Exception e) {
            return "";
        }
    }
}
