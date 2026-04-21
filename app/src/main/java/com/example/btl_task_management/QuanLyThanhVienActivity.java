package com.example.btl_task_management;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

public class QuanLyThanhVienActivity extends AppCompatActivity {
    private RecyclerView recyclerViewThanhVien;
    private ThanhVienAdapter adapter;
    private DatabaseHelper dbHelper;
    private List<NguoiDung> danhSachThanhVien;
    private TextView tvBack, tvDuAn, tvCongViec, tvThongKe, tvLich;
    private int maDuAn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_thanh_vien);
        dbHelper = new DatabaseHelper(this);
        maDuAn = getIntent().getIntExtra("MaDuAn", -1);
        initViews();
        taiDanhSachThanhVien();
        setupListeners();
    }

    private void initViews() {
        recyclerViewThanhVien = findViewById(R.id.recyclerViewThanhVien);
        tvBack = findViewById(R.id.tvBack);
        tvDuAn = findViewById(R.id.tvDuAn);
        tvCongViec = findViewById(R.id.tvCongViec);
        tvThongKe = findViewById(R.id.tvThongKe);
        tvLich = findViewById(R.id.tvLich);
        recyclerViewThanhVien.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupListeners() {
        tvBack.setOnClickListener(v -> finish());
        tvDuAn.setOnClickListener(v -> {
            Intent intent = new Intent(this, DanhSachDuAnActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
        tvCongViec.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
        tvThongKe.setOnClickListener(v -> startActivity(new Intent(this, ThongKeActivity.class)));
        tvLich.setOnClickListener(v -> startActivity(new Intent(this, LichCongViecActivity.class)));
    }

    private void taiDanhSachThanhVien() {
        danhSachThanhVien = dbHelper.layThanhVienDuAn(maDuAn);
        adapter = new ThanhVienAdapter(this, danhSachThanhVien, this::xacNhanXoa);
        recyclerViewThanhVien.setAdapter(adapter);
    }

    private void xacNhanXoa(NguoiDung nguoiDung) {
        new AlertDialog.Builder(this)
            .setTitle("Xác nhận xóa")
            .setMessage("Bạn có chắc chắn muốn xóa " + nguoiDung.getTenNguoiDung() + " khỏi dự án?")
            .setPositiveButton("Xóa", (dialog, which) -> {
                int result = dbHelper.xoaThanhVienDuAn(maDuAn, nguoiDung.getMaNguoiDung());
                String message = result > 0 ? "Xóa thành viên thành công" : "Xóa thành viên thất bại";
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                if (result > 0) taiDanhSachThanhVien();
            })
            .setNegativeButton("Hủy", null)
            .show();
    }
}
