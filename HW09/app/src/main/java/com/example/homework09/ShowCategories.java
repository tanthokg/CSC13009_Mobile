package com.example.homework09;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
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
            {"https://thanhnien.vn/rss/cong-nghe-12.rss","Công nghệ"},
            {"https://thanhnien.vn/rss/game-315.rss","Game"},
            {"https://thanhnien.vn/rss/suc-khoe-65.rss","Sức khoẻ"},
            {"https://thanhnien.vn/rss/xe-317.rss","Xe"},
            {"https://thanhnien.vn/rss/thoi-trang-tre-319.rss","Thời trang trẻ"},
            {"https://thanhnien.vn/rss/giao-duc-26.rss","Giáo dục"},
            {"https://thanhnien.vn/rss/gioi-tre-69.rss","Giới trẻ"},
            {"https://thanhnien.vn/rss/tai-chinh-kinh-doanh-49.rss","Tài chính kinh doanh"},
            {"https://thanhnien.vn/rss/doi-song-17.rss","Đời sống"},
            {"https://thanhnien.vn/rss/the-thao-318.rss","Thể thao"},
            {"https://thanhnien.vn/rss/the-gioi-66.rss","Thế giới"},
            {"https://thanhnien.vn/rss/thoi-su-4.rss","Thời sự"}
    };
    String[][] vnexpressUrls = {
            {"https://vnexpress.net/rss/the-gioi.rss", "Thế giới"},
            {"https://vnexpress.net/rss/thoi-su.rss", "Thời sự"},
            {"https://vnexpress.net/rss/giao-duc.rss", "Giáo dục"},
            {"https://vnexpress.net/rss/kinh-doanh.rss","Kinh doạnh"},
            {"https://vnexpress.net/rss/giai-tri.rss","Giải trí"},
            {"https://vnexpress.net/rss/the-thao.rss","Thể thao"},
            {"https://vnexpress.net/rss/phap-luat.rss","Pháp luật"},
            {"https://vnexpress.net/rss/suc-khoe.rss","Sức khoẻ"},
            {"https://vnexpress.net/rss/du-lich.rss","Du lịch"},
            {"https://vnexpress.net/rss/khoa-hoc.rss","Khoa học"},
            {"https://vnexpress.net/rss/tam-su.rss","Tâm sự"},
            {"https://vnexpress.net/rss/y-kien.rss","Ý kiến"}
    };
    String[][] tuoitreUrls = {
            {"https://tuoitre.vn/rss/khoa-hoc.rss", "Khoa học"},
            {"https://tuoitre.vn/rss/giai-tri.rss", "Giải trí"},
            {"https://tuoitre.vn/rss/the-gioi.rss", "Thế giới"},
            {"https://tuoitre.vn/rss/kinh-doanh.rss","Kinh doanh"},
            {"https://tuoitre.vn/rss/xe.rss","Xe"},
            {"https://tuoitre.vn/rss/van-hoa.rss","Văn hoá"},
            {"https://tuoitre.vn/rss/the-thao.rss","Thể thao"},
            {"https://tuoitre.vn/rss/thoi-su.rss","Thời sự"},
            {"https://tuoitre.vn/rss/phap-luat.rss","Pháp luật"},
            {"https://tuoitre.vn/rss/giao-duc.rss","Giáo dục"},
            {"https://tuoitre.vn/rss/du-lich.rss","Du lịch"},
            {"https://tuoitre.vn/rss/suc-khoe.rss","Sức khoẻ"}

    };
    String[][] vietnamnetUrls = {
            {"https://vietnamnet.vn/rss/thoi-su-chinh-tri.rss","Chính trị"},
            {"https://vietnamnet.vn/rss/talkshow.rss","Talkshow"},
            {"https://vietnamnet.vn/rss/thoi-su.rss","Thời sự"},
            {"https://vietnamnet.vn/rss/kinh-doanh.rss","Kinh doanh"},
            {"https://vietnamnet.vn/rss/giai-tri.rss","Giải trí"},
            {"https://vietnamnet.vn/rss/the-gioi.rss","Thế giới"},
            {"https://vietnamnet.vn/rss/giao-duc.rss","Giáo dục"},
            {"https://vietnamnet.vn/rss/doi-song.rss","Đời sống"},
            {"https://vietnamnet.vn/rss/phap-luat.rss","Pháp luật"},
            {"https://vietnamnet.vn/rss/the-thao.rss","Thể thao"},
            {"https://vietnamnet.vn/rss/cong-nghe.rss","Công nghệ"},
            {"https://vietnamnet.vn/rss/suc-khoe.rss","Sức khoẻ"}

    };
    String[][] nguoiLaoDongUrls = {
            {"https://nld.com.vn/thoi-su.rss","Thời sự"},
            {"https://nld.com.vn/thoi-su-quoc-te.rss","Thời sự quốc tế"},
            {"https://nld.com.vn/cong-doan.rss","Công đoàn"},
            {"https://nld.com.vn/ban-doc.rss","Bạn đọc"},
            {"https://nld.com.vn/kinh-te.rss","Kinh tế"},
            {"https://nld.com.vn/suc-khoe.rss","Sức khoẻ"},
            {"https://nld.com.vn/giao-duc-khoa-hoc.rss","Giáo dục"},
            {"https://nld.com.vn/phap-luat.rss","Pháp luật"},
            {"https://nld.com.vn/giai-tri.rss","Giải trí"},
            {"https://nld.com.vn/the-thao.rss","Thể thao"},
            {"https://nld.com.vn/cong-nghe.rss","Công nghệ"},
            {"https://nld.com.vn/du-lich-xanh.rss","Du lịch xanh"}
    };
    String[][] vtcNewsUrls = {
            {"https://vtc.vn/rss/thoi-su.rss", "Thời sự"},
            {"https://vtc.vn/rss/the-gioi.rss", "Thế giới"},
            {"https://vtc.vn/rss/giao-duc.rss", "Giáo dục"},
            {"https://vtc.vn/rss/kinh-te.rss", "Kinh tế"},
            {"https://vtc.vn/rss/truyen-hinh.rss", "Truyền hình"},
            {"https://vtc.vn/rss/phap-luat.rss", "Pháp luật"},
            {"https://vtc.vn/rss/khoa-hoc-cong-nghe.rss", "Khoa học - Công nghệ"},
            {"https://vtc.vn/rss/suc-khoe.rss", "Sức khoẻ đời sống"},
            {"https://vtc.vn/rss/gioi-tre.rss", "Giới trẻ"},
            {"https://vtc.vn/rss/phong-su-kham-pha.rss", "Phóng sự - khám phá"},
            {"https://vtc.vn/rss/bat-dong-san.rss", "Địa ốc - bất động sản"},
            {"https://vtc.vn/rss/the-thao.rss", "Thể thao"}
    };
    String[][] vtvNewsUrls = {
            {"https://vtv.vn/trong-nuoc/chinh-tri.rss", "Chính trị Việt Nam"},
            {"https://vtv.vn/trong-nuoc/xa-hoi.rss", "Xã hội Việt Nam"},
            {"https://vtv.vn/trong-nuoc/phap-luat.rss", "Pháp luật Việt Nam"},
            {"https://vtv.vn/the-gioi/tin-tuc.rss", "Tin tức thế giới"},
            {"https://vtv.vn/kinh-te.rss", "Kinh tế"},
            {"https://vtv.vn/truyen-hinh.rss", "Truyền hình"},
            {"https://vtv.vn/van-hoa-giai-tri.rss", "Văn hoá giải trí"},
            {"https://vtv.vn/the-thao.rss", "Thể thao"},
            {"https://vtv.vn/giao-duc.rss", "Giáo dục"},
            {"https://vtv.vn/cong-nghe.rss", "Công nghệ"},
            {"https://vtv.vn/doi-song.rss", "Đời sống"},
            {"https://vtv.vn/du-bao-thoi-tiet.rss", "Dự báo thời tiết"},
    };

    String [][] myUrlCaptionMenu = thanhnienUrls;

    String[] myUrlCaption = new String[myUrlCaptionMenu.length];
    String[] myUrlAddress = new String[myUrlCaptionMenu.length];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        String newspaper = intent.getStringExtra("newspaper");
        updateUrl(newspaper);

        context = getApplicationContext();
        TextView txtMsg = (TextView) findViewById(R.id.txtMsg);
        txtMsg.setText(("Channels in " + newspaper).toUpperCase(Locale.ROOT));

        // user will tap on a ListView’s row to request category’s headlines
        myMainListView = (ListView) this.findViewById(R.id.myListView);
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

    private void updateUrl(String news) {
        switch (news) {
            case "Thanh Niên":
                myUrlCaptionMenu = thanhnienUrls;
                break;
            case "VnExpress":
                myUrlCaptionMenu = vnexpressUrls;
                break;
            case "Tuổi Trẻ":
                myUrlCaptionMenu = tuoitreUrls;
                break;
            case "VietNamNet":
                myUrlCaptionMenu = vietnamnetUrls;
                break;
            case "Người Lao Động":
                myUrlCaptionMenu = nguoiLaoDongUrls;
                break;
            case "VTC News":
                myUrlCaptionMenu = vtcNewsUrls;
                break;
            case "VTV News":
                myUrlCaptionMenu = vtvNewsUrls;
                break;
            default:
                break;
        }

        for (int i = 0; i < myUrlAddress.length; i++) {
            myUrlAddress[i] = myUrlCaptionMenu[i][0];
            myUrlCaption[i] = myUrlCaptionMenu[i][1];
        }
    }
}