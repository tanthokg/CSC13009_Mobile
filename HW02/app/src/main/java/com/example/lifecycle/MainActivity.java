package com.example.lifecycle;

import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends Activity {
    private Button btnExit;
    private TextView txtChosenColor;
    private EditText edtColor;

    private ConstraintLayout screen;
    private Context context;
    private final String preferenceName = "backgroundColor";
    private final int duration = Toast.LENGTH_SHORT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        Toast.makeText(context, "On Create", duration).show();

        edtColor = findViewById(R.id.edtColor);
        btnExit = findViewById(R.id.btnExit);
        txtChosenColor = findViewById(R.id.txtChosenColor);
        screen = findViewById(R.id.mainScreen);


        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        edtColor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                String color = s.toString().toLowerCase(Locale.US);
                txtChosenColor.setText(color);
                setBackgroundColor(color, screen);
            }
        });

    }

    private void setBackgroundColor(String color, ConstraintLayout screen) {
        // Set background to white
        if (color.isEmpty() || color.contains("default")) screen.setBackgroundColor(0xffffffff);

        // Set background color according to input text
        if (color.contains("ngan")) screen.setBackgroundColor(0xff120302);
        if (color.contains("tho")) screen.setBackgroundColor(0xff120383);
        if (color.contains("uyen")) screen.setBackgroundColor(0xff120426);
        if (color.contains("dang")) screen.setBackgroundColor(0xff120469);
        if (color.contains("duy")) screen.setBackgroundColor(0xff120492);
    }

    private void storeColorData() {
        SharedPreferences colorContainer = getSharedPreferences(preferenceName, Activity.MODE_PRIVATE);
        SharedPreferences.Editor colorEditor = colorContainer.edit();
        String key = "chosenBackgroundColor", newColorValue = edtColor.getText().toString();

        colorEditor.putString(key, newColorValue);
        colorEditor.commit();
    }

    private void updateUsingStoredColorData() {
        SharedPreferences colorContainer = getSharedPreferences(preferenceName, Activity.MODE_PRIVATE);
        String key = "chosenBackgroundColor", defaultValue = "default";

        if (colorContainer != null && colorContainer.contains(key)) {
            String color = colorContainer.getString(key, defaultValue);
            setBackgroundColor(color, screen);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateUsingStoredColorData();
        Toast.makeText(this, "On Start", duration).show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Toast.makeText(this, "On Restart", duration).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Toast.makeText(this, "On Stop", duration).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, "On Resume", duration).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // To keep the previously chosen color if the user re-enter the app and exit immediately.
        // If you want to change the color back to white, please input "default".
        if (!edtColor.getText().toString().isEmpty())
            storeColorData();
        Toast.makeText(this, "On Pause", duration).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "On Destroy", duration).show();
    }

}