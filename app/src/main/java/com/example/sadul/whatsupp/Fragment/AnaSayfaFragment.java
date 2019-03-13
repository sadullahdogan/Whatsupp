package com.example.sadul.whatsupp.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sadul.whatsupp.Adapter.UserAdapter;
import com.example.sadul.whatsupp.R;
import com.example.sadul.whatsupp.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AnaSayfaFragment extends Fragment {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    View view;
    List<String> userList;
    RecyclerView userListRc;
    RecyclerView.LayoutManager layoutManager;
    UserAdapter userAdapter;
    FirebaseAuth firebaseAuth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_ana_sayfa, container, false);
        tanimla();

        getUsers();

        return view;


    }

    public void tanimla() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();//.child("Users");
        userList=new ArrayList<>();
        firebaseAuth=FirebaseAuth.getInstance();
        userAdapter= new UserAdapter(userList,getActivity(),getContext(),firebaseAuth.getUid());
        userListRc=(RecyclerView)view.findViewById(R.id.userListRcView);
        layoutManager=new GridLayoutManager(getContext(),2);
        userListRc.setLayoutManager(layoutManager);

        userListRc.setAdapter(userAdapter);

    }

    public void getUsers() {
        databaseReference.child("Users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(!dataSnapshot.getKey().equals(firebaseAuth.getUid())) {
                    userList.add(dataSnapshot.getKey());
                    userAdapter.notifyDataSetChanged();
                }


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
