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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.DialogFragment;

import Helper.HashingHelper;

public class HideChangePasswordFragment extends DialogFragment {


    public static String Tag = "HideChangePassword";
    public static final String FLAG = "HIDE-CHANGE-PASSWORD-FLAG";
    public static final String CHANGE_SUCCESS = "OK";
    public static final String OPEN_FORM = "OPEN";
    public static final String CLEAR_PASSWORD = "CLEAR";

    //Error to toast into album fragment
    private String _error = "Error in change password";
    public String error() {return _error;}


    private String _oldPassword;
    private String _newPassword;
    private String _retypeNewPassword;

    private EditText _oldPasswordField;
    private EditText _newPasswordField;
    private EditText _retypePasswordField;

    private Button _showHideButtonOldPassword;
    private Button _showHideButtonNewPassword;
    private Button _showHideButtonRetypeNewPassword;

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
        _oldPasswordField = (EditText) view.findViewById(R.id.oldPasswordFieldChange);
        _newPasswordField = (EditText) view.findViewById(R.id.newPasswordFieldChange);
        _retypePasswordField = (EditText) view.findViewById(R.id.retypeNewPasswordFieldChange);

        _showHideButtonOldPassword = (Button) view.findViewById(R.id.showHideButtonOldPasswordChange);
        _showHideButtonNewPassword = (Button) view.findViewById(R.id.showHideButtonNewPasswordChange);
        _showHideButtonRetypeNewPassword = (Button) view.findViewById(R.id.showHideButtonRetypeNewPasswordChange);

        //Click Button to show/hide old password field
        _showHideButtonOldPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (_showHideButtonOldPassword.getText().toString().equals("Show")) {
                    _oldPasswordField.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    _showHideButtonOldPassword.setText("Hide");

                } else {
                    _oldPasswordField.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    _showHideButtonOldPassword.setText("Show");

                }

            }
        });

        //Click Button to show/hide new password field
        _showHideButtonNewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (_showHideButtonNewPassword.getText().toString().equals("Show")) {
                    _newPasswordField.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    _showHideButtonNewPassword.setText("Hide");

                } else {
                    _newPasswordField.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    _showHideButtonNewPassword.setText("Show");

                }

            }
        });

        //Click Button to show/hide retype-new password field
        _showHideButtonRetypeNewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (_showHideButtonRetypeNewPassword.getText().toString().equals("Show")) {
                    _retypePasswordField.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    _showHideButtonRetypeNewPassword.setText("Hide");

                } else {
                    _retypePasswordField.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    _showHideButtonRetypeNewPassword.setText("Show");

                }

            }
        });


        int color = ResourcesCompat.getColor(getResources(), R.color.black, null);
        if (AppConfig.getInstance(getContext()).getDarkMode()) {
            color = ResourcesCompat.getColor(getResources(), R.color.white, null);
        }

        _showHideButtonNewPassword.setTextColor(color);
        _showHideButtonRetypeNewPassword.setTextColor(color);
        _showHideButtonOldPassword.setTextColor(color);

    }

    //check if the old password, new password and retype-password are valid
    boolean isValid() {
        _oldPassword = _oldPasswordField.getText().toString();
        _newPassword = _newPasswordField.getText().toString();
        _retypeNewPassword = _retypePasswordField.getText().toString();

        if (null == _oldPassword || null == _newPassword || null == _retypeNewPassword ||
            _oldPassword.equals("") || _newPassword.equals("") || _retypeNewPassword.equals(""))
        {
            return false;
        }

        if (!_newPassword.equals(_retypeNewPassword))
        {
            return false;
        }

        try {
           if (!HashingHelper.SHA256(_oldPassword).equals(getCurrentPassword()))
           {
               return false;
           }
        } catch (Exception e) {
            Log.e("Error in hashing!", e.getMessage());
        }

        return true;
    }


    //Get the current hash password
    private String getCurrentPassword() {
        SharedPreferences mPref = this.getActivity().
                getSharedPreferences(HideLoginFragment.PREF_NAME, Context.MODE_PRIVATE);
        return mPref.getString(HideLoginFragment.PREF_PASS_NAME, null);
    }

    void savePassword() {
        SharedPreferences mPref = this.getActivity().
                getSharedPreferences(HideLoginFragment.PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mPref.edit();
        try {
            editor.putString(HideLoginFragment.PREF_PASS_NAME, HashingHelper.SHA256(_newPassword));
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
        View view = inflater.inflate(R.layout.hide_change_password_form, null);
        createDialog.setView(view)
                .setPositiveButton("Change", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (isValid()) {
                            savePassword();
                            dismiss();
                            main.onMsgFromFragToMain(FLAG, CHANGE_SUCCESS);
                            Toast.makeText(getActivity(), "Change password successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), _error, Toast.LENGTH_SHORT).show();
                        }

                    }
                });

        createDialog.setView(view)
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
