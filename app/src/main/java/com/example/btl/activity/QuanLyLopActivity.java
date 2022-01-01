package com.example.btl.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.btl.dataClass.QuanLyLop;
import com.example.btl.R;
import com.example.btl.adapter.QuanLyLopAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class QuanLyLopActivity extends AppCompatActivity {
    private ListView lstClassRoom;
    private List<QuanLyLop> lstLop;
    private DatabaseReference dataLop;
    private FirebaseUser user;
    private QuanLyLopAdapter adapter;
    private String hocKy;
    private QuanLyLop QLL;
    private String KEYHOCKY = "HOCKY";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_lop);

        lstLop = new ArrayList<QuanLyLop>();
        setAdapterListView();
        lstClassRoom.setEmptyView(findViewById(R.id.emptyList));

        Intent intent = getIntent();
        hocKy = intent.getStringExtra("HocKy");

        if(savedInstanceState != null){
            hocKy = savedInstanceState.getString(KEYHOCKY);
        }


        user = FirebaseAuth.getInstance().getCurrentUser();
        dataLop = FirebaseDatabase.getInstance().getReference();

        dataLop.child("Lop").child(user.getUid()).child(hocKy).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lstLop.clear();
                for (DataSnapshot unit : dataSnapshot.getChildren()){
                    QuanLyLop QLL = unit.getValue(QuanLyLop.class);
                    lstLop.add(QLL);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.add_Lop);
        floatingActionButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v)
            {
                final AlertDialog.Builder alert = new AlertDialog.Builder(QuanLyLopActivity.this);
                View mViewQuanLyLop = (View)getLayoutInflater().inflate(R.layout.dialog_add_class,null);
                final TextView txtMaLopAdd = (TextView)mViewQuanLyLop.findViewById(R.id.txtAddMaLop);
                final TextView txtTenLopAdd = (TextView)mViewQuanLyLop.findViewById(R.id.txtAddTenLop);
                final TextView txtSoTinChi = (TextView)mViewQuanLyLop.findViewById(R.id.txtAddSoTinChi);

                Button btnAdd = (Button)mViewQuanLyLop.findViewById(R.id.btnAddClass);
                btnAdd.setText("Thêm");
                Button btnCancel = (Button)mViewQuanLyLop.findViewById(R.id.btnCancel);
                alert.setView(mViewQuanLyLop);

                final AlertDialog alertDialog = alert.create();
                alertDialog.setCanceledOnTouchOutside(false);
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // add data to firebase
                        QuanLyLop qllop = new QuanLyLop(Long.parseLong(txtSoTinChi.getText().toString()),txtTenLopAdd.getText().toString(),txtMaLopAdd.getText().toString());
                        dataLop.child("Lop").child(user.getUid()).child(hocKy).child(txtMaLopAdd.getText().toString()).setValue(qllop);
                        lstLop.add(qllop);
                        adapter.notifyDataSetChanged();
                        dataLop.child("Lop").child(user.getUid()).child(hocKy).child(txtMaLopAdd.getText().toString()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Toast.makeText(QuanLyLopActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(QuanLyLopActivity.this, "Có lỗi xảy ra vui lòng thử lại", Toast.LENGTH_SHORT).show();
                            }
                        });
                        //dataHocKy.child(user.getUid()).child(txtNamHoc.getText().toString()+"-"+txtHocKy.getText().toString()).setValue(txtNamHoc.getText().toString()+"-"+txtHocKy.getText().toString());
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
            }
        });
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        hocKy = savedInstanceState.getString(KEYHOCKY);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(KEYHOCKY, hocKy);
        super.onSaveInstanceState(outState);
    }

    private void setAdapterListView() {
        lstClassRoom = (ListView) findViewById(R.id.listClassRoom);
        adapter = new QuanLyLopAdapter(QuanLyLopActivity.this,lstLop);
        lstClassRoom.setAdapter(adapter);
    }


    public void xemChiTiet(View v)
    {
        View parentRow = (View) v.getParent();
        final int position = lstClassRoom.getPositionForView(parentRow);
        Toast.makeText(this, "Xem chi tiết"+ lstLop.get(position).getTenLop(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(QuanLyLopActivity.this, QuanLySinhVienLopActivity.class);
        intent.putExtra("hocKy",hocKy);
        intent.putExtra("maLop", lstLop.get(position).getMaLop());
        startActivity(intent);
    }
    public void xemThongKe(View v)
    {
        Toast.makeText(this, "Thống kê", Toast.LENGTH_SHORT).show();
    }

    public void chinhSuaLop(View v)
    {
        final AlertDialog.Builder alert = new AlertDialog.Builder(QuanLyLopActivity.this);
        View mViewQuanLyLop = (View)getLayoutInflater().inflate(R.layout.dialog_add_class,null);
        View view = (View)v.getParent();
        final int position = lstClassRoom.getPositionForView(view);
        Toast.makeText(this, lstLop.get(position).getTenLop().toString(), Toast.LENGTH_SHORT).show();

        //Toast.makeText(this, txtCurrentMaLop.getText().toString(), Toast.LENGTH_SHORT).show();
        final EditText txtEditMaLop = (EditText)mViewQuanLyLop.findViewById(R.id.txtAddMaLop);
        final EditText txtEditTenLop = (EditText)mViewQuanLyLop.findViewById(R.id.txtAddTenLop);
        final EditText txtEditSoTinChi = (EditText)mViewQuanLyLop.findViewById(R.id.txtAddSoTinChi);

        // SET DATA EDIT TO EDIT_TEXT
       txtEditMaLop.setText(lstLop.get(position).getMaLop());
       txtEditTenLop.setText(lstLop.get(position).getTenLop());
       txtEditSoTinChi.setText(lstLop.get(position).getSoTinChi().toString());

        // GET BUTTON IN DIALOG
        Button btnAdd = (Button)mViewQuanLyLop.findViewById(R.id.btnAddClass);
        btnAdd.setText("Sửa");
        Button btnCancel = (Button)mViewQuanLyLop.findViewById(R.id.btnCancel);

        // SET VIEW TO DIALO18008579
        alert.setView(mViewQuanLyLop);
        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);

        // CATCH EVENT CLICK BUTTON CANCEL AND EDIT
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // REMOVE DATA CURRENT IN FIREBASE
                dataLop.child("Lop").child(user.getUid()).child(hocKy).child(lstLop.get(position).getMaLop()).removeValue();
                lstLop.remove(lstLop.get(position));
                // ADD NEW DATA TO FIREBASE

                QuanLyLop qllop = new QuanLyLop(Long.parseLong(txtEditSoTinChi.getText().toString()),txtEditTenLop.getText().toString(),txtEditMaLop.getText().toString());
                lstLop.add(qllop);

                dataLop.child("Lop").child(user.getUid()).child(hocKy).child(txtEditMaLop.getText().toString()).setValue(qllop);
                adapter.notifyDataSetChanged();

                dataLop.child("Lop").child(user.getUid()).child(hocKy).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //listHocKy.add(txtNamHoc.getText().toString()+txtHocKy.getText().toString());
                        Toast.makeText(QuanLyLopActivity.this, "Sửa học kỳ thành công", Toast.LENGTH_SHORT).show();

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(QuanLyLopActivity.this, "Sửa học kỳ thất bại", Toast.LENGTH_SHORT).show();
                    }
                });

                alertDialog.dismiss();
            }
        });
        alertDialog.show();

    }
    public void xoaLop(View v)
    {
        View view = (View)v.getParent();
        final int position = lstClassRoom.getPositionForView(view);
        dataLop.child("Lop").child(user.getUid()).child(hocKy).child(lstLop.get(position).getMaLop()).removeValue();
        lstLop.remove(lstLop.get(position));
        adapter.notifyDataSetChanged();
        Toast.makeText(this, "Đã xóa lớp", Toast.LENGTH_SHORT).show();
    }

}
