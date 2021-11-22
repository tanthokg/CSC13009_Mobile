package com.example.homework08;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

    private Student[] _students = new Student[5];
    private Class[] _classes = new Class[1];

    public Student[] get_students() {
        return _students;
    }
    public Class[] get_classes() {
        return _classes;
    }

    public void getDataFromDatabase() {
        SQLiteDatabase database = main.getDatabase();

        Cursor cursor = database.rawQuery("select * from HOCSINH", null);
        cursor.moveToPosition(-1);
        int i = 0;
        while (cursor.moveToNext()) {
            _students[i] = new Student(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getInt(2),
                    cursor.getFloat(3));
            i++;
        }

        _students[0].setThumbnailID(R.drawable.avatar_ngan);
        _students[1].setThumbnailID(R.drawable.avatar_tho);
        _students[2].setThumbnailID(R.drawable.avatar_uyen);
        _students[3].setThumbnailID(R.drawable.avatar_dang);
        _students[4].setThumbnailID(R.drawable.avatar_duy);

        cursor = database.rawQuery("select * from LOPHOC", null);
        cursor.moveToPosition(-1);
        i = 0;
        while (cursor.moveToNext()) {
            _classes[i] = new Class(cursor.getInt(0), cursor.getString(1));
            i++;
        }
        main.setStudents(_students);
        main.setClasses(_classes);
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
            getDataFromDatabase();
        }
        catch (IllegalStateException e) {
            throw new IllegalStateException("MainActivity must implement callbacks");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout layout_blue = (LinearLayout) inflater.inflate(R.layout.layout_blue, null);

        txtViewBlue = layout_blue.findViewById(R.id.txtViewBlue);
        listView = layout_blue.findViewById(R.id.listViewBlue);

        CustomAdapter adapter = new CustomAdapter(context, R.layout.layout_blue_item, _students);
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