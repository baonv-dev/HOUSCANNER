package com.example.btl.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.btl.dataClass.QuanLySinhVienLop;
import com.example.btl.R;
import com.example.btl.adapter.QuanLySinhVienLopAdapter;
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

public class QuanLySinhVienLopActivity extends AppCompatActivity {
    private Button showEditSv,cancelEditSv;
    private EditText txtEditTenSv,txtEditLopSv,txtEditMaSv,txtEditNgaySinhSv;
    private ArrayList<QuanLySinhVienLop> lstSinhVien;
    private String maLop, hocKy;
    private DatabaseReference dataLop;
    private FirebaseUser user;
    private QuanLySinhVienLopAdapter adapter;
    private SwipeMenuListView listViewSinhVien;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_sinh_vien_lop);
        listViewSinhVien = (SwipeMenuListView) findViewById(R.id.danhsachsinhvien);

        lstSinhVien = new ArrayList<QuanLySinhVienLop>();
        adapter = new QuanLySinhVienLopAdapter(QuanLySinhVienLopActivity.this, lstSinhVien);
        listViewSinhVien.setAdapter(adapter);

        listViewSinhVien.setEmptyView(findViewById(R.id.emptyList));

        Intent intent = getIntent();
        maLop = intent.getStringExtra("maLop");
        hocKy = intent.getStringExtra("hocKy");

        user = FirebaseAuth.getInstance().getCurrentUser();
        dataLop = FirebaseDatabase.getInstance().getReference();

        Toast.makeText(this, maLop, Toast.LENGTH_SHORT).show();

        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));
                // set item width
                openItem.setWidth(170);

                // set item title
                openItem.setTitle("Sửa");
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(170);
                // set a icon
                deleteItem.setIcon(R.drawable.icon_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

        listViewSinhVien.setMenuCreator(creator);
        listViewSinhVien.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        // AFTER CLICK EDIT IN ROW STUDENT
                        showDialogEdit(lstSinhVien.get(position));
                        Toast.makeText(QuanLySinhVienLopActivity.this, "Sửa", Toast.LENGTH_SHORT).show();
                        break;

                    case 1:
                        // AFTER CLICK DELETE IN ROW STUDENT
                        deleteStudent(lstSinhVien.get(position));
                        Toast.makeText(QuanLySinhVienLopActivity.this, "Xóa", Toast.LENGTH_SHORT).show();
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });

        dataLop.child("diemdanh").child(user.getUid()).child(hocKy).child(maLop).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lstSinhVien.clear();
                for (DataSnapshot unit : dataSnapshot.getChildren()){
                    QuanLySinhVienLop QLSV = unit.getValue(QuanLySinhVienLop.class);
                    lstSinhVien.add(QLSV);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(QuanLySinhVienLopActivity.this, "Đã xảy ra lỗi vui lòng thử lại", Toast.LENGTH_SHORT).show();
            }
        });
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.add_Student);
        floatingActionButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v)
            {
                final AlertDialog.Builder alert = new AlertDialog.Builder(QuanLySinhVienLopActivity.this);
                View mViewQuanLySinhVien = (View)getLayoutInflater().inflate(R.layout.dialog_edit_sinhvien,null);
                final EditText txtMaSv = (EditText)mViewQuanLySinhVien.findViewById(R.id.txtMaSinhVien);
                final EditText txtTenSv = (EditText)mViewQuanLySinhVien.findViewById(R.id.txtTenSinhVien);
                final EditText txtLop = (EditText)mViewQuanLySinhVien.findViewById(R.id.txtLopSinhVien);
                final EditText txtNgaySinh = (EditText)mViewQuanLySinhVien.findViewById(R.id.txtNgaySinh);
                final TextView txtTieuDe = (TextView)mViewQuanLySinhVien.findViewById(R.id.txtTieuDeDialogSinhVien);
                Button btnAdd = (Button)mViewQuanLySinhVien.findViewById(R.id.btnEditSv);
                btnAdd.setText("Thêm");
                txtTieuDe.setText("Thêm sinh viên");
                Button btnCancel = (Button)mViewQuanLySinhVien.findViewById(R.id.btnCancelSv);
                alert.setView(mViewQuanLySinhVien);
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
                        QuanLySinhVienLop qlsv = new QuanLySinhVienLop(txtMaSv.getText().toString(),txtTenSv.getText().toString(),txtLop.getText().toString(),txtNgaySinh.getText().toString());
                        dataLop.child("diemdanh").child(user.getUid()).child(hocKy).child(maLop).child(txtMaSv.getText().toString()).setValue(qlsv);
                        lstSinhVien.add(qlsv);
                        adapter.notifyDataSetChanged();

                        dataLop.child("diemdanh").child(user.getUid()).child(hocKy).child(maLop).addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                Toast.makeText(QuanLySinhVienLopActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                            }

                            @Override
                            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                            }

                            @Override
                            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(QuanLySinhVienLopActivity.this, "Thêm thất bại", Toast.LENGTH_SHORT).show();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home: onBackPressed(); return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void deleteStudent(final QuanLySinhVienLop lstSv)
    {
        dataLop.child("diemdanh").child(user.getUid()).child(hocKy).child(maLop).child(lstSv.getMaSV()).removeValue();
        lstSinhVien.remove(lstSv);
        adapter.notifyDataSetChanged();
        dataLop.child("diemdanh").child(user.getUid()).child(hocKy).child(maLop).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Toast.makeText(QuanLySinhVienLopActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(QuanLySinhVienLopActivity.this, "Xóa thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showDialogEdit(final QuanLySinhVienLop lstSv)
    {
        final AlertDialog.Builder alert = new AlertDialog.Builder(QuanLySinhVienLopActivity.this);
        View mViewEditSinhVien = (View)getLayoutInflater().inflate(R.layout.dialog_edit_sinhvien,null);
        alert.setView(mViewEditSinhVien);
        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);

        // GET VIEWS
        showEditSv = (Button)mViewEditSinhVien.findViewById(R.id.btnEditSv);
        cancelEditSv = (Button)mViewEditSinhVien.findViewById(R.id.btnCancelSv);
        txtEditTenSv = (EditText) mViewEditSinhVien.findViewById(R.id.txtTenSinhVien);
        txtEditMaSv = (EditText) mViewEditSinhVien.findViewById(R.id.txtMaSinhVien);
        txtEditNgaySinhSv = (EditText) mViewEditSinhVien.findViewById(R.id.txtNgaySinh);
        txtEditLopSv = (EditText) mViewEditSinhVien.findViewById(R.id.txtLopSinhVien);

        final TextView txtTieuDe = (TextView)mViewEditSinhVien.findViewById(R.id.txtTieuDeDialogSinhVien);
        Button btnAdd = (Button)mViewEditSinhVien.findViewById(R.id.btnEditSv);
        btnAdd.setText("Sửa");
        txtTieuDe.setText("Sửa thông tin sinh viên");

        //SET TEXT TO EDIT TEXT
        txtEditTenSv.setText(lstSv.getHoTen());
        txtEditLopSv.setText(lstSv.getLop());
        txtEditNgaySinhSv.setText(lstSv.getNgaySinh());
        txtEditMaSv.setText(lstSv.getMaSV());


        cancelEditSv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        // BUTTON EDIT CLICK -  ADD DATA TO FIREBASE
        showEditSv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // remove data in firebase
                dataLop.child("diemdanh").child(user.getUid()).child(hocKy).child(maLop).child(lstSv.getMaSV()).removeValue();
                lstSinhVien.remove(lstSv);
                // add data to firebase
                QuanLySinhVienLop qlsv = new QuanLySinhVienLop(txtEditMaSv.getText().toString(),txtEditTenSv.getText().toString(),txtEditLopSv.getText().toString(),txtEditNgaySinhSv.getText().toString());
                dataLop.child("diemdanh").child(user.getUid()).child(hocKy).child(maLop).child(txtEditMaSv.getText().toString()).setValue(qlsv);
                lstSinhVien.add(qlsv);
                dataLop.child("diemdanh").child(user.getUid()).child(hocKy).child(maLop).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Toast.makeText(QuanLySinhVienLopActivity.this, "Sửa thành công", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(QuanLySinhVienLopActivity.this, "Có lỗi xảy ra vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                    }
                });

                adapter.notifyDataSetChanged();
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }
}
