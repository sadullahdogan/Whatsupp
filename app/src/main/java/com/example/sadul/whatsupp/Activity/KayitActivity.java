package com.example.sadul.whatsupp.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sadul.whatsupp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class KayitActivity extends AppCompatActivity {

    EditText editTextEmail, editTextPassword,kullaniciAdi,dogumTarihi;
    Button buttonKayit;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    TextView giris;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayit);
        tanimla();
        buttonKayit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();

                if (editTextEmail.getText().toString().isEmpty() && editTextPassword.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Lütfen tüm alanları doldurun", Toast.LENGTH_LONG).show();
                } else {
                    kayitOl(email, password);

                }

            }
        });
        giris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(KayitActivity.this, GirisActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void tanimla() {
        editTextEmail = (EditText) findViewById(R.id.emailTxt);
        editTextPassword = (EditText) findViewById(R.id.passwordTxt);
        kullaniciAdi = (EditText) findViewById(R.id.usernameTxt);
        dogumTarihi = (EditText) findViewById(R.id.birthDate);
        buttonKayit = (Button) findViewById(R.id.kayitBtn);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference();
        giris = (TextView) findViewById(R.id.hesapKotrol);
        progressDialog= new ProgressDialog(KayitActivity.this);
        progressDialog.setMessage("Kayıt İşlemi devam Ediyor.");

    }

    public void kayitOl(String email, String password) {
        progressDialog.show();
        Log.i("Testtt", email + " " + password);
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    String username=kullaniciAdi.getText().toString();
                    String dgmTrh=dogumTarihi.getText().toString();
                    Map map=new HashMap<>();
                    map.put("picture","null");
                    map.put("username",username);
                    map.put("birthDate",dgmTrh);
                    map.put("about","Yaşıyor ama ölü sayılır");
                    databaseReference.child("Users").child(firebaseAuth.getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Intent intent = new Intent(KayitActivity.this, Main2Activity.class);
                                startActivity(intent);
                                finish();
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"Kayıt sırasında hata oluştu lütfen tekrar deneyin",Toast.LENGTH_LONG).show();

                            }
                        }
                    });

                } else {
                    Toast.makeText(getApplicationContext(), "Kayıt olma esnasında bir problem oluştu", Toast.LENGTH_LONG).show();
                }
            }
        });
        progressDialog.dismiss();

    }
}
