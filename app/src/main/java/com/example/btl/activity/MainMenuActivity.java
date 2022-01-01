package com.example.btl.activity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.example.btl.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

public class MainMenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private FirebaseUser user;
    private TextView txtMailDangNhap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View header=navigationView.getHeaderView(0);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        user = FirebaseAuth.getInstance().getCurrentUser();
        txtMailDangNhap = (TextView)header.findViewById(R.id.txtEmailDangNhap);
        txtMailDangNhap.setText(user.getEmail());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    public void gotoThongKeLop(View view)
    {
        Intent intent = new Intent(MainMenuActivity.this, ThongKeActivity.class);
        startActivity(intent);
        Toast.makeText(this, "Thống kê lớp", Toast.LENGTH_SHORT).show();
    }
    public void goToManagerClass(View view)
    {
        Intent intent = new Intent(MainMenuActivity.this, QuanLyHocKyActivity.class);
        startActivity(intent);
        Toast.makeText(this, "Quản Lý Lóp", Toast.LENGTH_SHORT).show();
    }
    public void gotoAttendanceStudent(View view)
    {
        Intent intent = new Intent(MainMenuActivity.this, AttendanceBarcodeActivity.class);
        startActivity(intent);
        Toast.makeText(this, "Điểm danh sinh viên", Toast.LENGTH_SHORT).show();
    }
    public void gotoInfo(View view)
    {
        Intent intent = new Intent(MainMenuActivity.this, InfoAppActivity.class);
        startActivity(intent);
    }
    public void gotoDelete(View view)
    {
        Intent intent = new Intent(MainMenuActivity.this, DeleteActivity.class);
        startActivity(intent);
        Toast.makeText(this, "Điểm danh sinh viên", Toast.LENGTH_SHORT).show();
    }
    public void gotoDoiMatKhau(View view)
    {
        Intent intent = new Intent(MainMenuActivity.this, DoiMatKhau.class);
        startActivity(intent);
        Toast.makeText(this, "Đã đăng xuất", Toast.LENGTH_SHORT).show();
    }
    public void logOut(View view)
    {
        FirebaseAuth.getInstance().signOut();
        Intent goToLogin = new Intent(MainMenuActivity.this,LoginActivity.class);
        startActivity(goToLogin);
        Toast.makeText(this, "Đã đăng xuất", Toast.LENGTH_SHORT).show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            Toast.makeText(this, "Đang ở trang chủ", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_diemdanh) {
            Intent intent = new Intent(MainMenuActivity.this, AttendanceBarcodeActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_listclass) {
            Intent intent = new Intent(MainMenuActivity.this, QuanLyHocKyActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_thongke) {
            Intent intent = new Intent(MainMenuActivity.this, ThongKeActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_xoadulieu) {
            Intent intent = new Intent(MainMenuActivity.this, DeleteActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_thongtincanhan) {
            Intent intent = new Intent(MainMenuActivity.this, InfoAppActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut();
            Intent goToLogin = new Intent(MainMenuActivity.this,LoginActivity.class);
            startActivity(goToLogin);
            Toast.makeText(this, "Đăng xuất", Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
