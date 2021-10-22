package com.example.homework05;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.style.AlignmentSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentBlue extends Fragment implements FragmentCallbacks {
    MainActivity main;
    Context context = null;
    ListView listView;
    TextView txtViewBlue;

    private Student[] _students = {
            new Student("19120302", "Đoàn Thu Ngân", "19_3", 9, R.drawable.avatar_ngan),
            new Student("19120383", "Huỳnh Tấn Thọ", "19_3", 10, R.drawable.avatar_tho),
            new Student("19120426", "Phan Đặng Diễm Uyên", "19_3",9,R.drawable.avatar_uyen),
            new Student("19120469", "Sử Nhật Đăng", "19_3", 10,R.drawable.avatar_dang),
            new Student("19120492", "Đỗ Thái Duy", "19_3", 9, R.drawable.avatar_duy)
    };

    public Student[] get_students() {
        return _students;
    }

    public static FragmentBlue newInstance(String strArg) {
        FragmentBlue fragment = new FragmentBlue();
        Bundle args = new Bundle();
        args.putString("strArg1", strArg);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            context = getActivity();
            main = (MainActivity) getActivity();
        }
        catch (IllegalStateException e) {
            throw new IllegalStateException("MainActivity must implement callbacks");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout layout_blue = (LinearLayout) inflater.inflate(R.layout.layout_blue, null);

        txtViewBlue = (TextView) layout_blue.findViewById(R.id.txtViewBlue);
        listView = (ListView) layout_blue.findViewById(R.id.listViewBlue);

        CustomerAdapter adapter = new CustomerAdapter(context, R.layout.layout_blue_item, _students);
        listView.setAdapter(adapter);

        listView.setSelection(0);
        listView.smoothScrollToPosition(0);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                txtViewBlue.setText(_students[position].getStudentID());
                for (int index = 0; index < _students.length; index++) {
                    if (position != index)
                        listView.getChildAt(index).setBackgroundColor(Color.WHITE);
                }
                listView.getChildAt(position).setBackgroundColor(Color.parseColor("#FCDADA"));
                main.onMsgFromFragToMain("BLUE", position);
            }
        });

        return layout_blue;
    }

    @Override
    public void onMsgFromMainToFragment(int position) {
        for (int i = 0; i < _students.length; i++) {
            if (i!= position)
                listView.getChildAt(i).setBackgroundColor(Color.WHITE);
        }
        listView.getChildAt(position).setBackgroundColor(Color.parseColor("#FCDADA"));
        txtViewBlue.setText(_students[position].getStudentID());
    }
}