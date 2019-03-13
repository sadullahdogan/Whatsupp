package com.example.sadul.whatsupp.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sadul.whatsupp.Fragment.OtherProfileFragment;
import com.example.sadul.whatsupp.R;
import com.example.sadul.whatsupp.Util.ChangeFragment;
import com.example.sadul.whatsupp.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class IstekAdapter extends RecyclerView.Adapter<IstekAdapter.MyViewHolder> {

    List<String> userList;
    Activity activity;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    Context context;
    String userKey;

    public IstekAdapter(List<String> userList, Activity activity, Context context) {
        this.userList = userList;
        this.activity = activity;
        this.context = context;
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        userKey = firebaseAuth.getUid();
    }


    //layout tanımlaması
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.istek_layout, viewGroup, false);
        return new MyViewHolder(view);
    }

    //viewlara setlemeler yapılc
    @Override
    public void onBindViewHolder(final MyViewHolder myViewHolder, final int i) {
        //myViewHolder.isim.setText(userList.get(i).toString());
        databaseReference.child("Users").child(userList.get(i).toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (!user.getPicture().equals("null")) {
                    Picasso.get().load(user.getPicture()).into(myViewHolder.resim);

                }
                else
                {
                    myViewHolder.resim.setImageResource(R.drawable.user_default);
                }
                myViewHolder.isim.setText(user.getUsername());
                myViewHolder.kabulEt.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        kabulEt(userKey,userList.get(i));
                        userList.remove(i);
                    }
                });
                myViewHolder.reddet.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        reddet(userKey,userList.get(i));
                        userList.remove(i);

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void reddet(final String userKey, final String otherKey) {
        databaseReference.child("Arkadaslik").child(otherKey).child(userKey).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(context, "Arkadaşlık isteği reddedildi", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(context, "Reddetme işlemi sırasında hata oluştu.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public  void kabulEt(final String userKey,final String otherKey){
        databaseReference.child("Arkadaslik").child(otherKey).child(userKey).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                   // databaseReference.child("Friends").child("")
                    final Date currentTime = Calendar.getInstance().getTime();
                    SimpleDateFormat postFormater = new SimpleDateFormat("MMMM dd, yyyy");

                    final String newDateStr = postFormater.format(currentTime);
                    databaseReference.child("Friends").child(userKey).child(otherKey).child("Eklenme Tarhi").setValue(newDateStr).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                databaseReference.child("Friends").child(otherKey).child(userKey).child("Eklenme Tarhi").setValue(newDateStr).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(context, "Arkadaşlık isteği kabul edildi.", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });

                            }
                            else {
                                Toast.makeText(context, "İşlem sırasında hata oluştu.", Toast.LENGTH_LONG).show();
                            }
                        }
                    });


                } else {
                    Toast.makeText(context, "İşlem sırasında hata oluştu.", Toast.LENGTH_LONG).show();
                }
            }
        });

    }


    //adapteri oluşturulcak listenin sizeı
    @Override
    public int getItemCount() {
        return userList.size();
    }



    //view tanımlamaları
    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView isim;
        CircleImageView resim;
        ImageView kabulEt, reddet;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            isim = (TextView) itemView.findViewById(R.id.usernameTextViewReq);
            resim = (CircleImageView) itemView.findViewById(R.id.userImageReq);
            kabulEt = (ImageView) itemView.findViewById(R.id.istekKabul);
            reddet = (ImageView) itemView.findViewById(R.id.istekReddet);


        }
    }


}
