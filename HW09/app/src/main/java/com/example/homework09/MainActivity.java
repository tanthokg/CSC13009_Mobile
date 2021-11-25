package com.example.homework09;

import androidx.annotation.Nullable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {
    String[] newspaper = {"Thanhnien", "VnExpress", "Tuoitre"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView txtMsg = (TextView) findViewById(R.id.txtMsg);
        txtMsg.setText("NEWS APP");

        GridView listView = (GridView) findViewById(R.id.myGridView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, newspaper);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, ShowCategories.class);
                intent.putExtra("newspaper", newspaper[i]);
                startActivity(intent);
            }
        });
    }
}