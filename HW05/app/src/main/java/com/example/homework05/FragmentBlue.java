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

    private final User[] users = {
            new User("Đoàn Thu Ngân", "19_3", "19120302", R.drawable.ic_launcher_background, 8),
            new User("Huỳnh Tấn Thọ", "19_3", "19120383", R.drawable.ic_launcher_background, 7),
            new User("Phan Đặng Diễm Uyên", "19_3", "19120426", R.drawable.ic_launcher_background, 9),
            new User("Sử Nhật Đăng", "19_3", "19120469", R.drawable.ic_launcher_background, 10),
            new User("Đỗ Thái Duy", "19_3", "19120492", R.drawable.ic_launcher_background, 9)
        };

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

        CustomAdapter customAdapter = new CustomAdapter(context, R.layout.layout_blue_item, users);
        listView.setAdapter(customAdapter);

        listView.setSelection(0);
        listView.smoothScrollToPosition(0);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                txtMsg.setText("Selected student: " + position);
                main.fragmentToMain("BLUE", "Selected Row: " + position);
            }
        });
        return layout_blue;
    }
}
