package com.example.gallery;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.switchmaterial.SwitchMaterial;

public class SettingsFragment extends Fragment {
    private SwitchMaterial darkModeSwitch, trashModeSwitch;
    private MainActivity main;
    private RelativeLayout relChange;
    private TextView timeSlideshow;
    private TextView changeTime;
    String[] list;
    int position;
    String time;

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
        relChange = (RelativeLayout) settingsFragment.findViewById(R.id.relChange);
        timeSlideshow = (TextView) settingsFragment.findViewById(R.id.timeSlideshow);
        changeTime = (TextView) settingsFragment.findViewById(R.id.changeTime);

        list = getContext().getResources().getStringArray(R.array.choice_items);

        darkModeSwitch.setChecked(AppConfig.getInstance(getContext()).getDarkMode());
        trashModeSwitch.setChecked(AppConfig.getInstance(getContext()).getTrashMode());
        timeSlideshow.setText("After " + AppConfig.getInstance(getContext()).getTimeLapse());

        if(AppConfig.getInstance(getContext()).getDarkMode())
            changeTime.setTextColor(getContext().getResources().getColor(R.color.white));

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

        time = AppConfig.getInstance(getContext()).getTimeLapse();
        position = Integer.parseInt(time.substring(0, 1)) - 1;
        relChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext(), R.style.AlertDialog);
                dialog.setTitle("Change time lapse duration of every slide show");
                dialog.setSingleChoiceItems(list, position, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        position = i;
                    }
                });
                dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String timeStr = list[position];
                        AppConfig.getInstance(getContext()).setTimeLapse(timeStr);
                        timeSlideshow.setText("After " + timeStr);
                        Toast.makeText(getContext(), "Slide duration: " + timeStr, Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                dialog.create().show();
            }
        });

        return settingsFragment;
    }
}
