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
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.Calendar;

public class RegisterActivity extends Activity {

    private EditText edtUsername;
    private EditText edtPassword1;
    private EditText edtPassword2;
    private EditText edtBirthdate;
    private CheckBox cbTennis;
    private CheckBox cbFutbal;
    private CheckBox cbOther;
    private RadioGroup rgGender;
    private Button btnPickDate;
    private Button btnReset;
    private Button btnSignup;

    //Initialize for btnPickDate to select date formally
    private void initPickDate() {
        btnPickDate = (Button) findViewById(R.id.btnPickDate);
        Calendar calendar = Calendar.getInstance();
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        final int month = calendar.get(Calendar.MONTH);
        final int year = calendar.get(Calendar.YEAR);

        btnPickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        RegisterActivity.this, new DatePickerDialog.OnDateSetListener() {
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
    }

    //Initialize for btnReset to clear all RegisterActivity data
    private void initReset() {
        btnReset = (Button) findViewById(R.id.btnReset);

        //Clear text while pressing btnReset
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

                //Reset gender to "Male"
                rgGender.check(R.id.rbMale);
            }
        });
    }

    //Validate data while pressing btnSignup
    boolean isValidData() {

        //Username field Or Password field Or Retype password field is empty => invalid
        if (edtUsername.getText().toString().isEmpty() ||
                edtPassword1.getText().toString().isEmpty() ||
                edtPassword2.getText().toString().isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Please fill in the required fields",
                    Toast.LENGTH_LONG).show();
            return false;
        }

        //Password != Retype password => invalid
        if (!edtPassword1.getText().toString().equals(edtPassword2.getText().toString())) {
            Toast.makeText(RegisterActivity.this, "Please re-type your password.",
                    Toast.LENGTH_LONG).show();
            return false;
        }

        //Birthday field empty => invalid
        if (edtBirthdate.getText().toString().isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Please choose your birthdate.",
                    Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    //Get data from 3 hobby checkboxes
    private String getHobbies() {
        StringBuilder hobbies = new StringBuilder("");
        if (cbTennis.isChecked()) hobbies.append("tennis, ");
        if (cbFutbal.isChecked()) hobbies.append("futbal, ");
        if (cbOther.isChecked()) hobbies.append("others, ");
        if (hobbies.length() > 2) {
            hobbies.setLength(hobbies.length() - 2);
        }
        return hobbies.toString();
    }

    //Return the text of the selected gender
    private String getGender() {
        int selectedGenderId = rgGender.getCheckedRadioButtonId();
        RadioButton selectedGender = (RadioButton)findViewById(selectedGenderId);
        return selectedGender.getText().toString();
    }

    //Initialize for btnSignup to save register data and transfer to ResultActiviy
    private void initSignup() {
        btnSignup = (Button) findViewById(R.id.btnSignup);
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //If invalid data => throw warning and do nothing
                if (!isValidData())
                {
                    return;
                }

                Bundle bundle = new Bundle();
                bundle.putString("username", edtUsername.getText().toString());
                bundle.putString("password", edtPassword1.getText().toString());
                bundle.putString("birthdate", edtBirthdate.getText().toString());
                bundle.putString("gender", getGender());
                bundle.putString("hobbies", getHobbies());

                Intent intent = new Intent(RegisterActivity.this, ResultActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    //Initialize for rgGender to get selected gender
    private void initGender() {
        rgGender = (RadioGroup) findViewById(R.id.rgGender);

        //default value of gender is "Male"
        rgGender.check(R.id.rbMale);
    }

    //Initialize for ALL components
    private void initComponents() {
        edtUsername = (EditText) findViewById(R.id.edtUsername);
        edtPassword1 = (EditText) findViewById(R.id.edtPassword1);
        edtPassword2 = (EditText) findViewById(R.id.edtPassword2);
        edtBirthdate = (EditText) findViewById(R.id.edtBirthdate);
        cbTennis = (CheckBox) findViewById(R.id.cbTennis);
        cbFutbal = (CheckBox) findViewById(R.id.cbFutbal);
        cbOther = (CheckBox) findViewById(R.id.cbOthers);

        initPickDate();
        initReset();
        initSignup();
        initGender();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_form);
        initComponents();
    }



}