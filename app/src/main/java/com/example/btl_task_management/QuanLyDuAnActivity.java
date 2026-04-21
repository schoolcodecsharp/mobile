package com.example.btl_task_management;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.btl_task_management.adapter.CongViecAdapter;
import com.example.btl_task_management.database.DatabaseHelper;
import com.example.btl_task_management.model.CongViec;
import com.example.btl_task_management.model.DuAn;
import com.example.btl_task_management.model.NguoiDung;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.io.Serializable;
import java.util.List;

public class QuanLyDuAnActivity extends AppCompatActivity {
    private RecyclerView recyclerViewCongViec;
    private CongViecAdapter adapter;
    private DatabaseHelper dbHelper;
    private List<CongViec> danhSachCongViec;
    private FloatingActionButton fabThemCongViec;
    private TextView tvTenDuAn, tvMoTaDuAn, tvBack;
    private Button btnThemThanhVien, btnQuanLyThanhVien;
    private int maDuAn;
    private DuAn duAn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_du_an);
        dbHelper = new DatabaseHelper(this);
        maDuAn = getIntent().getIntExtra("MaDuAn", -1);
        if (maDuAn == -1) {
            Toast.makeText(this, "Không tìm thấy dự án", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        duAn = dbHelper.layDuAnTheoMa(maDuAn);
        recyclerViewCongViec = findViewById(R.id.recyclerViewCongViec);
        fabThemCongViec = findViewById(R.id.fabThemCongViec);
        tvTenDuAn = findViewById(R.id.tvTenDuAn);
        tvMoTaDuAn = findViewById(R.id.tvMoTaDuAn);
        tvBack = findViewById(R.id.tvBack);
        btnThemThanhVien = findViewById(R.id.btnThemThanhVien);
        btnQuanLyThanhVien = findViewById(R.id.btnQuanLyThanhVien);
        tvTenDuAn.setText(duAn.getTenDuAn());
        tvMoTaDuAn.setText(duAn.getMoTa());
        recyclerViewCongViec.setLayoutManager(new LinearLayoutManager(this));
        taiDanhSachCongViec();
        tvBack.setOnClickListener(v -> finish());
        
        fabThemCongViec.setOnClickListener(v -> {
            Intent intent = new Intent(QuanLyDuAnActivity.this, ThemCongViecDuAnActivity.class);
            intent.putExtra("MaDuAn", maDuAn);
            startActivity(intent);
        });
        
        btnQuanLyThanhVien.setOnClickListener(v -> {
            Intent intent = new Intent(QuanLyDuAnActivity.this, QuanLyThanhVienActivity.class);
            intent.putExtra("MaDuAn", maDuAn);
            startActivity(intent);
        });
        
        btnThemThanhVien.setOnClickListener(v -> hienThiDialogThemThanhVien());
    }

    @Override
    protected void onResume() {
        super.onResume();
        taiDanhSachCongViec();
    }

    private void taiDanhSachCongViec() {
        danhSachCongViec = dbHelper.layDanhSachCongViecTheoDuAn(maDuAn);
        adapter = new CongViecAdapter(this, danhSachCongViec, new CongViecAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(CongViec congViec) {
                Intent intent = new Intent(QuanLyDuAnActivity.this, ChiTietCongViecActivity.class);
                intent.putExtra("CongViec", (Serializable) congViec);
                startActivity(intent);
            }
            @Override
            public void onItemLongClick(CongViec congViec) {}
        });
        recyclerViewCongViec.setAdapter(adapter);
    }

    private void hienThiDialogThemThanhVien() {
        final EditText edtEmail = new EditText(this);
        edtEmail.setHint("Nhập email người dùng");
        
        new AlertDialog.Builder(this)
            .setTitle("Thêm thành viên vào dự án")
            .setView(edtEmail)
            .setPositiveButton("Thêm", (dialog, which) -> themThanhVien(edtEmail.getText().toString().trim()))
            .setNegativeButton("Hủy", null)
            .show();
    }
    
    private void themThanhVien(String email) {
        if (email.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập email", Toast.LENGTH_SHORT).show();
            return;
        }
        
        NguoiDung nguoiDung = dbHelper.layNguoiDungTheoEmail(email);
        if (nguoiDung == null) {
            Toast.makeText(this, "Không tìm thấy người dùng với email này", Toast.LENGTH_SHORT).show();
            return;
        }
        if (nguoiDung.getLoaiTaiKhoan().equals("Admin")) {
            Toast.makeText(this, "Không thể thêm Admin vào dự án", Toast.LENGTH_SHORT).show();
            return;
        }
        
        long result = dbHelper.themThanhVienDuAn(maDuAn, nguoiDung.getMaNguoiDung());
        String message = result > 0 ? "Thêm thành viên thành công" : "Thêm thành viên thất bại hoặc đã tồn tại";
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
