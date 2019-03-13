package com.example.sadul.whatsupp.Services;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.sadul.whatsupp.Activity.ChatActivity;
import com.example.sadul.whatsupp.R;
import com.example.sadul.whatsupp.models.TopluMesajModel;
import com.example.sadul.whatsupp.models.mesajModel;

import android.support.annotation.RequiresApi;
import android.util.Log;

import com.example.sadul.whatsupp.Activity.Main2Activity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class CheckMessages extends Service {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    Context context;
    Notification.Builder builder;
    Notification notification;
    NotificationManager notificationManager;
    Ringtone r;
    boolean state;
    String otherUserName;

    public CheckMessages() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        context = Main2Activity.cnt;
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        state=false;
    }


    public CheckMessages(Context applicationContext) {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        context = applicationContext;
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        state=false;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void notifiy(String title,String text) {
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        String id = "my_channel_01";

// The user-visible name of the channel.
        CharSequence name = "Deneme";
        String description = "Tanımla";
        int importance = NotificationManager.IMPORTANCE_LOW;
        NotificationChannel mChannel = new NotificationChannel(id, name, importance);
        mChannel.setDescription(description);

        mChannel.enableLights(true);
// Sets the notification light color for notifications posted to this
// channel, if the device supports this feature.
        mChannel.setLightColor(Color.RED);

        mChannel.enableVibration(true);
        mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        r = RingtoneManager.getRingtone(getApplicationContext(), alarmSound);

        mChannel.setSound(alarmSound, null);

        notificationManager.createNotificationChannel(mChannel);


        builder = new Notification.Builder(context);


        builder.setSmallIcon(R.drawable.ic_launcher_background);
        builder.setAutoCancel(true);
        builder.setTicker("Bildirim Geldi");
        builder.setChannelId(id);
        builder.setContentText(text);
        getOtherName(title);
        builder.setContentTitle(otherUserName);
        Notification notification = builder.build();
        notificationManager.cancelAll();




        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("username",firebaseUser.getUid());
        intent.putExtra("othername",title);
        //Log.i("TEST123456",firebaseUser.getUid()+"-"+otherUser);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 100, intent, 0);
        builder.setContentIntent(pendingIntent);
        r.play();
        notificationManager.notify(100, notification);

    }
    public void getOtherName(String key) {

        databaseReference.child("Users").child(key).child("username").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               otherUserName =dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
if(firebaseUser==null){

    this.onDestroy();
}
else {
    databaseReference.child("Mesajlar").child(firebaseAuth.getUid()).addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                for (DataSnapshot mesaj : postSnapshot.getChildren()) {
                    final mesajModel msj = mesaj.getValue(mesajModel.class);
                    Log.i("MesajSeen", msj.getSeen());
                    if ((!msj.getFrom().equals(firebaseAuth.getUid())) && msj.getSeen().equals("false")) {
                        msj.setSeen("true");
                        getOtherName(msj.getFrom());
                        mesaj.getRef().setValue(msj).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(msj.getType().equals("resim")){
                                    notifiy(msj.getFrom(), "Resim");
                                }
                                else {
                                    notifiy(msj.getFrom(), msj.getText());
                                }

                            }
                        });
                    }
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });

}
        return START_STICKY;
    }

    @Override
    public void onDestroy() {

        if(firebaseUser==null){
            state=true;
        }
        super.onDestroy();
        if(!state) {
            Intent broadcastIntent = new Intent(this, SensorRestarterBroadcastReceiver.class);

            sendBroadcast(broadcastIntent);
        }


        //stopping the player when service is destroyed

    }


}
