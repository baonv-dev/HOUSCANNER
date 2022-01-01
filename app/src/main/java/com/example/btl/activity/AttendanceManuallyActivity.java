package com.example.btl.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.example.btl.dataClass.AttendanceManually;
import com.example.btl.adapter.AttendanceManuallyAdapter;
import com.example.btl.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AttendanceManuallyActivity extends AppCompatActivity {
    private ArrayList<AttendanceManually> arrAttMan;
    private AttendanceManuallyAdapter adapter;
    private ListView lvAttMan;
    private FirebaseUser user;
    private DatabaseReference mData;
    private String hocKy,maLop,soBuoi,ngayDiemDanh;
    private CheckBox cbDiemDanh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_manually);
        init();
        getDataIntent();

        mData = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();

        adapter= new AttendanceManuallyAdapter(arrAttMan,AttendanceManuallyActivity.this);
        addDiemDanh();
        lvAttMan.setAdapter(adapter);
    }
    private void getDataIntent() {
        Intent intent = getIntent();
        maLop = intent.getStringExtra("maLop");
        hocKy = intent.getStringExtra("hocKy");
        soBuoi = intent.getStringExtra("soBuoi");
        ngayDiemDanh = intent.getStringExtra("ngayDiemDanh");
    }
    private void addDiemDanh() {
        mData.child("diemdanh").child(user.getUid()).child(hocKy).child(maLop).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrAttMan.clear();
                for(DataSnapshot unit : dataSnapshot.getChildren())
                {
                    AttendanceManually student = unit.getValue(AttendanceManually.class);
                    if(unit.child("diemDanh").child(soBuoi).exists())
                    {
                        student.setDadiemdanh(true);
                    }
                    arrAttMan.add(student);
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });

    }
    public void diemDanh(View v) {
        View parentRow = (View) v.getParent();
        final int position = lvAttMan.getPositionForView(parentRow);
        if (((CheckBox) v).isChecked()) {
            mData.child("diemdanh").child(user.getUid()).child(hocKy).child(maLop).child(arrAttMan.get(position).getMaSV()).child("diemDanh").child(String.valueOf(soBuoi)).setValue(ngayDiemDanh);
            mData.child("diemdanh").child(user.getUid()).child(hocKy).child(maLop).child(arrAttMan.get(position).getMaSV()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Toast.makeText(AttendanceManuallyActivity.this, "Đã điểm danh!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(AttendanceManuallyActivity.this, "Có lỗi xảy ra vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else
        {
            mData.child("diemdanh").child(user.getUid()).child(hocKy).child(maLop).child(arrAttMan.get(position).getMaSV()).child("diemDanh").child(String.valueOf(soBuoi)).removeValue();
            mData.child("diemdanh").child(user.getUid()).child(hocKy).child(maLop).child(arrAttMan.get(position).getMaSV()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Toast.makeText(AttendanceManuallyActivity.this, "Sinh viên này cúp học", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(AttendanceManuallyActivity.this, "Có lỗi xảy ra vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                }
            });

        }

    }
    private void init(){
        lvAttMan = findViewById(R.id.lvSV);
        arrAttMan = new ArrayList<AttendanceManually>();
    }
}
