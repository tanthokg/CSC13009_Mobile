package com.example.homework05;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;

public class MainActivity extends FragmentActivity implements MainCallbacks {
    FragmentTransaction ft;
    FragmentRed redFragment;
    FragmentBlue blueFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ft = getSupportFragmentManager().beginTransaction();
        blueFragment = FragmentBlue.newInstance("first_blue");
        ft.replace(R.id.main_holder_blue, blueFragment);
        ft.commit();

        ft = getSupportFragmentManager().beginTransaction();
        redFragment = FragmentRed.newInstance("first_red");
        ft.replace(R.id.main_holder_red, redFragment);
        ft.commit();


    }

    @Override
    public void fragmentToMain(String sender, String strValue) {
        if (sender.equals("RED")) {}
        if (sender.equals("BLUE")) {
            try {
                redFragment.mainToFragment("\nSender: " + sender + "\nMsg: " + strValue);
            }
            catch (Exception e) {
                Log.e("ERROR", "fragmentToMain " + e.getMessage());
            }
        }
    }
}