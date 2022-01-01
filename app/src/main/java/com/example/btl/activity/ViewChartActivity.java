package com.example.btl.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.btl.R;
import com.example.btl.dataClass.ThongKe;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewChartActivity extends AppCompatActivity {
    private int[] yData = {};
    private BarChart barChart;
    private int soBuoi = 0;
    private String hocKy = null,maLop = null;
    private DatabaseReference dataFireBase;
    private FirebaseUser user;
    private BarChart chart ;
    private ArrayList<BarEntry> BARENTRY ;
    private ArrayList<String> BarEntryLabels ;
    private BarDataSet Bardataset ;
    private BarData BARDATA ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_chart);

        user = FirebaseAuth.getInstance().getCurrentUser();
        dataFireBase = FirebaseDatabase.getInstance().getReference();

        // get data Intent
        Intent intent = getIntent();
        hocKy = intent.getStringExtra("hocKy");
        maLop = intent.getStringExtra("maLop");
        soBuoi = Integer.parseInt(intent.getStringExtra("soBuoi"));
        yData = new int[soBuoi];
        getData();
        createChart();
    }
    public void getData() {
        if (maLop != null) {
            dataFireBase.child("diemdanh").child(user.getUid()).child(hocKy).child(maLop).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot unit : dataSnapshot.getChildren()) {
                        for(int i = 0 ; i < soBuoi;i++) {
                            if (unit.child("diemDanh").child(String.valueOf((i+1))).exists()) {
                                yData[i] = yData[i]+1;
                            }
                        }
                    }
                    AddValuesToBARENTRY();
                    AddValuesToBarEntryLabels();
                    Bardataset = new BarDataSet(BARENTRY, "Buổi đi học");
                    BARDATA = new BarData(BarEntryLabels, Bardataset);
                    Bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
                    chart.setData(BARDATA);
                    chart.animateY(3000);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(ViewChartActivity.this, "Đã xảy ra lỗi vui lòng thử lại", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void createChart() {
        chart = (BarChart) findViewById(R.id.barChart);
        BARENTRY = new ArrayList<>();
        BarEntryLabels = new ArrayList<String>();

    }

    private void AddValuesToBARENTRY(){
        for(int i = 0;i<soBuoi;i++) {
            BARENTRY.add(new BarEntry(yData[i], i));
        }
    }

    private void AddValuesToBarEntryLabels(){
        for(int i = 0;i<soBuoi;i++)
        {
            BarEntryLabels.add("Buổi "+(i+1));
        }
    }
}
