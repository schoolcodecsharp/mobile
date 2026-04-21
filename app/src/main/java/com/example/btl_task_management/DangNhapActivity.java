package com.example.btl_task_management;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.btl_task_management.database.DatabaseHelper;
import com.example.btl_task_management.model.NguoiDung;

public class DangNhapActivity extends AppCompatActivity {
    private EditText edtEmail, edtMatKhau;
    private Button btnDangNhap;
    private TextView tvDangKy;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);
        dbHelper = new DatabaseHelper(this);
        edtEmail = findViewById(R.id.edtEmail);
        edtMatKhau = findViewById(R.id.edtMatKhau);
        btnDangNhap = findViewById(R.id.btnDangNhap);
        tvDangKy = findViewById(R.id.tvDangKy);
        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString().trim();
                String matKhau = edtMatKhau.getText().toString().trim();
                if (email.isEmpty() || matKhau.isEmpty()) {
                    Toast.makeText(DangNhapActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }
                NguoiDung nguoiDung = dbHelper.kiemTraDangNhap(email, matKhau);
                if (nguoiDung != null) {
                    SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putInt("MaNguoiDung", nguoiDung.getMaNguoiDung());
                    editor.putString("TenNguoiDung", nguoiDung.getTenNguoiDung());
                    editor.putString("Email", nguoiDung.getEmail());
                    editor.putString("LoaiTaiKhoan", nguoiDung.getLoaiTaiKhoan());
                    editor.apply();
                    Toast.makeText(DangNhapActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                    if (nguoiDung.getLoaiTaiKhoan().equals("Admin")) {
                        Intent intent = new Intent(DangNhapActivity.this, AdminActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(DangNhapActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Toast.makeText(DangNhapActivity.this, "Email hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
                }
            }
        });
        tvDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DangNhapActivity.this, DangKyActivity.class);
                startActivity(intent);
            }
        });
    }
}
