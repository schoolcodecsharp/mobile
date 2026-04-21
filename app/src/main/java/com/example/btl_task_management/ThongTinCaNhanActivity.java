package com.example.btl_task_management;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.btl_task_management.database.DatabaseHelper;

public class ThongTinCaNhanActivity extends AppCompatActivity {
    private TextView tvEmail;
    private EditText edtTenMoi, edtMatKhauCu, edtMatKhauMoi, edtXacNhanMatKhauMoi;
    private Button btnCapNhatTen, btnDoiMatKhau, btnQuayLai;
    private DatabaseHelper dbHelper;
    private int maNguoiDung;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_tin_ca_nhan);
        
        dbHelper = new DatabaseHelper(this);
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        maNguoiDung = prefs.getInt("MaNguoiDung", -1);
        String tenNguoiDung = prefs.getString("TenNguoiDung", "");
        email = prefs.getString("Email", "");
        
        initViews();
        tvEmail.setText(email);
        edtTenMoi.setText(tenNguoiDung);
        
        btnCapNhatTen.setOnClickListener(v -> capNhatTen());
        btnDoiMatKhau.setOnClickListener(v -> doiMatKhau());
        btnQuayLai.setOnClickListener(v -> finish());
    }
    
    private void initViews() {
        tvEmail = findViewById(R.id.tvEmail);
        edtTenMoi = findViewById(R.id.edtTenMoi);
        edtMatKhauCu = findViewById(R.id.edtMatKhauCu);
        edtMatKhauMoi = findViewById(R.id.edtMatKhauMoi);
        edtXacNhanMatKhauMoi = findViewById(R.id.edtXacNhanMatKhauMoi);
        btnCapNhatTen = findViewById(R.id.btnCapNhatTen);
        btnDoiMatKhau = findViewById(R.id.btnDoiMatKhau);
        btnQuayLai = findViewById(R.id.btnQuayLai);
    }

    private void capNhatTen() {
        String tenMoi = edtTenMoi.getText().toString().trim();
        if (tenMoi.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập tên mới", Toast.LENGTH_SHORT).show();
            return;
        }
        
        int result = dbHelper.capNhatTenNguoiDung(maNguoiDung, tenMoi);
        if (result > 0) {
            getSharedPreferences("UserPrefs", MODE_PRIVATE).edit()
                .putString("TenNguoiDung", tenMoi).apply();
            Toast.makeText(this, "Cập nhật tên thành công", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Cập nhật tên thất bại", Toast.LENGTH_SHORT).show();
        }
    }

    private void doiMatKhau() {
        String matKhauCu = edtMatKhauCu.getText().toString().trim();
        String matKhauMoi = edtMatKhauMoi.getText().toString().trim();
        String xacNhanMatKhauMoi = edtXacNhanMatKhauMoi.getText().toString().trim();
        
        if (matKhauCu.isEmpty() || matKhauMoi.isEmpty() || xacNhanMatKhauMoi.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!matKhauMoi.equals(xacNhanMatKhauMoi)) {
            Toast.makeText(this, "Mật khẩu mới không khớp", Toast.LENGTH_SHORT).show();
            return;
        }
        if (dbHelper.kiemTraDangNhap(email, matKhauCu) == null) {
            Toast.makeText(this, "Mật khẩu cũ không đúng", Toast.LENGTH_SHORT).show();
            return;
        }
        
        int result = dbHelper.capNhatMatKhau(maNguoiDung, matKhauMoi);
        String message = result > 0 ? "Đổi mật khẩu thành công" : "Đổi mật khẩu thất bại";
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        
        if (result > 0) {
            edtMatKhauCu.setText("");
            edtMatKhauMoi.setText("");
            edtXacNhanMatKhauMoi.setText("");
        }
    }
}
