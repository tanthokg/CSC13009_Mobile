package com.example.homework05;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class FragmentBlue extends Fragment {
    MainActivity main;
    Context context = null;
    String message = "";
    private static int currentUserIndex;

    public static int getCurrentUserIndex() {
        return currentUserIndex;
    }

    public static void setCurrentUserIndex(int currentUserIndex) {
        FragmentBlue.currentUserIndex = currentUserIndex;
    }

    public static FragmentBlue newInstance(String strArg) {
        FragmentBlue fragment = new FragmentBlue();
        Bundle args = new Bundle();
        args.putString("strArg1", strArg);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            context = getActivity();
            main = (MainActivity) getActivity();
        }
        catch (IllegalStateException e) {
            throw new IllegalStateException("MainActivity must implement callbacks");
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout layout_blue = (LinearLayout) inflater.inflate(R.layout.layout_blue, null);
        final TextView txtMsg = (TextView) layout_blue.findViewById(R.id.txtMsg);
        ListView listView = (ListView) layout_blue.findViewById(R.id.studentListView);

        User[] users = MainActivity.getUsers();
        CustomAdapter customAdapter = new CustomAdapter(context, R.layout.layout_blue_item, users);
        listView.setAdapter(customAdapter);

        listView.setSelection(0);
        listView.smoothScrollToPosition(0);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentUserIndex = position;
                txtMsg.setText("Mã số: " + MainActivity.getUserAtIndex(currentUserIndex).getStudentID());
                FragmentRed.chosenUser = currentUserIndex;

                main.fragmentToMain("BLUE", "Selected Row: " + currentUserIndex);
            }
        });
        return layout_blue;
    }
}
