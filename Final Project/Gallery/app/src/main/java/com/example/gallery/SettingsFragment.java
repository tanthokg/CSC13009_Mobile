package com.example.gallery;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.switchmaterial.SwitchMaterial;

public class SettingsFragment extends Fragment {
    private final Context context;
    SwitchMaterial darkModeSwitch;
    MainActivity main;

    public static SettingsFragment getInstance(Context context) {
        return new SettingsFragment(context);
    }

    private SettingsFragment(Context context) {
        this.context = context;
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
        SharedPreferences preferencesContainer = context.getSharedPreferences("app theme", Activity.MODE_PRIVATE);
        boolean switchChecked = false;
        if (preferencesContainer != null && preferencesContainer.contains("switch mode"))
            switchChecked = preferencesContainer.getBoolean("switch mode", false);
        darkModeSwitch.setChecked(switchChecked);

        darkModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                AppConfig.getInstance(context).setDarkMode(isChecked);
                /*SharedPreferences preferences = context.getSharedPreferences("app theme", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("switch mode", isChecked);
                editor.commit();*/
                main.onMsgFromFragToMain("SETTING-FLAG", String.valueOf(isChecked));
            }
        });

        return settingsFragment;
    }
}
