package com.example.registerform;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ResultActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_form);
        TextView resultUsername = (TextView) findViewById(R.id.resultUsername);
        TextView resultPassword = (TextView) findViewById(R.id.resultPassword);
        TextView resultBirthdate = (TextView) findViewById(R.id.resultBirthdate);
        TextView resultGender = (TextView) findViewById(R.id.resultGender);
        TextView resultHobbies = (TextView) findViewById(R.id.resultHobbies);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        String password = "";
        for (int i = 0; i < bundle.getString("password").length(); ++i)
            password += '*';
        resultUsername.setText("Username: " + bundle.getString("username"));
        resultPassword.setText("Password: " + password);
        resultBirthdate.setText("Birthdate: " + bundle.getString("birthdate"));
        resultGender.setText("Gender: " + bundle.getString("gender"));
        resultHobbies.setText("Hobbies: " + bundle.getString("hobbies"));

        Button btnExit = (Button) findViewById(R.id.btnExit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
            }
        });
    }
}