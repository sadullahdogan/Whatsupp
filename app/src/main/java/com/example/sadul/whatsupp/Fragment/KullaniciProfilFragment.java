package com.example.sadul.whatsupp.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sadul.whatsupp.R;
import com.example.sadul.whatsupp.Util.ChangeFragment;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class KullaniciProfilFragment extends Fragment {
    CircleImageView profilResmi;
    EditText kullaniciAdi, dogumTarihi, hakkında;
    Button güncelle,arkadaslarBtn,bildirimlerBtn;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    Uri filePath;
    ProgressDialog progressDialog;
    ProgressDialog progressDialog2;
    boolean güncelleCheck;
    User user;
    ChangeFragment changeFragment;
    View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_kullanici_profil, container, false);
        tanimla();
        bilgileriGetir();
        güncelle.setOnClickListener(güncelleListener);
        profilResmi.setOnClickListener(resimListener);
        return view;


    }

    View.OnClickListener resimListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            galeriAc();
        }
    };

    private void galeriAc() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 5);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 5 && resultCode == Activity.RESULT_OK) {
            progressDialog.show();
            filePath = data.getData();
            storageReference.child("UserProfileImages").child(firebaseAuth.getUid() + ".jpg").putFile(filePath).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Yüklendi", Toast.LENGTH_LONG).show();
                        final Map map = new HashMap<>();
                        task.getResult().getMetadata().getReference().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                filePath = Uri.parse(task.getResult().toString());
                                map.put("picture", task.getResult().toString());
                                map.put("username", kullaniciAdi.getText().toString());
                                map.put("birthDate", dogumTarihi.getText().toString());
                                map.put("about", hakkında.getText().toString());
                                databaseReference.child("Users").child(firebaseAuth.getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            progressDialog.dismiss();
                                            Toast.makeText(view.getContext(), "Bilgileriniz başarıyla güncellendi", Toast.LENGTH_LONG).show();
                                        } else {
                                            progressDialog.dismiss();
                                            Toast.makeText(view.getContext(), "Bilgileriniz güncellenirken bir hatayla karşılaşıldı lütfen tekrar deneyin", Toast.LENGTH_LONG).show();

                                        }
                                    }
                                });

                            }
                        });


                    } else {
                        Toast.makeText(getContext(), "Yüklenemedi", Toast.LENGTH_LONG).show();
                    }
                }
            });
            //Log.i("Data12",filePath+"");
        }

    }
    View.OnClickListener arkadaslarListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            changeFragment.change(new ArkadaslarFragment());

        }
    };
    View.OnClickListener bildirimListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            changeFragment.change(new NotificationFragment());
        }
    };

    View.OnClickListener güncelleListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(güncelleCheck) {
                kullaniciAdi.setEnabled(true);
                dogumTarihi.setEnabled(true);
                hakkında.setEnabled(true);
                güncelle.setText("KAYDET");
                güncelleCheck=false;
            }
            else{
                kullaniciAdi.setEnabled(false);
                dogumTarihi.setEnabled(false);
                hakkında.setEnabled(false);
                güncelle.setText("GÜNCELLE");
                güncelleCheck=true;
                guncelle();
            }

        }
    };


    public void bilgileriGetir() {
        progressDialog2.show();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        databaseReference.child("Users").child(firebaseAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                Log.i("Users", user.toString());
                kullaniciAdi.setText(user.getUsername());
                dogumTarihi.setText(user.getBirthDate());
                hakkında.setText(user.getAbout());
                if (!user.getPicture().equals("null")) {
                    Picasso.get().load(user.getPicture()).into(profilResmi);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        progressDialog2.dismiss();


    }

    public void tanimla() {
        firebaseAuth = FirebaseAuth.getInstance();
        profilResmi = (CircleImageView) view.findViewById(R.id.profile_image);
        kullaniciAdi = (EditText) view.findViewById(R.id.kullaniciAdiTxt);
        dogumTarihi = (EditText) view.findViewById(R.id.dogumTarihiTxt);
        güncelle = (Button) view.findViewById(R.id.bilgiGüncelleBtn);
        arkadaslarBtn=(Button)view.findViewById(R.id.arkadaslarBtn);
        bildirimlerBtn=(Button)view.findViewById(R.id.bildirimBtn);
        changeFragment=new ChangeFragment(getContext());
        arkadaslarBtn.setOnClickListener(arkadaslarListener);
        bildirimlerBtn.setOnClickListener(bildirimListener);
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        hakkında = (EditText) view.findViewById(R.id.hakkındaTxt);
         progressDialog= new ProgressDialog(view.getContext());
        progressDialog.setMessage("Resim Yükleniyor Lütfen Bekleyin");
        progressDialog2= new ProgressDialog(view.getContext());
        progressDialog2.setMessage("Bilgiler Yükleniyor");
        güncelleCheck=true;


    }

    public void guncelle() {

        Map map = new HashMap<>();
        map.put("picture", user.getPicture());
        map.put("username", kullaniciAdi.getText().toString());
        map.put("birthDate", dogumTarihi.getText().toString());
        map.put("about", hakkında.getText().toString());
        databaseReference.child("Users").child(firebaseAuth.getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(view.getContext(), "Bilgileriniz başarıyla güncellendi", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(view.getContext(), "Bilgileriniz güncellenirken bir hatayla karşılaşıldı lütfen tekrar deneyin", Toast.LENGTH_LONG).show();

                }
            }
        });

    }


}
