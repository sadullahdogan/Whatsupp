package com.example.sadul.whatsupp.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class GirisActivity extends AppCompatActivity {

    EditText editTextEmail, editTextPassword;
    Button buttonGiris;
    FirebaseAuth firebaseAuth;
    TextView kayit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris);
        tanimla();
        buttonGiris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();
                //if(ed_text.isEmpty() || ed_text.length() == 0 || ed_text.equals("") || ed_text == null)
                if (editTextEmail.getText().toString().isEmpty() && editTextPassword.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Lütfen tüm alanları doldurun", Toast.LENGTH_LONG).show();
                } else {
                    login(email, password);

                }
            }
        });
        kayit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GirisActivity.this, KayitActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void tanimla() {
        editTextEmail = (EditText) findViewById(R.id.grsEmailTxt);
        editTextPassword = (EditText) findViewById(R.id.grsPasswordTxt);
        buttonGiris = (Button) findViewById(R.id.girisBtn);
        firebaseAuth = FirebaseAuth.getInstance();
        kayit = (TextView) findViewById(R.id.hesapKotrolKayit);
    }

    public void login(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(GirisActivity.this, Main2Activity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Email yada şifre Hatalı", Toast.LENGTH_LONG).show();

                }
            }
        });


    }
}
