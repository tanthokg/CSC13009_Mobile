package com.example.homework09;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import java.util.ArrayList;

public class MainActivity extends Activity {

    private  final String newspaperNames [] = {"Thanh Niên", "VnExpress", "Tuổi Trẻ", "VietNamNet", "Người Lao Động", "VTV News"};
    private  final  int[] newspaperImageIds = {
            R.drawable.thanhnien,
            R.drawable.vnexpress,
            R.drawable.tuoitre,
            R.drawable.vietnamnet,
            R.drawable.nguoilaodong,
            R.drawable.vtvnews
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView txtMsg = (TextView) findViewById(R.id.txtMsg);
        txtMsg.setText("NEWS APP");
        initViews();

    }

    private void initViews(){
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.RecView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),2);
        recyclerView.setLayoutManager(layoutManager);
        ArrayList<Newspaper> newspaperArrayList = prepareData();
        DataAdapter adapter = new DataAdapter(this, newspaperArrayList);
        recyclerView.setAdapter(adapter);
    }

    private ArrayList<Newspaper> prepareData(){

        ArrayList<Newspaper> newspaperArrayList = new ArrayList<>();
        for(int i = 0; i < newspaperNames.length; i++)
        {
            Newspaper newspaper = new Newspaper();
            newspaper.setNewspaperName(newspaperNames[i]);
            newspaper.setImageid(newspaperImageIds[i]);
            newspaperArrayList.add(newspaper);
        }
        return newspaperArrayList;
    }
}

