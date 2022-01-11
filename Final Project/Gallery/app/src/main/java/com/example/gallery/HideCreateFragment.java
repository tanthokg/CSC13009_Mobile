package com.example.gallery;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.DialogFragment;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import Helper.HashingHelper;

public class HideCreateFragment extends DialogFragment {


    public static String Tag = "HideCreate";
    public static final String FLAG = "HIDE-CREATE-FLAG";
    public static final String CREATE_SUCCESS = "OK";
    public static final String OPEN_FORM = "OPEN";

    //Error to toast into album fragment
    private String _error;
    public String error() {return _error;}

    private String _password;
    private String _retypePassword;

    private EditText _passwordField;
    private EditText _retypePasswordField;
    private Button _showHideButtonPassword;
    private Button _showHideButtonRetypePassword;

    MainActivity main;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            main = (MainActivity) getActivity();
        }
        catch (IllegalStateException e) {
            throw new IllegalStateException("MainActivity must implement callbacks");
        }
    }


    private void initComponent(View view) {
        _passwordField = (EditText) view.findViewById(R.id.passwordFieldCreate);
        _retypePasswordField = (EditText) view.findViewById(R.id.retypePasswordFieldCreate);
        _showHideButtonPassword = (Button) view.findViewById(R.id.showHideButtonPasswordCreate);
        _showHideButtonRetypePassword = (Button) view.findViewById(R.id.showHideButtonRetypePasswordCreate);

        //Click Button to show/hide password field
        _showHideButtonPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (_showHideButtonPassword.getText().toString().equals("Show")) {
                    _passwordField.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    _showHideButtonPassword.setText("Hide");

                } else {
                    _passwordField.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    _showHideButtonPassword.setText("Show");

                }

            }
        });

        //Click Button to show/hide retype-password field
        _showHideButtonRetypePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (_showHideButtonRetypePassword.getText().toString().equals("Show")) {
                    _retypePasswordField.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    _showHideButtonRetypePassword.setText("Hide");

                } else {
                    _retypePasswordField.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    _showHideButtonRetypePassword.setText("Show");

                }

            }
        });


        int color = ResourcesCompat.getColor(getResources(), R.color.black, null);
        if (AppConfig.getInstance(getContext()).getDarkMode()) {
            color = ResourcesCompat.getColor(getResources(), R.color.white, null);
        }
        _showHideButtonRetypePassword.setTextColor(color);
        _showHideButtonPassword.setTextColor(color);

    }

    //check if the password and retype-password is valid
    boolean isValid() {
        _password = _passwordField.getText().toString();
        _retypePassword = _retypePasswordField.getText().toString();

        if (null == _password || null == _retypePassword ||
        _password.equals("") || _retypePassword.equals("")) {
            _error = "Error: password or retype password must not be empty";
            return false;
        }

        if (!_password.equals(_retypePassword)) {
            _error = "Error: password and retype password must be the same";
            return false;
        }

        return true;
    }

    void savePassword() {
        SharedPreferences mPref = this.getActivity().
                getSharedPreferences(HideLoginFragment.PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mPref.edit();
        try {
            editor.putString(HideLoginFragment.PREF_PASS_NAME, HashingHelper.SHA256(_password));
            editor.commit();
        } catch (Exception e) {
            Log.e("Error in hashing!", e.getMessage());
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder createDialog = new AlertDialog.Builder(getContext(), R.style.AlertDialogCustom);
        /*if (AppConfig.getInstance(getContext()).getDarkMode()) {
            createDialog =
                    new AlertDialog.Builder(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_DARK);
        } else {
            createDialog =
                    new AlertDialog.Builder(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        }*/

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.hide_create_form, null);
        createDialog.setView(view)
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (isValid()) {
                            savePassword();
                            dismiss();
                            main.onMsgFromFragToMain(FLAG, CREATE_SUCCESS);
                            Toast.makeText(getActivity(), "Create password successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), _error, Toast.LENGTH_SHORT).show();
                        }

                    }
                });


        createDialog
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                });
        initComponent(view);
        return createDialog.create();
    }
}
