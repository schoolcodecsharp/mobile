package com.example.btl_task_management;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.btl_task_management.database.DatabaseHelper;
import com.example.btl_task_management.model.NguoiDung;

public class ThemSuaNguoiDungActivity extends AppCompatActivity {
    private EditText edtTenNguoiDung, edtEmail, edtMatKhau;
    private Spinner spinnerLoaiTaiKhoan;
    private Button btnLuu, btnHuy;
    private TextView tvTieuDe;
    private DatabaseHelper dbHelper;
    private int maNguoiDung = -1;
    private boolean isEdit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_sua_nguoi_dung);
        dbHelper = new DatabaseHelper(this);
        tvTieuDe = findViewById(R.id.tvTieuDe);
        edtTenNguoiDung = findViewById(R.id.edtTenNguoiDung);
        edtEmail = findViewById(R.id.edtEmail);
        edtMatKhau = findViewById(R.id.edtMatKhau);
        spinnerLoaiTaiKhoan = findViewById(R.id.spinnerLoaiTaiKhoan);
        btnLuu = findViewById(R.id.btnLuu);
        btnHuy = findViewById(R.id.btnHuy);
        String[] loaiTaiKhoan = {"User", "Admin"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, loaiTaiKhoan);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLoaiTaiKhoan.setAdapter(adapter);
        maNguoiDung = getIntent().getIntExtra("MaNguoiDung", -1);
        if (maNguoiDung != -1) {
            isEdit = true;
            tvTieuDe.setText("SỬA TÀI KHOẢN");
            btnLuu.setText("CẬP NHẬT");
            String tenNguoiDung = getIntent().getStringExtra("TenNguoiDung");
            String email = getIntent().getStringExtra("Email");
            String matKhau = getIntent().getStringExtra("MatKhau");
            String loaiTK = getIntent().getStringExtra("LoaiTaiKhoan");
            edtTenNguoiDung.setText(tenNguoiDung);
            edtEmail.setText(email);
            edtMatKhau.setText(matKhau);
            if (loaiTK.equals("Admin")) {
                spinnerLoaiTaiKhoan.setSelection(1);
            }
        } else {
            tvTieuDe.setText("THÊM TÀI KHOẢN MỚI");
            btnLuu.setText("THÊM");
        }
        btnLuu.setOnClickListener(v -> luuNguoiDung());
        btnHuy.setOnClickListener(v -> finish());
    }
    
    private void luuNguoiDung() {
        String tenNguoiDung = edtTenNguoiDung.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String matKhau = edtMatKhau.getText().toString().trim();
        String loaiTaiKhoan = spinnerLoaiTaiKhoan.getSelectedItem().toString();
        
        if (tenNguoiDung.isEmpty() || email.isEmpty() || matKhau.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (isEdit) {
            capNhatNguoiDung(tenNguoiDung, email, matKhau, loaiTaiKhoan);
        } else {
            themNguoiDungMoi(tenNguoiDung, email, matKhau, loaiTaiKhoan);
        }
    }
    
    private void capNhatNguoiDung(String ten, String email, String matKhau, String loaiTK) {
        int result = dbHelper.capNhatNguoiDung(maNguoiDung, ten, email, matKhau, loaiTK);
        String message = result > 0 ? "Cập nhật tài khoản thành công" : "Cập nhật tài khoản thất bại";
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        if (result > 0) finish();
    }
    
    private void themNguoiDungMoi(String ten, String email, String matKhau, String loaiTK) {
        NguoiDung nguoiDung = new NguoiDung(ten, email, matKhau, loaiTK);
        long result = dbHelper.themNguoiDung(nguoiDung);
        String message = result > 0 ? "Thêm tài khoản thành công" : "Thêm tài khoản thất bại. Email có thể đã tồn tại";
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        if (result > 0) finish();
    }
}
