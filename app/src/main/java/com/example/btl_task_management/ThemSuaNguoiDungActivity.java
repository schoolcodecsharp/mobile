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
        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tenNguoiDung = edtTenNguoiDung.getText().toString().trim();
                String email = edtEmail.getText().toString().trim();
                String matKhau = edtMatKhau.getText().toString().trim();
                String loaiTaiKhoan = spinnerLoaiTaiKhoan.getSelectedItem().toString();
                if (tenNguoiDung.isEmpty() || email.isEmpty() || matKhau.isEmpty()) {
                    Toast.makeText(ThemSuaNguoiDungActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (isEdit) {
                    int result = dbHelper.capNhatNguoiDung(maNguoiDung, tenNguoiDung, email, matKhau, loaiTaiKhoan);
                    if (result > 0) {
                        Toast.makeText(ThemSuaNguoiDungActivity.this, "Cập nhật tài khoản thành công", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(ThemSuaNguoiDungActivity.this, "Cập nhật tài khoản thất bại", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    NguoiDung nguoiDung = new NguoiDung(tenNguoiDung, email, matKhau, loaiTaiKhoan);
                    long result = dbHelper.themNguoiDung(nguoiDung);
                    if (result > 0) {
                        Toast.makeText(ThemSuaNguoiDungActivity.this, "Thêm tài khoản thành công", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(ThemSuaNguoiDungActivity.this, "Thêm tài khoản thất bại. Email có thể đã tồn tại", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
