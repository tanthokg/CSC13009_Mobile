package com.example.homework09;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ShowCategories extends Activity {
    ArrayAdapter<String> adapterMainSubjects;
    ListView myMainListView;
    Context context;
    String[][] thanhnienUrls = {
            {"https://thanhnien.vn/rss/the-gioi-66.rss", "Thế giới"},
            {"https://thanhnien.vn/rss/thoi-su-4.rss", "Thời sự"},
            {"https://thanhnien.vn/rss/van-hoa-93.rss", "Văn hóa"}
    };
    String[][] vnexpressUrls = {
            {"https://vnexpress.net/rss/the-gioi.rss", "Thế giới"},
            {"https://vnexpress.net/rss/thoi-su.rss", "Thời sự"},
            {"https://vnexpress.net/rss/giao-duc.rss", "Giáo dục"}
    };
    String[][] tuoitreUrls = {
            {"https://tuoitre.vn/rss/khoa-hoc.rss", "Khoa học"},
            {"https://tuoitre.vn/rss/nhip-song-so.rss", "Nhịp sống số"},
            {"https://tuoitre.vn/rss/the-gioi.rss", "Thế giới"}
    };


    String [][] myUrlCaptionMenu = thanhnienUrls;

    String[] myUrlCaption = new String[myUrlCaptionMenu.length];
    String[] myUrlAddress = new String[myUrlCaptionMenu.length];
    public static String niceDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("EE MMM d, yyyy",
                Locale.US);
        return sdf.format(new Date());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        String newspaper = intent.getStringExtra("newspaper");
        Toast.makeText(this, newspaper, Toast.LENGTH_SHORT).show();
        switch (newspaper) {
            case "Thanhnien":
                myUrlCaptionMenu = thanhnienUrls;
                break;
            case "VnExpress":
                myUrlCaptionMenu = vnexpressUrls;
                break;
            case "Tuoitre":
                myUrlCaptionMenu = tuoitreUrls;
                break;
            default:
                break;
        }

        for (int i = 0; i < myUrlAddress.length; i++) {
            myUrlAddress[i] = myUrlCaptionMenu[i][0];
            myUrlCaption[i] = myUrlCaptionMenu[i][1];
        }

        context = getApplicationContext();
        // this.setTitle("NPR Headline News\n" + niceDate() );
        TextView txtMsg = (TextView) findViewById(R.id.txtMsg);
        txtMsg.setText(("Channels in " + newspaper).toUpperCase(Locale.ROOT));

        // user will tap on a ListView’s row to request category’s headlines
        myMainListView = (ListView)this.findViewById(R.id.myListView);
        myMainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> _av, View _v, int _index, long _id) {
                String urlAddress = myUrlAddress[_index], urlCaption = myUrlCaption[_index];
                //create an Intent to talk to activity: ShowHeadlines
                Intent callShowHeadlines = new Intent(ShowCategories.this, ShowHeadlines.class);
                //prepare a Bundle and add the input arguments: url & caption
                Bundle myData = new Bundle();
                myData.putString("urlAddress", urlAddress);
                myData.putString("urlCaption", urlCaption);
                myData.putString("newspaper", newspaper);
                callShowHeadlines.putExtras(myData); startActivity(callShowHeadlines);
            }
        });
        // fill up the Main-GUI’s ListView with main news categories
        adapterMainSubjects = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, myUrlCaption);
        myMainListView.setAdapter(adapterMainSubjects);
    }
}