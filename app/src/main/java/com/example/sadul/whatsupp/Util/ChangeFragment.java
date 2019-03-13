package com.example.sadul.whatsupp.Util;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.example.sadul.whatsupp.R;

public class ChangeFragment {
    private Context context;

    Activity activity;
    public ChangeFragment(Context context) {
        this.context = context;
    }
    public void change(Fragment fragment){
        ((FragmentActivity)context).getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentLayout,fragment,"fragmeny")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();


    }
    public void changeWithParameter(Fragment fragment,String key){
        Bundle bundle= new Bundle();
        bundle.putString("userKey",key);
        fragment.setArguments(bundle);
        ((FragmentActivity)context).getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentLayout,fragment,"fragmeny")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();

    }

}
