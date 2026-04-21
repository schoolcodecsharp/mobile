package com.example.btl_task_management;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.btl_task_management.adapter.ThanhVienAdapter;
import com.example.btl_task_management.database.DatabaseHelper;
import com.example.btl_task_management.model.NguoiDung;
import java.util.List;

public class AdminActivity extends AppCompatActivity {
    private RecyclerView recyclerViewNguoiDung;
    private ThanhVienAdapter adapter;
    private DatabaseHelper dbHelper;
    private List<NguoiDung> danhSachNguoiDung;
    private TextView tvTenAdmin, tvDangXuat, tvChuyenSangUser;
    private com.google.android.material.floatingactionbutton.FloatingActionButton fabThemNguoiDung;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String loaiTaiKhoan = prefs.getString("LoaiTaiKhoan", "User");
        if (!loaiTaiKhoan.equals("Admin")) {
            Intent intent = new Intent(AdminActivity.this, DangNhapActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        setContentView(R.layout.activity_admin);
        dbHelper = new DatabaseHelper(this);
        recyclerViewNguoiDung = findViewById(R.id.recyclerViewDuAn);
        tvTenAdmin = findViewById(R.id.tvTenAdmin);
        tvDangXuat = findViewById(R.id.tvDangXuat);
        tvChuyenSangUser = findViewById(R.id.tvChuyenSangUser);
        fabThemNguoiDung = findViewById(R.id.fabThemNguoiDung);
        String tenNguoiDung = prefs.getString("TenNguoiDung", "");
        tvTenAdmin.setText("Xin chào, " + tenNguoiDung + " (Admin)");
        recyclerViewNguoiDung.setLayoutManager(new LinearLayoutManager(this));
        taiDanhSachNguoiDung();
        fabThemNguoiDung.setOnClickListener(v -> 
            startActivity(new Intent(AdminActivity.this, ThemSuaNguoiDungActivity.class)));
        
        tvChuyenSangUser.setOnClickListener(v -> 
            startActivity(new Intent(AdminActivity.this, MainActivity.class)));
        
        tvDangXuat.setOnClickListener(v -> {
            prefs.edit().clear().apply();
            startActivity(new Intent(AdminActivity.this, DangNhapActivity.class));
            finish();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        taiDanhSachNguoiDung();
    }

    private void taiDanhSachNguoiDung() {
        danhSachNguoiDung = dbHelper.layDanhSachNguoiDung();
        adapter = new ThanhVienAdapter(this, danhSachNguoiDung, this::xacNhanXoa);
        adapter.setOnItemClickListener(this::xacNhanXoa);
        adapter.setOnItemLongClickListener(nguoiDung -> {
            Intent intent = new Intent(AdminActivity.this, ThemSuaNguoiDungActivity.class);
            intent.putExtra("MaNguoiDung", nguoiDung.getMaNguoiDung());
            intent.putExtra("TenNguoiDung", nguoiDung.getTenNguoiDung());
            intent.putExtra("Email", nguoiDung.getEmail());
            intent.putExtra("MatKhau", nguoiDung.getMatKhau());
            intent.putExtra("LoaiTaiKhoan", nguoiDung.getLoaiTaiKhoan());
            startActivity(intent);
        });
        recyclerViewNguoiDung.setAdapter(adapter);
    }

    private void xacNhanXoa(final NguoiDung nguoiDung) {
        new AlertDialog.Builder(this)
            .setTitle("Xác nhận xóa")
            .setMessage("Bạn có chắc chắn muốn xóa tài khoản " + nguoiDung.getTenNguoiDung() + "?")
            .setPositiveButton("Xóa", (dialog, which) -> {
                int result = dbHelper.xoaNguoiDung(nguoiDung.getMaNguoiDung());
                String message = result > 0 ? "Xóa tài khoản thành công" : "Xóa tài khoản thất bại";
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                if (result > 0) taiDanhSachNguoiDung();
            })
            .setNegativeButton("Hủy", null)
            .show();
    }
}
