package com.example.sadul.whatsupp.Activity;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sadul.whatsupp.Adapter.MesajAdapter;
import com.example.sadul.whatsupp.R;
import com.example.sadul.whatsupp.Util.RandomName;
import com.example.sadul.whatsupp.models.User;
import com.example.sadul.whatsupp.models.mesajModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ChatActivity extends AppCompatActivity {

    String userName, otherName;
    ImageView back, send, sendMedia;
    TextView userText;
    EditText mesaj;
    RecyclerView chatMessage;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    FirebaseAuth firebaseAuth;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    MesajAdapter mesajAdapter;
    List<mesajModel> list;
    List<mesajModel> list2;
    List<String> mesajKeyList;
    Uri filePath;
    int count12 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        userName = getIntent().getStringExtra("username");
        otherName = getIntent().getStringExtra("othername");
        back = (ImageView) findViewById(R.id.backImage);
        send = (ImageView) findViewById(R.id.sendTextMsg);
        sendMedia = (ImageView) findViewById(R.id.sendImage);
        userText = (TextView) findViewById(R.id.chatUserName);
        mesaj = (EditText) findViewById(R.id.textMesaj);
        list = new ArrayList<>();
        list2 = new ArrayList<>();
        mesajAdapter = new MesajAdapter(ChatActivity.this, list, ChatActivity.this, userName);
        chatMessage = (RecyclerView) findViewById(R.id.chatRecycl);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(ChatActivity.this, 1);
        chatMessage.setAdapter(mesajAdapter);
        chatMessage.setLayoutManager(layoutManager);
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        mesajKeyList = new ArrayList<>();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = mesaj.getText().toString();
                mesaj.setText("");
                mesajGonder(text, "text");
            }
        });
        sendMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                galeriAc();
            }
        });
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference();
        getOtherName();

        loadMesaj();


    }

    private void galeriAc() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 5);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 5 && resultCode == Activity.RESULT_OK) {


            filePath = data.getData();
            storageReference.child("Medias").child(RandomName.getRandomString() + ".jpg").putFile(filePath).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Yüklendi", Toast.LENGTH_LONG).show();
                        final Map map = new HashMap<>();
                        task.getResult().getMetadata().getReference().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                filePath = Uri.parse(task.getResult().toString());
                                mesajGonder(filePath.toString(), "resim");


                            }
                        });


                    } else {
                        Toast.makeText(getApplicationContext(), "Yüklenemedi", Toast.LENGTH_LONG).show();
                    }
                }
            });
            //Log.i("Data12",filePath+"");
        }

    }

    public void getOtherName() {
        reference.child("Users").child(otherName).child("username").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userText.setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void mesajGonder(String text, String type) {
        final String key = reference.child("Mesajlar").child(userName).child(otherName).push().getKey();
        final Map messageMap = new HashMap();
        messageMap.put("text", text);
        messageMap.put("from", userName);
        messageMap.put("seen", "false");
        messageMap.put("type", type);
        reference.child("Mesajlar").child(userName).child(otherName).child(key).setValue(messageMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                reference.child("Mesajlar").child(otherName).child(userName).child(key).setValue(messageMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
            }
        });


    }


    public void loadMesaj() {

        reference.child("Mesajlar").child(userName).child(otherName).addChildEventListener(new ChildEventListener() {


            @RequiresApi(api = Build.VERSION_CODES.O)
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                mesajModel msjModel = dataSnapshot.getValue(mesajModel.class);
                String as = dataSnapshot.getKey();
                mesajKeyList.add(as);
                list.add(msjModel);

                msjModel.setSeen("true");
                list2.add(msjModel);

                mesajAdapter.notifyDataSetChanged();

                chatMessage.scrollToPosition(list.size() - 1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
