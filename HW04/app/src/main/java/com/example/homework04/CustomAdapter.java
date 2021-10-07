package com.example.homework04;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter extends ArrayAdapter<User> {
    private final Context context;
    private final User[] users;

    public CustomAdapter(Context context, int layoutToBeInflated, User[] users) {
        super(context, R.layout.custom_list_item, users);
        this.context = context;
        this.users = users;
    }

    @Override
    public int getCount() {
        return users.length;
    }

    @Override
    public User getItem(int position) {
        return users[position];
    }

    /*@Override
    public long getItemId(int position) {
        return position;
    }*/

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_list_item, null);
        TextView txtName = (TextView) view.findViewById(R.id.txtName);
        TextView txtStudentID = (TextView) view.findViewById(R.id.txtStudentID);
        ImageView icon = (ImageView) view.findViewById(R.id.icon);

        txtName.setText(users[position].getName());
        txtStudentID.setText(users[position].getStudentID());
        icon.setImageResource(users[position].getThumbnailID());
        return view;
    }
}
