package com.example.btl_task_management;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.btl_task_management.database.DatabaseHelper;
import com.example.btl_task_management.model.NguoiDung;

public class DangKyActivity extends AppCompatActivity {
    private EditText edtTenNguoiDung, edtEmail, edtMatKhau, edtXacNhanMatKhau;
    private Button btnDangKy;
    private TextView tvDangNhap;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);
        dbHelper = new DatabaseHelper(this);
        edtTenNguoiDung = findViewById(R.id.edtTenNguoiDung);
        edtEmail = findViewById(R.id.edtEmail);
        edtMatKhau = findViewById(R.id.edtMatKhau);
        edtXacNhanMatKhau = findViewById(R.id.edtXacNhanMatKhau);
        btnDangKy = findViewById(R.id.btnDangKy);
        tvDangNhap = findViewById(R.id.tvDangNhap);
        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tenNguoiDung = edtTenNguoiDung.getText().toString().trim();
                String email = edtEmail.getText().toString().trim();
                String matKhau = edtMatKhau.getText().toString().trim();
                String xacNhanMatKhau = edtXacNhanMatKhau.getText().toString().trim();
                if (tenNguoiDung.isEmpty() || email.isEmpty() || matKhau.isEmpty() || xacNhanMatKhau.isEmpty()) {
                    Toast.makeText(DangKyActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!matKhau.equals(xacNhanMatKhau)) {
                    Toast.makeText(DangKyActivity.this, "Mật khẩu xác nhận không khớp", Toast.LENGTH_SHORT).show();
                    return;
                }
                NguoiDung nguoiDung = new NguoiDung(tenNguoiDung, email, matKhau, "User");
                long result = dbHelper.themNguoiDung(nguoiDung);
                if (result > 0) {
                    Toast.makeText(DangKyActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(DangKyActivity.this, "Đăng ký thất bại. Email có thể đã tồn tại", Toast.LENGTH_SHORT).show();
                }
            }
        });
        tvDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
