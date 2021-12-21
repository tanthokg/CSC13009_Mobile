package com.example.gallery;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.switchmaterial.SwitchMaterial;

public class SettingsFragment extends Fragment {
    private SwitchMaterial darkModeSwitch, trashModeSwitch;
    private MainActivity main;

    public static SettingsFragment getInstance() {
        return new SettingsFragment();
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

        ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Gallery");
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayUseLogoEnabled(true);
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View settingsFragment = inflater.inflate(R.layout.settings_fragment, container, false);

        darkModeSwitch = (SwitchMaterial) settingsFragment.findViewById(R.id.sDarkMode);
        trashModeSwitch = (SwitchMaterial) settingsFragment.findViewById(R.id.sTrashMode);

        darkModeSwitch.setChecked(AppConfig.getInstance(getContext()).getDarkMode());
        trashModeSwitch.setChecked(AppConfig.getInstance(getContext()).getTrashMode());

        darkModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                AppConfig.getInstance(getContext()).setDarkMode(isChecked);
                main.onMsgFromFragToMain("SETTING-FLAG", String.valueOf(isChecked));
            }
        });
        trashModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                AppConfig.getInstance(getContext()).setTrashMode(isChecked);
                Toast.makeText(getContext(), "Trash Mode: " + (isChecked?"ON":"OFF"), Toast.LENGTH_SHORT).show();
            }
        });

        return settingsFragment;
    }
}
