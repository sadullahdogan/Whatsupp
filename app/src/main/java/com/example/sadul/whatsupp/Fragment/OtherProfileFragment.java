package com.example.sadul.whatsupp.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sadul.whatsupp.R;
import com.example.sadul.whatsupp.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class OtherProfileFragment extends Fragment {


    CircleImageView profilResmi;
    EditText kullaniciAdi, dogumTarihi, hakkında;
    View view;
    Button arkadasEkleBtn, arkadasSilBtn;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference, referenceArkadaslik;
    String userKey;
    User user;
    boolean istekKontrol;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        tanimla();
        bilgileriGetir();
        return view;

    }

    public void arkadasIstegiIptal(final String adderId, final String receiverId) {
        databaseReference.child("Arkadaslik").child(adderId).child(receiverId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getContext(), "İsteğiniz başarıyla iptal edildi.", Toast.LENGTH_LONG).show();
                    arkadasEkleBtn.setText("Arkadaş Ekle");
                    istekKontrol = false;

                } else {
                    Toast.makeText(getContext(), "İptal işlemi sırasında hata oluştu.", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void arkadasIstegiGonder(final String adderId, final String receiverId) {

        databaseReference.child("Arkadaslik").child(adderId).child(receiverId).child("Tip").setValue("Gönderme").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getContext(), "İsteğiniz başarıyla gönderildi.", Toast.LENGTH_LONG).show();
                    arkadasEkleBtn.setText("İstek Gönderildi");
                    istekKontrol = true;

                } else {
                    Toast.makeText(getContext(), "Arkaşlık isteği gönderilemedi", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void bilgileriGetir() {


        databaseReference.child("Users").child(userKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                Log.i("Users", user.toString());
                kullaniciAdi.setText(user.getUsername());
                dogumTarihi.setText(user.getBirthDate());
                hakkında.setText(user.getAbout());
                if (!user.getPicture().equals("null")) {
                    Picasso.get().load(user.getPicture()).into(profilResmi);
                } else {
                    profilResmi.setImageResource(R.drawable.user_default);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    View.OnClickListener arkadasSil = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            arkadasKaldir(firebaseAuth.getUid(), userKey);

        }
    };

    private void arkadasKaldir(final String uid, final String userKey) {
        databaseReference.child("Friends").child(uid).child(userKey).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    databaseReference.child("Friends").child(userKey).child(uid).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getContext(), "Kişiyi arkadaşlıktan çıkardınız", Toast.LENGTH_LONG).show();
                                arkadasEkleBtn.setVisibility(View.VISIBLE);
                                arkadasSilBtn.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
                }
            }
        });
    }

    View.OnClickListener arkadasEkle = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!istekKontrol) {
                arkadasIstegiGonder(firebaseAuth.getUid(), userKey);
            } else {
                arkadasIstegiIptal(firebaseAuth.getUid(), userKey);

            }
        }
    };

    public void tanimla() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        referenceArkadaslik = firebaseDatabase.getReference();
        userKey = getArguments().getString("userKey");
        firebaseAuth = FirebaseAuth.getInstance();
        profilResmi = (CircleImageView) view.findViewById(R.id.profile_imageO);
        kullaniciAdi = (EditText) view.findViewById(R.id.kullaniciAdiTxtO);
        dogumTarihi = (EditText) view.findViewById(R.id.dogumTarihiTxtO);
        hakkında = (EditText) view.findViewById(R.id.hakkındaTxtO);
        arkadasEkleBtn = (Button) view.findViewById(R.id.arkadasEkleBtn);
        arkadasEkleBtn.setOnClickListener(arkadasEkle);

        arkadasSilBtn = (Button) view.findViewById(R.id.arkadasSilBtn);
        arkadasSilBtn.setOnClickListener(arkadasSil);


        databaseReference.child("Arkadaslik").child(firebaseAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(userKey)) {
                    arkadasEkleBtn.setText("İstek Gönderildi");
                    istekKontrol = true;
                } else {
                    istekKontrol = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        databaseReference.child("Friends").child(firebaseAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(userKey)) {
                    arkadasSilBtn.setVisibility(View.VISIBLE);
                    arkadasEkleBtn.setVisibility(View.INVISIBLE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
