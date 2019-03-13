package com.example.sadul.whatsupp.Activity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.example.sadul.whatsupp.Fragment.AnaSayfaFragment;
import com.example.sadul.whatsupp.Fragment.KullaniciProfilFragment;
import com.example.sadul.whatsupp.Fragment.NotificationFragment;
import com.example.sadul.whatsupp.Services.CheckMessages;
import com.example.sadul.whatsupp.Util.ChangeFragment;
import com.example.sadul.whatsupp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Main2Activity extends AppCompatActivity {

    ChangeFragment changeFragment;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    public static Context cnt;
    CheckMessages checkMessages;
    Intent mServiceIntent;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    changeFragment.change(new AnaSayfaFragment());
                    return true;
                case R.id.navigation_profil:
                    changeFragment.change(new KullaniciProfilFragment());

                    return true;
                case R.id.navigation_notifications:

                    return true;
                case R.id.navigation_logout:
                    logout();
                    return true;
            }
            return false;
        }
    };

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i("isMyServiceRunning?", true + "");
                return true;
            }
        }
        Log.i("isMyServiceRunning?", false + "");
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        tanimla();
        cnt=getApplicationContext();
        kontrol();
        checkMessages=new CheckMessages(cnt);
        mServiceIntent=new Intent(cnt,CheckMessages.class);
        if (!isMyServiceRunning(checkMessages.getClass())) {
            startService(mServiceIntent);
        }

        changeFragment.change(new AnaSayfaFragment());


    }
    public void logout(){
        firebaseAuth.signOut();
        Intent intent=new Intent(Main2Activity.this,GirisActivity.class);
        startActivity(intent);
        stopService(mServiceIntent);
        finish();



    }
    public void tanimla(){

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        changeFragment= new ChangeFragment(Main2Activity.this);
        changeFragment.change(new AnaSayfaFragment());
    }
    public void kontrol(){
        if(firebaseUser==null){
            Intent intent=new Intent(Main2Activity.this,GirisActivity.class);
            startActivity(intent);
            finish();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(mServiceIntent);
    }
}
