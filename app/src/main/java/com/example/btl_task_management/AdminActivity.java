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
        fabThemNguoiDung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, ThemSuaNguoiDungActivity.class);
                startActivity(intent);
            }
        });
        tvChuyenSangUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        tvDangXuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.clear();
                editor.apply();
                Intent intent = new Intent(AdminActivity.this, DangNhapActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        taiDanhSachNguoiDung();
    }

    private void taiDanhSachNguoiDung() {
        danhSachNguoiDung = dbHelper.layDanhSachNguoiDung();
        adapter = new ThanhVienAdapter(this, danhSachNguoiDung, new ThanhVienAdapter.OnItemClickListener() {
            @Override
            public void onXoaClick(NguoiDung nguoiDung) {
                xacNhanXoa(nguoiDung);
            }
        });
        adapter.setOnItemClickListener(new ThanhVienAdapter.OnItemClickListener() {
            @Override
            public void onXoaClick(NguoiDung nguoiDung) {
                xacNhanXoa(nguoiDung);
            }
        });
        adapter.setOnItemLongClickListener(new ThanhVienAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(NguoiDung nguoiDung) {
                Intent intent = new Intent(AdminActivity.this, ThemSuaNguoiDungActivity.class);
                intent.putExtra("MaNguoiDung", nguoiDung.getMaNguoiDung());
                intent.putExtra("TenNguoiDung", nguoiDung.getTenNguoiDung());
                intent.putExtra("Email", nguoiDung.getEmail());
                intent.putExtra("MatKhau", nguoiDung.getMatKhau());
                intent.putExtra("LoaiTaiKhoan", nguoiDung.getLoaiTaiKhoan());
                startActivity(intent);
            }
        });
        recyclerViewNguoiDung.setAdapter(adapter);
    }

    private void xacNhanXoa(final NguoiDung nguoiDung) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xác nhận xóa");
        builder.setMessage("Bạn có chắc chắn muốn xóa tài khoản " + nguoiDung.getTenNguoiDung() + "?");
        builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int result = dbHelper.xoaNguoiDung(nguoiDung.getMaNguoiDung());
                if (result > 0) {
                    Toast.makeText(AdminActivity.this, "Xóa tài khoản thành công", Toast.LENGTH_SHORT).show();
                    taiDanhSachNguoiDung();
                } else {
                    Toast.makeText(AdminActivity.this, "Xóa tài khoản thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Hủy", null);
        builder.show();
    }
}
