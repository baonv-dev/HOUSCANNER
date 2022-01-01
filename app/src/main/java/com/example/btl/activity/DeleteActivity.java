package com.example.btl.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.btl.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DeleteActivity extends AppCompatActivity {

    private DatabaseReference dataFireBase;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);
        user = FirebaseAuth.getInstance().getCurrentUser();
        dataFireBase = FirebaseDatabase.getInstance().getReference();

    }
    public void deleteAll(View view)
    {
        //dataFireBase.child("diemdanh").child(user.getUid()).setValue(null);
        //dataFireBase.child("HocKy").child(user.getUid()).setValue(null);
        //dataFireBase.child("Lop").child(user.getUid()).setValue(null);
        Toast.makeText(this, "Đã xóa thành công", Toast.LENGTH_SHORT).show();
    }
}
