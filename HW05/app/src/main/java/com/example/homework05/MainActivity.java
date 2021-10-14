package com.example.homework05;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;

public class MainActivity extends FragmentActivity implements MainCallbacks {
    FragmentTransaction ft;
    FragmentRed redFragment;
    FragmentBlue blueFragment;


    private static final User[] users = {
            new User("Đoàn Thu Ngân", "19_3", "19120302", R.drawable.ic_launcher_background, 8),
            new User("Huỳnh Tấn Thọ", "19_3", "19120383", R.drawable.ic_launcher_background, 7),
            new User("Phan Đặng Diễm Uyên", "19_3", "19120426", R.drawable.ic_launcher_background, 9),
            new User("Sử Nhật Đăng", "19_3", "19120469", R.drawable.ic_launcher_background, 10),
            new User("Đỗ Thái Duy", "19_3", "19120492", R.drawable.ic_launcher_background, 9)
    };

    public static User getUserAtIndex(int index) {
        return users[index];
    }

    public static User[] getUsers() {
        return users;
    }

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
                redFragment.mainToFragment(FragmentRed.chosenUser);
            }
            catch (Exception e) {
                Log.e("ERROR", "fragmentToMain " + e.getMessage());
            }
        }
    }
}