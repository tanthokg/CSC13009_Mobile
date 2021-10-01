package com.example.registerform;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Calendar;

public class RegisterActivity extends Activity {
    private CheckBox cbTennis, cbFutbal, cbOther;
    private RadioGroup rgGender;

    private String gender = "", hobbies = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_form);

        EditText edtUsername = (EditText) findViewById(R.id.edtUsername);
        EditText edtPassword1 = (EditText) findViewById(R.id.edtPassword1);
        EditText edtPassword2 = (EditText) findViewById(R.id.edtPassword2);
        EditText edtBirthdate = (EditText) findViewById(R.id.edtBirthdate);

        cbTennis = (CheckBox) findViewById(R.id.cbTennis);
        cbFutbal = (CheckBox) findViewById(R.id.cbFutbal);
        cbOther = (CheckBox) findViewById(R.id.cbOthers);
        rgGender = (RadioGroup) findViewById(R.id.rgGender);

        Calendar calendar = Calendar.getInstance();
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        final int month = calendar.get(Calendar.MONTH);
        final int year = calendar.get(Calendar.YEAR);

        Button btnPickDate = (Button) findViewById(R.id.btnPickDate);
        btnPickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(RegisterActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month += 1;
                        String date = dayOfMonth + "/" + month + "/" + year;
                        edtBirthdate.setText(date);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        Button btnReset = (Button) findViewById(R.id.btnReset);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtUsername.setText("");
                edtPassword1.setText("");
                edtPassword2.setText("");
                edtBirthdate.setText("");
                cbTennis.setChecked(false);
                cbFutbal.setChecked(false);
                cbOther.setChecked(false);
                rgGender.clearCheck();
            }
        });

        getGender();
        Button btnSignup = (Button) findViewById(R.id.btnSignup);
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtUsername.getText().toString().isEmpty() || edtPassword1.getText().toString().isEmpty() || edtPassword2.getText().toString().isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Please fill in the required fields",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                if (!edtPassword1.getText().toString().equals(edtPassword2.getText().toString())) {
                    Toast.makeText(RegisterActivity.this, "Please re-type your password.",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                if (edtBirthdate.getText().toString().isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Please choose your birthdate.",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                getHobbies();
                Bundle bundle = new Bundle();
                bundle.putString("username", edtUsername.getText().toString());
                bundle.putString("password", edtPassword1.getText().toString());
                bundle.putString("birthdate", edtBirthdate.getText().toString());
                bundle.putString("gender", gender);
                bundle.putString("hobbies", hobbies);

                Intent intent = new Intent(RegisterActivity.this, ResultActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void getGender() {
        rgGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbMale) gender = "Male";
                else if (checkedId == R.id.rbFemale) gender = "Female";
            }
        });
    }

    private void getHobbies() {
        hobbies = "";
        if (cbTennis.isChecked()) hobbies += "tennis, ";
        if (cbFutbal.isChecked()) hobbies += "futbal, ";
        if (cbOther.isChecked()) hobbies += "others, ";
        if (hobbies.length() > 2)
            hobbies = hobbies.substring(0, hobbies.length() - 2);
    }
}