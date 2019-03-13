package com.example.sadul.whatsupp.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sadul.whatsupp.Activity.ChatActivity;
import com.example.sadul.whatsupp.Fragment.OtherProfileFragment;
import com.example.sadul.whatsupp.R;
import com.example.sadul.whatsupp.Util.ChangeFragment;
import com.example.sadul.whatsupp.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {

    List<String> userList;
    Activity activity;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String userName;

    public UserAdapter(List<String> userList, Activity activity, Context context,String userName) {
        this.userList = userList;
        this.activity = activity;
        this.context = context;
        this.userName=userName;
    }

    Context context;


    //layout tanımlaması
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_layout, viewGroup, false);
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
                else{
                    myViewHolder.resim.setImageResource(R.drawable.user_default);
                }
                myViewHolder.isim.setText(user.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        myViewHolder.resim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeFragment changeFragment=new ChangeFragment(context);
                changeFragment.changeWithParameter(new OtherProfileFragment(),userList.get(i).toString());
            }
        });
        myViewHolder.isim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(activity,ChatActivity.class);
                intent.putExtra("username",userName);
                intent.putExtra("othername",userList.get(i).toString());
                activity.startActivity(intent);
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
        LinearLayout userAnaLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            isim = (TextView) itemView.findViewById(R.id.usernameTextView);
            resim = (CircleImageView) itemView.findViewById(R.id.userImage);
            userAnaLayout=(LinearLayout) itemView.findViewById(R.id.userAnaLayout);
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference();
        }
    }


}
