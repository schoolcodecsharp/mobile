package com.example.btl_task_management;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.btl_task_management.adapter.CongViecAdapter;
import com.example.btl_task_management.database.DatabaseHelper;
import com.example.btl_task_management.model.CongViec;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LichCongViecActivity extends AppCompatActivity {
    private CalendarView calendarView;
    private RecyclerView recyclerView;
    private TextView tvNgayChon, tvKhongCoCongViec;
    private Button btnQuayLai;
    private CongViecAdapter adapter;
    private DatabaseHelper dbHelper;
    private int maNguoiDung;
    private String ngayChon = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lich_cong_viec);
        dbHelper = new DatabaseHelper(this);
        maNguoiDung = getSharedPreferences("UserPrefs", MODE_PRIVATE).getInt("MaNguoiDung", -1);
        initViews();
        setupListeners();
    }

    private void initViews() {
        calendarView = findViewById(R.id.calendarView);
        recyclerView = findViewById(R.id.recyclerView);
        tvNgayChon = findViewById(R.id.tvNgayChon);
        tvKhongCoCongViec = findViewById(R.id.tvKhongCoCongViec);
        btnQuayLai = findViewById(R.id.btnQuayLai);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupListeners() {
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            ngayChon = String.format("%02d/%02d/%d", dayOfMonth, month + 1, year);
            tvNgayChon.setText("Công việc ngày: " + ngayChon);
            hienThiCongViecTheoNgay();
        });
        btnQuayLai.setOnClickListener(v -> finish());
    }

    private void hienThiCongViecTheoNgay() {
        List<CongViec> tatCaCongViec = dbHelper.layDanhSachCongViec(maNguoiDung);
        List<CongViec> congViecTheoNgay = new ArrayList<>();
        
        for (CongViec cv : tatCaCongViec) {
            if (!cv.getNgayBatDau().isEmpty() && !cv.getNgayKetThuc().isEmpty() &&
                laNgayTrongKhoang(ngayChon, cv.getNgayBatDau(), cv.getNgayKetThuc())) {
                congViecTheoNgay.add(cv);
            }
        }
        
        sapXepCongViec(congViecTheoNgay);
        
        if (congViecTheoNgay.isEmpty()) {
            tvKhongCoCongViec.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            tvKhongCoCongViec.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            adapter = new CongViecAdapter(this, congViecTheoNgay, new CongViecAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(CongViec congViec) {
                    android.content.Intent intent = new android.content.Intent(LichCongViecActivity.this, ChiTietCongViecActivity.class);
                    intent.putExtra("CongViec", (Serializable) congViec);
                    startActivity(intent);
                }
                @Override
                public void onItemLongClick(CongViec congViec) {}
            });
            recyclerView.setAdapter(adapter);
        }
    }
    
    private boolean laNgayTrongKhoang(String ngayKiemTra, String ngayBatDau, String ngayKetThuc) {
        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
            java.util.Date dateKiemTra = sdf.parse(ngayKiemTra);
            java.util.Date dateBatDau = sdf.parse(ngayBatDau);
            java.util.Date dateKetThuc = sdf.parse(ngayKetThuc);
            
            return !dateKiemTra.before(dateBatDau) && !dateKiemTra.after(dateKetThuc);
        } catch (Exception e) {
            return false;
        }
    }
    
    private void sapXepCongViec(List<CongViec> danhSach) {
        java.util.Collections.sort(danhSach, (cv1, cv2) -> {
            int uuTien1 = getGiaTriUuTien(cv1.getMucDoUuTien());
            int uuTien2 = getGiaTriUuTien(cv2.getMucDoUuTien());
            if (uuTien1 != uuTien2) return uuTien1 - uuTien2;
            return getGiaTriTrangThai(cv1.getTrangThai()) - getGiaTriTrangThai(cv2.getTrangThai());
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
}
