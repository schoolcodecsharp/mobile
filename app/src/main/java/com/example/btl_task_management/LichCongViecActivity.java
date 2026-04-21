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
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        maNguoiDung = prefs.getInt("MaNguoiDung", -1);
        calendarView = findViewById(R.id.calendarView);
        recyclerView = findViewById(R.id.recyclerView);
        tvNgayChon = findViewById(R.id.tvNgayChon);
        tvKhongCoCongViec = findViewById(R.id.tvKhongCoCongViec);
        btnQuayLai = findViewById(R.id.btnQuayLai);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                ngayChon = String.format("%02d/%02d/%d", dayOfMonth, month + 1, year);
                tvNgayChon.setText("Công việc ngày: " + ngayChon);
                hienThiCongViecTheoNgay();
            }
        });
        btnQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void hienThiCongViecTheoNgay() {
        // Lấy tất cả công việc của user
        List<CongViec> tatCaCongViec = dbHelper.layDanhSachCongViec(maNguoiDung);
        List<CongViec> congViecTheoNgay = new ArrayList<>();
        
        // Lọc công việc theo ngày đã chọn
        for (CongViec cv : tatCaCongViec) {
            // Kiểm tra công việc có ngày không (không rỗng)
            if (!cv.getNgayBatDau().isEmpty() && !cv.getNgayKetThuc().isEmpty()) {
                // Kiểm tra ngày chọn có nằm trong khoảng [ngayBatDau, ngayKetThuc] không
                if (laNgayTrongKhoang(ngayChon, cv.getNgayBatDau(), cv.getNgayKetThuc())) {
                    congViecTheoNgay.add(cv);
                }
            }
        }
        
        // Sắp xếp: Ưu tiên (Cao → Trung bình → Thấp) → Trạng thái (Chưa → Đang → Hoàn thành)
        sapXepCongViec(congViecTheoNgay);
        
        // Hiển thị kết quả
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
                public void onItemLongClick(CongViec congViec) {
                }
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
        java.util.Collections.sort(danhSach, new java.util.Comparator<CongViec>() {
            @Override
            public int compare(CongViec cv1, CongViec cv2) {
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
}
