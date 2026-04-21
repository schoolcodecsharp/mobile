package com.example.btl_task_management;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.btl_task_management.adapter.CongViecAdapter;
import com.example.btl_task_management.database.DatabaseHelper;
import com.example.btl_task_management.model.CongViec;
import com.example.btl_task_management.model.DuAn;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CongViecAdapter adapter;
    private DatabaseHelper dbHelper;
    private List<CongViec> danhSachCongViec;
    private List<CongViec> danhSachGoc;
    private FloatingActionButton fabThem;
    private TextView tvTenNguoiDung, tvDangXuat, tvQuayLaiAdmin, tvDuAn, tvThongKe, tvLich, tvThongTinCaNhan;
    private SearchView searchView;
    private Spinner spinnerLocTrangThai, spinnerLocDanhMuc;
    private int maNguoiDung;
    private String loaiTaiKhoan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        maNguoiDung = prefs.getInt("MaNguoiDung", -1);
        loaiTaiKhoan = prefs.getString("LoaiTaiKhoan", "User");
        if (maNguoiDung == -1) {
            Intent intent = new Intent(MainActivity.this, DangNhapActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        setContentView(R.layout.activity_main);
        dbHelper = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerView);
        fabThem = findViewById(R.id.fabThem);
        tvTenNguoiDung = findViewById(R.id.tvTenNguoiDung);
        tvDangXuat = findViewById(R.id.tvDangXuat);
        tvQuayLaiAdmin = findViewById(R.id.tvQuayLaiAdmin);
        tvDuAn = findViewById(R.id.tvDuAn);
        tvThongKe = findViewById(R.id.tvThongKe);
        tvLich = findViewById(R.id.tvLich);
        tvThongTinCaNhan = findViewById(R.id.tvThongTinCaNhan);
        searchView = findViewById(R.id.searchView);
        spinnerLocTrangThai = findViewById(R.id.spinnerLocTrangThai);
        spinnerLocDanhMuc = findViewById(R.id.spinnerLocDanhMuc);
        String tenNguoiDung = prefs.getString("TenNguoiDung", "");
        tvTenNguoiDung.setText("Xin chào, " + tenNguoiDung);
        if (loaiTaiKhoan.equals("Admin")) {
            tvQuayLaiAdmin.setVisibility(View.VISIBLE);
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        khoiTaoSpinner();
        taiDanhSachCongViec();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { return false; }
            @Override
            public boolean onQueryTextChange(String newText) {
                locCongViec();
                return true;
            }
        });
        
        AdapterView.OnItemSelectedListener spinnerListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                locCongViec();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        };
        
        spinnerLocTrangThai.setOnItemSelectedListener(spinnerListener);
        spinnerLocDanhMuc.setOnItemSelectedListener(spinnerListener);
        fabThem.setOnClickListener(v -> 
            startActivity(new Intent(MainActivity.this, ThemCongViecActivity.class)));
        
        tvQuayLaiAdmin.setOnClickListener(v -> 
            startActivity(new Intent(MainActivity.this, AdminActivity.class)));
        
        tvDangXuat.setOnClickListener(v -> {
            prefs.edit().clear().apply();
            startActivity(new Intent(MainActivity.this, DangNhapActivity.class));
            finish();
        });
        
        tvDuAn.setOnClickListener(v -> 
            startActivity(new Intent(MainActivity.this, DanhSachDuAnActivity.class)));
        
        tvThongKe.setOnClickListener(v -> 
            startActivity(new Intent(MainActivity.this, ThongKeActivity.class)));
        
        tvLich.setOnClickListener(v -> 
            startActivity(new Intent(MainActivity.this, LichCongViecActivity.class)));
        
        tvThongTinCaNhan.setOnClickListener(v -> 
            startActivity(new Intent(MainActivity.this, ThongTinCaNhanActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        taiDanhSachCongViec();
    }

    private void khoiTaoSpinner() {
        String[] trangThai = {"Tất cả", "Chưa hoàn thành", "Đang thực hiện", "Hoàn thành"};
        ArrayAdapter<String> adapterTrangThai = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, trangThai);
        adapterTrangThai.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLocTrangThai.setAdapter(adapterTrangThai);
        List<String> danhMuc = new ArrayList<>();
        danhMuc.add("Tất cả");
        danhMuc.add("Cá nhân");
        List<DuAn> danhSachDuAn = dbHelper.layDanhSachDuAn(maNguoiDung);
        for (DuAn duAn : danhSachDuAn) {
            danhMuc.add(duAn.getTenDuAn());
        }
        ArrayAdapter<String> adapterDanhMuc = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, danhMuc);
        adapterDanhMuc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLocDanhMuc.setAdapter(adapterDanhMuc);
    }

    private void taiDanhSachCongViec() {
        danhSachGoc = dbHelper.layDanhSachCongViec(maNguoiDung);
        sapXepDanhSachCongViec(danhSachGoc);
        danhSachCongViec = new ArrayList<>(danhSachGoc);
        locCongViec();
    }

    private void locCongViec() {
        String tuKhoa = searchView.getQuery().toString().toLowerCase();
        String trangThai = spinnerLocTrangThai.getSelectedItem().toString();
        String danhMuc = spinnerLocDanhMuc.getSelectedItem().toString();
        
        danhSachCongViec = new ArrayList<>();
        for (CongViec cv : danhSachGoc) {
            if (khopDieuKien(cv, tuKhoa, trangThai, danhMuc)) {
                danhSachCongViec.add(cv);
            }
        }
        capNhatAdapter();
    }

    private boolean khopDieuKien(CongViec cv, String tuKhoa, String trangThai, String danhMuc) {
        boolean khopTuKhoa = cv.getTieuDe().toLowerCase().contains(tuKhoa) || 
                             cv.getMoTa().toLowerCase().contains(tuKhoa);
        boolean khopTrangThai = trangThai.equals("Tất cả") || cv.getTrangThai().equals(trangThai);
        boolean khopDanhMuc = kiemTraDanhMuc(cv, danhMuc);
        return khopTuKhoa && khopTrangThai && khopDanhMuc;
    }

    private boolean kiemTraDanhMuc(CongViec cv, String danhMuc) {
        if (danhMuc.equals("Tất cả")) return true;
        if (danhMuc.equals("Cá nhân")) return cv.getDanhMuc() != null && cv.getDanhMuc().equals("Cá nhân");
        if (cv.getMaDuAn() > 0) {
            DuAn duAn = dbHelper.layDuAnTheoMa(cv.getMaDuAn());
            return duAn != null && duAn.getTenDuAn().equals(danhMuc);
        }
        return false;
    }

    private void capNhatAdapter() {
        adapter = new CongViecAdapter(this, danhSachCongViec, new CongViecAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(CongViec congViec) {
                Intent intent = new Intent(MainActivity.this, ChiTietCongViecActivity.class);
                intent.putExtra("CongViec", (Serializable) congViec);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(CongViec congViec) {
                xacNhanXoa(congViec);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void sapXepDanhSachCongViec(List<CongViec> danhSach) {
        java.util.Collections.sort(danhSach, (cv1, cv2) -> {
            boolean cv1Done = cv1.getTrangThai().equals("Hoàn thành");
            boolean cv2Done = cv2.getTrangThai().equals("Hoàn thành");
            if (cv1Done != cv2Done) return cv1Done ? 1 : -1;
            
            int uuTien = getGiaTriUuTien(cv1.getMucDoUuTien()) - getGiaTriUuTien(cv2.getMucDoUuTien());
            return uuTien != 0 ? uuTien : getGiaTriTrangThai(cv1.getTrangThai()) - getGiaTriTrangThai(cv2.getTrangThai());
        });
    }

    private int getGiaTriUuTien(String uuTien) {
        switch (uuTien) {
            case "Cao": return 1;
            case "Trung bình": return 2;
            case "Thấp": return 3;
            default: return 4;
        }
    }

    private int getGiaTriTrangThai(String trangThai) {
        switch (trangThai) {
            case "Chưa hoàn thành": return 1;
            case "Đang thực hiện": return 2;
            case "Hoàn thành": return 3;
            default: return 4;
        }
    }

    private void xacNhanXoa(final CongViec congViec) {
        new AlertDialog.Builder(this)
            .setTitle("Xác nhận xóa")
            .setMessage("Bạn có chắc chắn muốn xóa công việc này?")
            .setPositiveButton("Xóa", (dialog, which) -> {
                int result = dbHelper.xoaCongViec(congViec.getMaCongViec());
                String message = result > 0 ? "Xóa công việc thành công" : "Xóa công việc thất bại";
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                if (result > 0) taiDanhSachCongViec();
            })
            .setNegativeButton("Hủy", null)
            .show();
    }
}
