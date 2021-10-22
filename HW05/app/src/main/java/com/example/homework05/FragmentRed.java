package com.example.homework05;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentRed extends Fragment implements FragmentCallbacks{
    MainActivity main;
    TextView txtStudentIDRed, txtNameRed, txtClassRed, txtScoreRed;
    Button btnFirst, btnPrevious, btnNext, btnLast;
    private Student[] students;
    int currentPosition;

    public static FragmentRed newInstance(String strArg1) {
        FragmentRed fragment = new FragmentRed();
        Bundle bundle = new Bundle();
        bundle.putString("arg1", strArg1);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!(getActivity() instanceof MainCallbacks)) {
            throw new IllegalStateException("Activity must implement MainCallbacks");
        }
        main = (MainActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout layout_red = (LinearLayout) inflater.inflate(R.layout.layout_red, null);
        txtStudentIDRed = (TextView) layout_red.findViewById(R.id.txtStudentIDRed);
        txtNameRed = (TextView) layout_red.findViewById(R.id.txtNameRed);
        txtClassRed = (TextView) layout_red.findViewById(R.id.txtClassRed);
        txtScoreRed = (TextView) layout_red.findViewById(R.id.txtScoreRed);

        try {
            Bundle arguments = getArguments();
            txtStudentIDRed.setText(arguments.getString("arg1",""));
        }
        catch (Exception e) {
            Log.e("RED BUNDLE ERROR - ","" + e.getMessage());
        }

        students = main.students;

        btnFirst = (Button) layout_red.findViewById(R.id.btnFirst);
        btnFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                main.onMsgFromFragToMain("RED", 0);
            }
        });

        btnPrevious = (Button) layout_red.findViewById(R.id.btnPrevious);
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                main.onMsgFromFragToMain("RED", currentPosition-1);
            }
        });

        btnNext = (Button) layout_red.findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                main.onMsgFromFragToMain("RED", currentPosition+1);
            }
        });

        btnLast = (Button) layout_red.findViewById(R.id.btnLast);
        btnLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                main.onMsgFromFragToMain("RED", students.length - 1);
            }
        });

        return layout_red;
    }

    @Override
    public void onMsgFromMainToFragment(int position) {
        currentPosition = position;
        txtStudentIDRed.setText(students[position].getStudentID());
        txtNameRed.setText(students[position].getName());
        txtClassRed.setText(students[position].getClassID());
        txtScoreRed.setText(Float.toString(students[position].getAvgScore()));
        updateButton(students, position);
    }

    public void updateButton(Student[] students, int position) {
        if (position == 0)
            btnPrevious.setEnabled(false);
        else
            btnPrevious.setEnabled(true);

        if (position == (students.length - 1))
            btnNext.setEnabled(false);
        else
            btnNext.setEnabled(true);
    }
}