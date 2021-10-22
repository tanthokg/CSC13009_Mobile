package com.example.homework05;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CustomerAdapter extends ArrayAdapter<Student> {
    private final Context context;
    private final Student[] students;

    public CustomerAdapter(Context context, int resource, Student[] students) {
        super(context, resource, students);
        this.context = context;
        this.students = students;
    }

    @Override
    public int getCount() {
        return students.length;
    }

    @Nullable
    @Override
    public Student getItem(int position) {
        return students[position];
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_blue_item,null);
        TextView txtStudentID = (TextView) view.findViewById(R.id.txtStudentIDBlue);
        ImageView avatar = (ImageView) view.findViewById(R.id.avatarBlue);

        txtStudentID.setText(students[position].getStudentID());
        avatar.setImageResource(students[position].getThumbnailID());
        return view;
    }
}
