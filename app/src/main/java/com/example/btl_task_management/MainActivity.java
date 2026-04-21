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
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                locCongViec();
                return true;
            }
        });
        spinnerLocTrangThai.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                locCongViec();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinnerLocDanhMuc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                locCongViec();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        fabThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ThemCongViecActivity.class);
                startActivity(intent);
            }
        });
        tvQuayLaiAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AdminActivity.class);
                startActivity(intent);
            }
        });
        tvDangXuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.clear();
                editor.apply();
                Intent intent = new Intent(MainActivity.this, DangNhapActivity.class);
                startActivity(intent);
                finish();
            }
        });
        tvDuAn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DanhSachDuAnActivity.class);
                startActivity(intent);
            }
        });
        tvThongKe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ThongKeActivity.class);
                startActivity(intent);
            }
        });
        tvLich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LichCongViecActivity.class);
                startActivity(intent);
            }
        });
        tvThongTinCaNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ThongTinCaNhanActivity.class);
                startActivity(intent);
            }
        });
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
            boolean khopTuKhoa = cv.getTieuDe().toLowerCase().contains(tuKhoa) || cv.getMoTa().toLowerCase().contains(tuKhoa);
            boolean khopTrangThai = trangThai.equals("Tất cả") || cv.getTrangThai().equals(trangThai);
            boolean khopDanhMuc = false;
            if (danhMuc.equals("Tất cả")) {
                khopDanhMuc = true;
            } else if (danhMuc.equals("Cá nhân")) {
                khopDanhMuc = (cv.getDanhMuc() != null && cv.getDanhMuc().equals("Cá nhân"));
            } else {
                if (cv.getMaDuAn() > 0) {
                    DuAn duAn = dbHelper.layDuAnTheoMa(cv.getMaDuAn());
                    if (duAn != null && duAn.getTenDuAn().equals(danhMuc)) {
                        khopDanhMuc = true;
                    }
                }
            }
            if (khopTuKhoa && khopTrangThai && khopDanhMuc) {
                danhSachCongViec.add(cv);
            }
        }
        capNhatAdapter();
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
        java.util.Collections.sort(danhSach, new java.util.Comparator<CongViec>() {
            @Override
            public int compare(CongViec cv1, CongViec cv2) {
                boolean cv1HoanThanh = cv1.getTrangThai().equals("Hoàn thành");
                boolean cv2HoanThanh = cv2.getTrangThai().equals("Hoàn thành");
                if (cv1HoanThanh && !cv2HoanThanh) {
                    return 1;
                }
                if (!cv1HoanThanh && cv2HoanThanh) {
                    return -1;
                }
                int uuTien1 = getGiaTriUuTien(cv1.getMucDoUuTien());
                int uuTien2 = getGiaTriUuTien(cv2.getMucDoUuTien());
                if (uuTien1 != uuTien2) {
                    return uuTien1 - uuTien2;
                }
                int trangThai1 = getGiaTriTrangThai(cv1.getTrangThai());
                int trangThai2 = getGiaTriTrangThai(cv2.getTrangThai());
                return trangThai1 - trangThai2;
            }
        });
    }

    private int getGiaTriUuTien(String mucDoUuTien) {
        if (mucDoUuTien.equals("Cao")) return 1;
        if (mucDoUuTien.equals("Trung bình")) return 2;
        if (mucDoUuTien.equals("Thấp")) return 3;
        return 4;
    }

    private int getGiaTriTrangThai(String trangThai) {
        if (trangThai.equals("Chưa hoàn thành")) return 1;
        if (trangThai.equals("Đang thực hiện")) return 2;
        if (trangThai.equals("Hoàn thành")) return 3;
        return 4;
    }

    private void xacNhanXoa(final CongViec congViec) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xác nhận xóa");
        builder.setMessage("Bạn có chắc chắn muốn xóa công việc này?");
        builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int result = dbHelper.xoaCongViec(congViec.getMaCongViec());
                if (result > 0) {
                    Toast.makeText(MainActivity.this, "Xóa công việc thành công", Toast.LENGTH_SHORT).show();
                    taiDanhSachCongViec();
                } else {
                    Toast.makeText(MainActivity.this, "Xóa công việc thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Hủy", null);
        builder.show();
    }
}
