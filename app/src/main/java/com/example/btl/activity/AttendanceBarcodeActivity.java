package com.example.btl.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.btl.R;
import com.example.btl.activity.AttendanceManuallyActivity;
import com.example.btl.dataClass.QuanLyLop;
import com.example.btl.dataClass.QuanLySinhVienLop;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class AttendanceBarcodeActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{
    private DatabaseReference mData;
    private TextView soluongdiemdanh, sinhvienvuadiemdanh, ngaydiemdanh;
    private Spinner spLop,spHocKy,spSoBuoi;
    private int tongSinhVienLop;
    private DateFormat df = new SimpleDateFormat("dd-MM-YYYY");
    private String date = df.format(Calendar.getInstance().getTime());
    private String hocKy = null,maLop = null;
    private long soBuoi = 0, soSinhVienDaDiemDanh = 0;
    private ArrayList<QuanLySinhVienLop> lstSinhVien;
    private ArrayAdapter<String> adapterHocKy;
    private ArrayAdapter<Long> adapterSoBuoi;
    private ArrayAdapter<QuanLyLop> adapterLop;
    private ArrayList<String> dataListHocKy;
    private ArrayList<QuanLyLop> dataListLop;
    private ArrayList<Long> dataSoBuoi;
    private FirebaseUser user;
    private static final int REQUEST_CAMERA = 1;
    private ZXingScannerView mScannerView;
    private QuanLyLop qll;
    private QuanLySinhVienLop qlsvlop;
    private int checkLop = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_barcode);

        lstSinhVien = new ArrayList<QuanLySinhVienLop>();
        // FIREBASE
        mData = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        sinhvienvuadiemdanh = (TextView)findViewById(R.id.vuadiemdanh);
        soluongdiemdanh = (TextView)findViewById(R.id.dadiemdanh);
        ngaydiemdanh = (TextView)findViewById(R.id.ngaydiemdanh);

        ngaydiemdanh.setText("Ngày điểm danh: "+date);
        sinhvienvuadiemdanh.setText("Sinh viên vừa điểm danh: ");
        soluongdiemdanh.setText("Số lượng đã điểm danh: ");
        // FindViews
        mScannerView = findViewById(R.id.scannerView);
        spLop = (Spinner)findViewById(R.id.listclass);
        spHocKy = (Spinner)findViewById(R.id.listHocKy);
        spSoBuoi = (Spinner)findViewById(R.id.selectBuoi);

        dataListLop = new ArrayList<QuanLyLop>();
        dataListHocKy = new ArrayList<String>();
        dataSoBuoi = new ArrayList<Long>();
        // Config Spinner Hoc Ky
        adapterHocKy = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,dataListHocKy);
        adapterHocKy.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spHocKy.setAdapter(adapterHocKy);
        retriveDataHocKy();
        // Config Spinner Class
        adapterLop = new ArrayAdapter<QuanLyLop>(this, android.R.layout.simple_spinner_item,dataListLop);
        adapterLop.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spLop.setAdapter(adapterLop);
        // Config Spinner So Buoi
        adapterSoBuoi = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,dataSoBuoi);
        adapterSoBuoi.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spSoBuoi.setAdapter(adapterSoBuoi);
        // Event Spinner HocKy Click
        spHocKy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                hocKy = dataListHocKy.get(position);
                retriveDataClass(hocKy);

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        // Event Spinner Lop Click
        spLop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                maLop = dataListLop.get(position).getMaLop();
                retriveSoBuoi(dataListLop.get(position).getSoBuoi());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Event Spinner So Buoi Click
        spSoBuoi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                soBuoi = dataSoBuoi.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    // CAMERA SCAN BARCODE
    @Override
    public void onResume() {
        super.onResume();
        Toast.makeText(this, "Đang quét  ", Toast.LENGTH_SHORT).show();// Start camera on resume
        // GRANT PERMISSION
        if (!checkPermission()) {
            requestPermission();
        }
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();
        mScannerView.setAutoFocus(true);
    }
    private boolean checkPermission() {
        return (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED);
    }
    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
    }
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA:
                if (grantResults.length > 0) {

                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted){
                        Toast.makeText(getApplicationContext(), "Permission Granted, Now you can access camera", Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(getApplicationContext(), "Permission Denied, You cannot access and camera", Toast.LENGTH_LONG).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                                showMessage("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{Manifest.permission.CAMERA},
                                                            REQUEST_CAMERA);
                                                }
                                            }
                                        });
                                return;
                            }
                        }
                    }
                }
                break;
        }
    }
    private void showMessage(String s, DialogInterface.OnClickListener onClickListener) {
        new AlertDialog.Builder(this)
                .setMessage(s)
                .setPositiveButton("OK", onClickListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
    public void retriveDataHocKy(){
        mData.child("HocKy").child(user.getUid()).addValueEventListener(new ValueEventListener() {
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
        mData.child("Lop").child(user.getUid()).child(hocKy).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataListLop.clear();
                dataSoBuoi.clear();
                for(DataSnapshot item:dataSnapshot.getChildren()) {
                    qll = item.getValue(QuanLyLop.class);
                    dataListLop.add(qll);
                }
                adapterLop.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void retriveSoBuoi(long soBuoi) {
        dataSoBuoi.clear();
        for(long i = 1;i <= soBuoi;i++) {
            dataSoBuoi.add(i);
        }
        adapterSoBuoi.notifyDataSetChanged();

        mData.child("diemdanh").child(user.getUid()).child(hocKy).child(maLop).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lstSinhVien.clear();
                for (DataSnapshot unit : dataSnapshot.getChildren()){
                    QuanLySinhVienLop QLSV = unit.getValue(QuanLySinhVienLop.class);
                    lstSinhVien.add(QLSV);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AttendanceBarcodeActivity.this, "Đã xảy ra lỗi vui lòng thử lại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onPause() {
        Toast.makeText(this, "Tạm dừng ", Toast.LENGTH_SHORT).show();
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause

    }

    @Override
    public void handleResult(final Result stringMaSvBarCode) {
        final String masvScan =  stringMaSvBarCode.getText();
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
        r.play();
        Vibrator v = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(400);
        checkLop = 0;
        showInfoSv(masvScan);
        mData.child("diemdanh").child(user.getUid()).child(hocKy).child(maLop).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tongSinhVienLop = (int)dataSnapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        if(checkLop == 1) {
            mData.child("diemdanh").child(user.getUid()).child(hocKy).child(maLop).child(masvScan).child("diemDanh").child(String.valueOf(soBuoi)).setValue(date);
        }
        if(checkLop == 0)
        {
            Toast.makeText(this, "Sinh viên không thuộc lớp này", Toast.LENGTH_SHORT).show();
        }
        mData.child("diemdanh").child(user.getUid()).child(hocKy).child(maLop).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                soSinhVienDaDiemDanh = 0;
                for(DataSnapshot unit : dataSnapshot.getChildren())
                {
                    if(unit.child("diemDanh").child(String.valueOf(soBuoi)).exists())
                    {
                        soSinhVienDaDiemDanh ++;
                    }
                }
                soluongdiemdanh.setText("Số sinh viên đã điểm danh:"+soSinhVienDaDiemDanh+"/"+tongSinhVienLop);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AttendanceBarcodeActivity.this, "Có lỗi xảy ra vui lòng thử lại!", Toast.LENGTH_SHORT).show();
            }
        });
        // If you would like to resume scanning, call this method below:
        mScannerView.resumeCameraPreview(this);
    }
    public void showInfoSv(String masv) {
        //int res = Collections.binarySearch(lstSinhVien,masv);
        //lstSinhVien.contains(masv);
        for(int i=0;i<lstSinhVien.size();i++)
        {
            if(lstSinhVien.get(i).getMaSV().equals(masv))
            {
                Toast.makeText(this, "Đã điểm danh sinh viên "+lstSinhVien.get(i).getHoTen(), Toast.LENGTH_SHORT).show();
                sinhvienvuadiemdanh.setText("Sinh viên vừa điểm danh: " + lstSinhVien.get(i).getHoTen());
                checkLop = 1;
                return;
            }
        }
        if(checkLop == 0)
        {
            sinhvienvuadiemdanh.setText("Sinh viên không thuộc lớp này");
        }
    }
    public void gotoAttendanceManual(View view) {
        Intent openAttendanceManual = new Intent(AttendanceBarcodeActivity.this, AttendanceManuallyActivity.class);
        openAttendanceManual.putExtra("hocKy",hocKy);
        openAttendanceManual.putExtra("maLop", maLop);
        openAttendanceManual.putExtra("soBuoi", String.valueOf(soBuoi));
        openAttendanceManual.putExtra("ngayDiemDanh", date);
        startActivity(openAttendanceManual);
    }
    public void turnOnFlash(View v) {
        mScannerView.setFlash(true);
    }
    public void turnOffFlash(View v){
        mScannerView.setFlash(false);
    }
}

