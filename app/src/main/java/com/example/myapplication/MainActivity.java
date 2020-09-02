package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyLineChartView viewById = findViewById(R.id.line);
        ArrayList<PointBean> points = new ArrayList<>();
        points.add(new PointBean(10, 4));
        points.add(new PointBean(15, 6));
        points.add(new PointBean(20, 10));
        points.add(new PointBean(25, 14));
        points.add(new PointBean(30, 18));
        points.add(new PointBean(35, 20));
        points.add(new PointBean(40, 24));
        viewById.setData(points);
    }
}