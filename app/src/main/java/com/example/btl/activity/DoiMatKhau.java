package com.example.btl.activity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.btl.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.example.btl.R.id.matkhaucu;

public class DoiMatKhau extends AppCompatActivity {
    private Button btnDoiMatKhau;
    private TextInputEditText txtMatKhauCu;
    private TextInputEditText txtMatKhauMoi;
    private TextInputEditText txtMatKhauNhapLaiMatKhau;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doi_mat_khau);
    }

    public void doiMatKhau(View view)
    {
        txtMatKhauCu = (TextInputEditText)findViewById(R.id.matkhaucu);
        txtMatKhauMoi = (TextInputEditText)findViewById(R.id.matkhaumoi);
        txtMatKhauNhapLaiMatKhau = (TextInputEditText)findViewById(R.id.nhaplaimatkhaumoi);
        btnDoiMatKhau = (Button)findViewById(R.id.doimatkhau);
        user = FirebaseAuth.getInstance().getCurrentUser();
        // such as GoogleAuthProvider or FacebookAuthProvider.
        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), txtMatKhauCu.getText().toString());

        // Prompt the user to re-provide their sign-in credentials
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            user.updatePassword(txtMatKhauMoi.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(DoiMatKhau.this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(DoiMatKhau.this, "Lỗi trong khi đổi mật khẩu", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(DoiMatKhau.this, "Mật khẩu cũ không đúng", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
