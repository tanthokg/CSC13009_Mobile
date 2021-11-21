package com.example.gallery;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.google.android.material.switchmaterial.SwitchMaterial;

public class SettingsFragment extends Fragment {
    public static SettingsFragment getInstance()
    {
        return new SettingsFragment();
    }

    SwitchMaterial darkMode;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View settingsFragment = inflater.inflate(R.layout.settings_fragment, container, false);

        darkMode = (SwitchMaterial) settingsFragment.findViewById(R.id.sDarkMode);
        SharedPreferences preferencesContainer = getActivity().getSharedPreferences("app theme", Activity.MODE_PRIVATE);
        boolean switchChecked = false;
        if (preferencesContainer != null && preferencesContainer.contains("switch mode"))
            switchChecked = preferencesContainer.getBoolean("switch mode", false);
        darkMode.setChecked(switchChecked);

        darkMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences preferences = getActivity().getSharedPreferences("app theme", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("switch mode", isChecked);
                editor.commit();

                main.onMsgFromFragToMain("SETTING-FLAG", String.valueOf(isChecked));
            }
        });

        return settingsFragment;
    }
}
