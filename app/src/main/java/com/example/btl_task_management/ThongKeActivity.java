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
        int tongCongViec = danhSach.size();
        int chuaHoanThanh = 0;
        int dangThucHien = 0;
        int hoanThanh = 0;
        int uuTienCao = 0;
        int uuTienTrungBinh = 0;
        int uuTienThap = 0;
        for (CongViec cv : danhSach) {
            if (cv.getTrangThai().equals("Chưa hoàn thành")) {
                chuaHoanThanh++;
            } else if (cv.getTrangThai().equals("Đang thực hiện")) {
                dangThucHien++;
            } else if (cv.getTrangThai().equals("Hoàn thành")) {
                hoanThanh++;
            }
            if (cv.getMucDoUuTien().equals("Cao")) {
                uuTienCao++;
            } else if (cv.getMucDoUuTien().equals("Trung bình")) {
                uuTienTrungBinh++;
            } else if (cv.getMucDoUuTien().equals("Thấp")) {
                uuTienThap++;
            }
        }
        tvTongCongViec.setText(String.valueOf(tongCongViec));
        tvChuaHoanThanh.setText(String.valueOf(chuaHoanThanh));
        tvDangThucHien.setText(String.valueOf(dangThucHien));
        tvHoanThanh.setText(String.valueOf(hoanThanh));
        tvUuTienCao.setText(String.valueOf(uuTienCao));
        tvUuTienTrungBinh.setText(String.valueOf(uuTienTrungBinh));
        tvUuTienThap.setText(String.valueOf(uuTienThap));
        if (tongCongViec > 0) {
            tvTiLeHoanThanh.setText((hoanThanh * 100) / tongCongViec + "%");
            tvTiLeDangLam.setText((dangThucHien * 100) / tongCongViec + "%");
            tvTiLeChuaLam.setText((chuaHoanThanh * 100) / tongCongViec + "%");
        } else {
            tvTiLeHoanThanh.setText("0%");
            tvTiLeDangLam.setText("0%");
            tvTiLeChuaLam.setText("0%");
        }
    }
}
