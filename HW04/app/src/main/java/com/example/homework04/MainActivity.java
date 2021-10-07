package com.example.homework04;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {
    private TextView txtMsg, txtName, txtStudentID;
    private ListView studentListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtMsg = (TextView) findViewById(R.id.txtMsg);
        studentListView = (ListView) findViewById(R.id.studentListView);

        User[] users = {
                new User("Đoàn Thu Ngân", "19120302", R.drawable.ngan),
                new User("Huỳnh Tấn Thọ", "19120383", R.drawable.tho),
                new User("Phan Đặng Diễm Uyên", "19120426", R.drawable.uyen),
                new User("Sử Nhật Đăng", "19120469", R.drawable.dang),
                new User("Đỗ Thái Duy", "19120492", R.drawable.duy)};
        CustomAdapter customAdapter = new CustomAdapter(this, R.layout.custom_list_item, users);
        studentListView.setAdapter(customAdapter);
        studentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                txtMsg.setText("You choose: " + users[position].getName());
            }
        });
    }
}