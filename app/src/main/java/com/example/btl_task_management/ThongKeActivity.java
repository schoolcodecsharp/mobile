package com.example.btl_task_management;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.btl_task_management.database.DatabaseHelper;
import com.example.btl_task_management.model.CongViec;
import java.util.List;

public class ThongKeActivity extends AppCompatActivity {
    private TextView tvTongCongViec, tvChuaHoanThanh, tvDangThucHien, tvHoanThanh;
    private TextView tvUuTienCao, tvUuTienTrungBinh, tvUuTienThap;
    private TextView tvTiLeHoanThanh, tvTiLeDangLam, tvTiLeChuaLam;
    private Button btnQuayLai;
    private DatabaseHelper dbHelper;
    private int maNguoiDung;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_ke);
        dbHelper = new DatabaseHelper(this);
        maNguoiDung = getSharedPreferences("UserPrefs", MODE_PRIVATE).getInt("MaNguoiDung", -1);
        initViews();
        tinhThongKe();
        btnQuayLai.setOnClickListener(v -> finish());
    }

    private void initViews() {
        tvTongCongViec = findViewById(R.id.tvTongCongViec);
        tvChuaHoanThanh = findViewById(R.id.tvChuaHoanThanh);
        tvDangThucHien = findViewById(R.id.tvDangThucHien);
        tvHoanThanh = findViewById(R.id.tvHoanThanh);
        tvUuTienCao = findViewById(R.id.tvUuTienCao);
        tvUuTienTrungBinh = findViewById(R.id.tvUuTienTrungBinh);
        tvUuTienThap = findViewById(R.id.tvUuTienThap);
        tvTiLeHoanThanh = findViewById(R.id.tvTiLeHoanThanh);
        tvTiLeDangLam = findViewById(R.id.tvTiLeDangLam);
        tvTiLeChuaLam = findViewById(R.id.tvTiLeChuaLam);
        btnQuayLai = findViewById(R.id.btnQuayLai);
    }

    private void tinhThongKe() {
        List<CongViec> danhSach = dbHelper.layDanhSachCongViec(maNguoiDung);
        int[] trangThai = new int[3]; // [chua, dang, hoan]
        int[] uuTien = new int[3]; // [cao, trungbinh, thap]
        
        for (CongViec cv : danhSach) {
            switch (cv.getTrangThai()) {
                case "Chưa hoàn thành": trangThai[0]++; break;
                case "Đang thực hiện": trangThai[1]++; break;
                case "Hoàn thành": trangThai[2]++; break;
            }
            switch (cv.getMucDoUuTien()) {
                case "Cao": uuTien[0]++; break;
                case "Trung bình": uuTien[1]++; break;
                case "Thấp": uuTien[2]++; break;
            }
        }
        
        int tong = danhSach.size();
        tvTongCongViec.setText(String.valueOf(tong));
        tvChuaHoanThanh.setText(String.valueOf(trangThai[0]));
        tvDangThucHien.setText(String.valueOf(trangThai[1]));
        tvHoanThanh.setText(String.valueOf(trangThai[2]));
        tvUuTienCao.setText(String.valueOf(uuTien[0]));
        tvUuTienTrungBinh.setText(String.valueOf(uuTien[1]));
        tvUuTienThap.setText(String.valueOf(uuTien[2]));
        
        if (tong > 0) {
            tvTiLeHoanThanh.setText((trangThai[2] * 100) / tong + "%");
            tvTiLeDangLam.setText((trangThai[1] * 100) / tong + "%");
            tvTiLeChuaLam.setText((trangThai[0] * 100) / tong + "%");
        } else {
            tvTiLeHoanThanh.setText("0%");
            tvTiLeDangLam.setText("0%");
            tvTiLeChuaLam.setText("0%");
        }
    }
}
