package com.example.btl.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.btl.dataClass.QuanLyLop;
import com.example.btl.R;
import com.example.btl.dataClass.ThongKe;
import com.example.btl.adapter.ThongKeAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class ThongKeActivity extends AppCompatActivity {
    private ListView lstThongKeSinhVien;
    private ArrayList<ThongKe> lstThongKe;
    private ThongKeAdapter adapter;
    private DatabaseReference dataFireBase;
    private FirebaseUser user;
    private Spinner spLop,spHocKy;
    private ArrayAdapter<String> adapterHocKy;
    private ArrayAdapter<QuanLyLop> adapterLop;
    private ArrayList<String> dataListHocKy;
    private ArrayList<QuanLyLop> dataListLop;
    private String hocKy = null,maLop = null;
    private long soBuoi = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_ke);

        user = FirebaseAuth.getInstance().getCurrentUser();
        dataFireBase = FirebaseDatabase.getInstance().getReference();

        lstThongKeSinhVien = (ListView)findViewById(R.id.lstThongKeSinhVien);
        lstThongKe = new ArrayList<ThongKe>();
        adapter = new ThongKeAdapter(ThongKeActivity.this, lstThongKe);
        lstThongKeSinhVien.setAdapter(adapter);

        lstThongKeSinhVien.setEmptyView(findViewById(R.id.emptyList));

        spLop = (Spinner)findViewById(R.id.spLop);
        spHocKy = (Spinner)findViewById(R.id.spHocKy);

        dataListLop = new ArrayList<QuanLyLop>();
        dataListHocKy = new ArrayList<String>();

        adapterHocKy = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,dataListHocKy);
        adapterHocKy.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spHocKy.setAdapter(adapterHocKy);

        adapterLop = new ArrayAdapter<QuanLyLop>(this, android.R.layout.simple_spinner_item,dataListLop);
        adapterLop.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spLop.setAdapter(adapterLop);
        retriveDataHocKy();

        spHocKy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                hocKy = dataListHocKy.get(position);
                retriveDataClass(hocKy);
                retriveDataListView(hocKy,maLop);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spLop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                maLop = dataListLop.get(position).getMaLop();
                soBuoi = dataListLop.get(position).getSoBuoi();
                retriveDataListView(hocKy,maLop);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
       // Toast.makeText(this, "Data"+lstThongKe.get(0).getHoTen(), Toast.LENGTH_SHORT).show()

    }
    public void retriveDataListView(String hocKy, String maLop)
    {
        if(maLop != null) {
            dataFireBase.child("diemdanh").child(user.getUid()).child(hocKy).child(maLop).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    lstThongKe.clear();
                    Log.d("loioday1",dataSnapshot.getChildren().toString());
                    for (DataSnapshot unit : dataSnapshot.getChildren()) {
                        Log.d("loioday",unit.getValue().toString());
                        ThongKe ListThongKe = unit.getValue(ThongKe.class);
                        lstThongKe.add(ListThongKe);
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(ThongKeActivity.this, "Đã xảy ra lỗi vui lòng thử lại", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else
        {
            lstThongKeSinhVien.setEmptyView(findViewById(R.id.emptyList));
        }
    }
    public void gotoViewChart(View view)
    {
        Intent intent = new Intent(ThongKeActivity.this,ViewChartActivity.class);
        intent.putExtra("hocKy", hocKy);
        intent.putExtra("maLop", maLop);
        intent.putExtra("soBuoi",soBuoi+"");
        startActivity(intent);
    }
    public void retriveDataHocKy(){
        dataFireBase.child("HocKy").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot item : dataSnapshot.getChildren()) {
                    dataListHocKy.add(item.getValue().toString());
                }
                adapterHocKy.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void retriveDataClass(String hocKy){
        dataFireBase.child("Lop").child(user.getUid()).child(hocKy).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataListLop.clear();
                for(DataSnapshot item:dataSnapshot.getChildren()) {
                    QuanLyLop qll = item.getValue(QuanLyLop.class);
                    dataListLop.add(qll);
                }
                adapterLop.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
