package com.example.gallery;

import android.app.ActionBar;
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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import Helper.HashingHelper;

public class HideLoginFragment extends Fragment {

    static private HideLoginFragment _instance = null;

    public static String Tag = "HideLogin";
    public static final String PREF_NAME = "PASSWORD";
    public static final String PREF_PASS_NAME = "PASS";
    public static final String FLAG = "HIDE-LOGIN-FLAG";
    public static final String CORRECT_FLAG = "CORRECT";    //flag if the password is correct
    public static final String INCORRECT_FLAG = "INCORRECT"; //flag if the password is incorrect
    public static final String RESET_PASS_FLAG = "RESET";   //flag if the password is resetted

    private String _password;
    private EditText _passwordField;
    private Button _showHideButton;
    private Button _loginButton;
    private Button _resetButton;
    private Context _context;

    MainActivity main;

    private HideLoginFragment(Context context) {
        _context = context;
    }

    static public HideLoginFragment getInstance(Context context) {
        if (null == _instance) {
            _instance = new HideLoginFragment(context);
        }

        return _instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            main = (MainActivity) getActivity();
        }
        catch (IllegalStateException e) {
            throw new IllegalStateException("MainActivity must implement callbacks");
        }

        // Show the up-key back arrow and name folder on Action Bar
        setHasOptionsMenu(true);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((MainActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(false);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayUseLogoEnabled(true);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.hide_login_form, container, false);
        initComponents(view);
        return view;
    }


    //Check if the _password is correct
    boolean isAuthorized() {
        SharedPreferences mPref = this.getActivity().
                getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        String password = mPref.getString(PREF_PASS_NAME, null);

        //always correct if there is no password yet
        if (null == password)
        {
            return true;
        }

        try {
            if (HashingHelper.SHA256(_password).equals(password))
            {
                return true;
            }
        } catch (Exception e)
        {
            Log.e("Error in hashing!", e.getMessage());
        }

        return false;
    }

    private void initComponents(View view) {
        _passwordField = (EditText) view.findViewById(R.id.passwordField);

        _showHideButton = (Button) view.findViewById(R.id.showHideButton);
        _loginButton = (Button) view.findViewById(R.id.login_Login);
        _resetButton = (Button) view.findViewById(R.id.login_Reset);

        //Click Button to show/hide password field
        _showHideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (_showHideButton.getText().toString().equals("Show")) {
                    _passwordField.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    _showHideButton.setText("Hide");
                } else {
                    _passwordField.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    _showHideButton.setText("Show");
                }

            }
        });

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                _password = _passwordField.getText().toString();
                _passwordField.setText(null);
                //Empty password field
                if (null == _password)
                {
                    _password = "";
                }

                main.onMsgFromFragToMain(FLAG,
                        isAuthorized()?CORRECT_FLAG:INCORRECT_FLAG);
            }
        });

        _resetButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                AlertDialog.Builder confirmResetDialog = new AlertDialog.Builder(getContext(), R.style.AlertDialog);;
                /*if (AppConfig.getInstance(getContext()).getDarkMode()) {
                     confirmResetDialog =
                            new AlertDialog.Builder(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_DARK);
                } else {
                    confirmResetDialog =
                            new AlertDialog.Builder(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                }*/
                confirmResetDialog.setTitle("Confirm reset password");
                confirmResetDialog.setMessage("By resetting password, you can access \"Hide\" " +
                        "without typing password, but every pictures in \"Hide\" will be deleted" +
                        "\nDo you want to reset password?");

                confirmResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        main.onMsgFromFragToMain(FLAG, RESET_PASS_FLAG);
                    }
                });

                confirmResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                       //do nothing
                    }
                });
                confirmResetDialog.setCancelable(true);
                confirmResetDialog.show();
            }
        });

        int color = ResourcesCompat.getColor(getResources(), R.color.black, null);
        if (AppConfig.getInstance(getContext()).getDarkMode()) {
            color = ResourcesCompat.getColor(getResources(), R.color.white, null);
        }
        _resetButton.setTextColor(color);
        _showHideButton.setTextColor(color);
        _loginButton.setTextColor(color);
    }

}
