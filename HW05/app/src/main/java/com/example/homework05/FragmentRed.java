package com.example.homework05;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.view.menu.MenuView;
import androidx.fragment.app.Fragment;

public class FragmentRed extends Fragment implements FragmentCallbacks {
    MainActivity main;
    TextView txtStudentID, txtStudentName, txtStudentClass, txtStudentScore;

    public static FragmentRed newInstance(String strArg) {
        FragmentRed fragment = new FragmentRed();
        Bundle bundle = new Bundle();
        bundle.putString("arg1", strArg);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!(getActivity() instanceof MainCallbacks)) {
            throw new IllegalStateException("Activity must implement MainCallbacks");
        }
        main = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout view_layout_red = (LinearLayout) inflater.inflate(R.layout.layout_red, null);
        txtStudentID = view_layout_red.findViewById(R.id.red_txtStudentId);

        try {
            Bundle arguments = getArguments();
            txtStudentID.setText(arguments.getString("arg1", "NO VALUE"));
        }
        catch (Exception e) {
            Log.e("RED BUNDLE ERROR – ", "" + e.getMessage());
        }
        return view_layout_red;
    }

    @Override
    public void mainToFragment(String strValue) {
        txtStudentID.setText(strValue);
    }
}
