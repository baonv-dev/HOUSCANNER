package com.example.btl.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.btl.adapter.QuanLyHocKyAdapter;
import com.example.btl.R;
import com.example.btl.adapter.QuanLyHocKyAdapter;
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

public class QuanLyHocKyActivity extends AppCompatActivity {
    private GridView gridView;
    private DatabaseReference dataHocKy;
    private FirebaseUser user;
    private ArrayList<String> listHocKy;
    private QuanLyHocKyAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_hoc_ky);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listHocKy = new ArrayList<String>();
        gridView = (GridView) findViewById(R.id.danhsachhocky);

        adapter = new QuanLyHocKyAdapter(QuanLyHocKyActivity.this,listHocKy);
        gridView.setAdapter(adapter);

        gridView.setEmptyView(findViewById(R.id.emptyList));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView < ? > adapter, View view,int position, long arg){
                Toast.makeText(getApplicationContext(), "You Click"+listHocKy.get(position), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(QuanLyHocKyActivity.this, QuanLyLopActivity.class);
                intent.putExtra("HocKy", listHocKy.get(position));  // Truyền một String
                startActivity(intent);
            }
        });

        registerForContextMenu(gridView);
        user = FirebaseAuth.getInstance().getCurrentUser();
        dataHocKy = FirebaseDatabase.getInstance().getReference("HocKy");
        //  get data retrive and show in List View
        fillAdapterToListView();

        // Dialog add Học Ky
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.add_Class);
        floatingActionButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v)
            {
                final AlertDialog.Builder alert = new AlertDialog.Builder(QuanLyHocKyActivity.this);
                View mViewQuanLyHocKy = (View)getLayoutInflater().inflate(R.layout.dialog_add_hoc_ky,null);
                final TextView txtTieuDe = (TextView)mViewQuanLyHocKy.findViewById(R.id.txtTieuDeDialogHocKy);
                final EditText txtNamHoc = (EditText)mViewQuanLyHocKy.findViewById(R.id.txtNamHoc);
                final EditText txtHocKy = (EditText)mViewQuanLyHocKy.findViewById(R.id.txtHocKy);
                Button btnAdd = (Button)mViewQuanLyHocKy.findViewById(R.id.btnAdd);
                txtTieuDe.setText("Thêm học kỳ");
                btnAdd.setText("Thêm");
                Button btnCancel = (Button)mViewQuanLyHocKy.findViewById(R.id.btnCancel);
                alert.setView(mViewQuanLyHocKy);
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
                        dataHocKy.child(user.getUid()).child(txtNamHoc.getText().toString()+"-"+txtHocKy.getText().toString()).setValue(txtNamHoc.getText().toString()+"-"+txtHocKy.getText().toString());
                        listHocKy.add(txtNamHoc.getText().toString()+"-"+txtHocKy.getText().toString());
                        adapter.notifyDataSetChanged();
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
            }
        });
    }

    // Create Menu While Long Press
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Chọn hành động");
        AdapterView.AdapterContextMenuInfo cmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.add(1, cmi.position, 0, "Chỉnh Sửa");
        menu.add(2, cmi.position, 0, "Xóa");
        //getMenuInflater().inflate(R.menu.menu_hoc_ky,menu);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        GridView g = (GridView) findViewById(R.id.danhsachhocky);
        String s = (String) g.getItemAtPosition(item.getItemId());
        switch (item.getGroupId())
        {
            case 1:
                ChinhSua(s);
                Toast.makeText(this, "Chỉnh sửa" , Toast.LENGTH_SHORT).show();
                return true;
            case 2:
                //delete data
                dataHocKy.child(user.getUid()).child(s).removeValue();
                listHocKy.remove(s);
                adapter.notifyDataSetChanged();
                dataHocKy.child(user.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Toast.makeText(QuanLyHocKyActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(QuanLyHocKyActivity.this, "Có lỗi xảy ra vui lòng thử lại", Toast.LENGTH_SHORT).show();
                    }
                });
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }
    // Run after selected edit
    public void ChinhSua(final String chuoiHocKy)
    {
        String[] arrHocKyNamHoc = chuoiHocKy.split("-");
        final AlertDialog.Builder alert = new AlertDialog.Builder(QuanLyHocKyActivity.this);
        View mViewQuanLyHocKy = (View)getLayoutInflater().inflate(R.layout.dialog_add_hoc_ky,null);
        final TextView txtTieuDe = (TextView)mViewQuanLyHocKy.findViewById(R.id.txtTieuDeDialogHocKy);
        final EditText txtNamHoc = (EditText)mViewQuanLyHocKy.findViewById(R.id.txtNamHoc);
        final EditText txtHocKy = (EditText)mViewQuanLyHocKy.findViewById(R.id.txtHocKy);
        Button btnAdd = (Button)mViewQuanLyHocKy.findViewById(R.id.btnAdd);
        txtHocKy.setText(arrHocKyNamHoc[1]);
        txtNamHoc.setText(arrHocKyNamHoc[0]);
        txtTieuDe.setText("Sửa học kỳ");
        btnAdd.setText("Sửa");
        Button btnCancel = (Button)mViewQuanLyHocKy.findViewById(R.id.btnCancel);
        alert.setView(mViewQuanLyHocKy);
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
                // update data to firebase
                dataHocKy.child(user.getUid()).child(chuoiHocKy).removeValue();
                listHocKy.remove(chuoiHocKy);
                dataHocKy.child(user.getUid()).child(txtNamHoc.getText().toString()+"-"+txtHocKy.getText().toString()).setValue(txtNamHoc.getText().toString()+"-"+txtHocKy.getText().toString());
                listHocKy.add(txtNamHoc.getText().toString()+"-"+txtHocKy.getText().toString());
                adapter.notifyDataSetChanged();

                dataHocKy.child(user.getUid()).child(txtNamHoc.getText().toString()+"-"+txtHocKy.getText().toString()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //listHocKy.add(txtNamHoc.getText().toString()+txtHocKy.getText().toString());
                        Toast.makeText(QuanLyHocKyActivity.this, "Sửa học kỳ thành công", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(QuanLyHocKyActivity.this, "Sửa học kỳ thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }


    private void fillAdapterToListView()
    {
        dataHocKy.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot unit : dataSnapshot.getChildren())
                {
                    listHocKy.add(unit.getValue().toString());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

