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

    private TextView resultUsername;
    private TextView resultPassword;
    private TextView resultBirthdate;
    private TextView resultGender;
    private TextView resultHobbies;
    Button btnExit;

    //Matching the components in register_form.xml with ResultActivity attribute
    private void initComponents() {

        //Match the components
        resultUsername = (TextView) findViewById(R.id.resultUsername);
        resultPassword = (TextView) findViewById(R.id.resultPassword);
        resultBirthdate = (TextView) findViewById(R.id.resultBirthdate);
        resultGender = (TextView) findViewById(R.id.resultGender);
        resultHobbies = (TextView) findViewById(R.id.resultHobbies);
        btnExit = (Button) findViewById(R.id.btnExit);

        //Click btnExit will kill ResultActivity and RegsiterActivity
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
            }
        });
    }

    //Get data from RegisterActivity and set them to screen
    private void loadDataFromRegisterForm() {
        Intent data = getIntent();
        Bundle bundle = data.getExtras();

        //Replace original password with '*'
        StringBuilder password = new StringBuilder("");
        int passwordSize = bundle.getString("password").length();
        for (int i = 0; i < passwordSize; ++i) {
            password.append('*');
        }

        //Set data to screen
        resultUsername.setText("Username: " + bundle.getString("username"));
        resultPassword.setText("Password: " + password.toString());
        resultBirthdate.setText("Birthdate: " + bundle.getString("birthdate"));
        resultGender.setText("Gender: " + bundle.getString("gender"));
        resultHobbies.setText("Hobbies: " + bundle.getString("hobbies"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_form);
        initComponents();
        loadDataFromRegisterForm();
    }

}