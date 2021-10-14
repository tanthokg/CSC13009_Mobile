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
    Button btnNext, btnPrevious, btnFirst, btnLast;
    public static int chosenUser;

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
        txtStudentID = (TextView) view_layout_red.findViewById(R.id.red_txtStudentId);
        txtStudentName = (TextView) view_layout_red.findViewById(R.id.red_txtStudentName);
        txtStudentClass = (TextView) view_layout_red.findViewById(R.id.red_txtStudentClass);
        txtStudentScore = (TextView) view_layout_red.findViewById(R.id.red_txtStudentScore);

        btnFirst = (Button) view_layout_red.findViewById(R.id.btnFirst);
        btnLast = (Button) view_layout_red.findViewById(R.id.btnLast);
        btnNext = (Button) view_layout_red.findViewById(R.id.btnNext);
        btnPrevious = (Button) view_layout_red.findViewById(R.id.btnPrevious);

        btnFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != MainActivity.getUsers())
                {
                    FragmentBlue.setCurrentUserIndex(0);
                    mainToFragment(0);
                }
            }
        });
        btnLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != MainActivity.getUsers())
                {
                    int lastIndex = MainActivity.getUsers().length - 1;
                    FragmentBlue.setCurrentUserIndex(lastIndex);
                    mainToFragment(lastIndex);
                }
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != MainActivity.getUsers())
                {
                    int nextIndex = FragmentBlue.getCurrentUserIndex() + 1;
                    int lastIndex = MainActivity.getUsers().length - 1;
                    if (FragmentBlue.getCurrentUserIndex() < lastIndex) {
                        FragmentBlue.setCurrentUserIndex(nextIndex);
                    }
                    mainToFragment(nextIndex);
                }
            }
        });
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != MainActivity.getUsers())
                {
                    int previousIndex = FragmentBlue.getCurrentUserIndex() - 1;
                    if (FragmentBlue.getCurrentUserIndex() > 0) {
                        FragmentBlue.setCurrentUserIndex(previousIndex);
                    }
                    mainToFragment(previousIndex);
                }
            }
        });


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
    public void mainToFragment(int index) {
        User user = MainActivity.getUserAtIndex(index);
        txtStudentID.setText(user.getStudentID());
        txtStudentName.setText(user.getName());
        txtStudentClass.setText(user.getStudentClass());
        txtStudentScore.setText(Float.toString(user.getAvg()));
        updateButtons(index);
        FragmentBlue.setCurrentUserIndex(index);

    }

    private void updateButtons(int index) {
        int lastIndex = MainActivity.getUsers().length - 1;
        if (index > 0) {
            btnPrevious.setEnabled(true);
            btnNext.setEnabled(index != lastIndex);
        }
        else {
            btnPrevious.setEnabled(false);
            btnNext.setEnabled(true);
        }
    }
}
