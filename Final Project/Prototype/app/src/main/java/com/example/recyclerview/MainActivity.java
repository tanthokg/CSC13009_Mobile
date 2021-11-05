package com.example.recyclerview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView locationRecView = findViewById(R.id.locationRecView);
        ArrayList<Location> locations = new ArrayList<>();
        locations.add(new Location("Arizona", "USA", 3339, "https://media.gody.vn//images/hinh-tong-hop/top-dao-yen-binh-nhat-chau-a/5-2019/63688326-20190504034614-hinh-tong-hop-top-dao-yen-binh-nhat-chau-a.jpg"));
        locations.add(new Location("Beijing", "China", 9341, "https://dmed-healthcare.com/wp-content/uploads/2018/03/Beijing-1490x745.jpg"));
        locations.add(new Location("Berlin", "German", 6571, "https://vcdn1-dulich.vnecdn.net/2018/12/06/berlin-1544056144.jpg?w=1200&h=0&q=100&dpr=1&fit=crop&s=Agkd7CjvHYJ_woNBfQGKFA"));
        locations.add(new Location("Bangkok", "Thailand", 11909, "https://www.thedrinksbusiness.com/content/uploads/2017/09/Bangkok.jpg"));
        locations.add(new Location("Ho Chi Minh", "Vietnam", 7785, "https://vnn-imgs-f.vgcloud.vn/2020/06/30/17/tp-hcm-nhin-tu-tren-cao-1.jpg"));
        locations.add(new Location("Hong Kong", "China", 5675, "https://vietyouth.vn/wp-content/uploads/2019/03/hong-kong-ve-dem.png"));
        locations.add(new Location("London", "England", 7782, "https://menback.com/wp-content/uploads/2021/06/dong-ho-bigben-london.jpg"));
        locations.add(new Location("Manila", "Philippines", 8901, "https://dulichvietnam.com.vn/du-lich-philippines/wp-content/uploads/2020/08/kinh-nghiem-du-lich-manila-anh-dai-dien-1.jpg"));
        locations.add(new Location("New York", "USA", 18772, "https://immica.org/wp-content/uploads/2017/11/bieu-tuong-noi-tieng-khong-chi-cua-new-york-ma-toan-nuoc-my.jpg"));
        locations.add(new Location("Phu Quoc Island", "Vietnam", 12311, "https://baoquocte.vn/stores/news_dataimages/nguyennga/022021/08/10/phu-quoc.jpg?rt=20210208105346"));
        locations.add(new Location("Tokyo", "Japan", 15652, "https://360nhatban.net/wp/wp-content/uploads/2017/02/tokyo1.jpg"));
        locations.add(new Location("Washington", "USA", 9018, "https://dulichvietnam.com.vn/vnt_upload/news/10_2019/khamphawashington1abia.jpg"));


        LocationViewAdapter locationViewAdapter = new LocationViewAdapter(this);
        locationViewAdapter.setLocations(locations);
        locationRecView.setAdapter(locationViewAdapter);
        locationRecView.setLayoutManager(new GridLayoutManager(this, 2));
    }
}