package com.example.btl_task_management;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.btl_task_management.database.DatabaseHelper;
import com.example.btl_task_management.model.CongViec;
import java.io.Serializable;

public class ChiTietCongViecActivity extends AppCompatActivity {
    private TextView tvTieuDe, tvMoTa, tvNgayBatDau, tvNgayKetThuc, tvTrangThai, tvMucDoUuTien;
    private Button btnSua, btnXoa, btnQuayLai;
    private CongViec congViec;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_cong_viec);
        dbHelper = new DatabaseHelper(this);
        tvTieuDe = findViewById(R.id.tvTieuDe);
        tvMoTa = findViewById(R.id.tvMoTa);
        tvNgayBatDau = findViewById(R.id.tvNgayBatDau);
        tvNgayKetThuc = findViewById(R.id.tvNgayKetThuc);
        tvTrangThai = findViewById(R.id.tvTrangThai);
        tvMucDoUuTien = findViewById(R.id.tvMucDoUuTien);
        btnSua = findViewById(R.id.btnSua);
        btnXoa = findViewById(R.id.btnXoa);
        btnQuayLai = findViewById(R.id.btnQuayLai);
        congViec = (CongViec) getIntent().getSerializableExtra("CongViec");
        if (congViec != null) {
            hienThiThongTin();
        }
        btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChiTietCongViecActivity.this, ThemCongViecActivity.class);
                intent.putExtra("CongViec", (Serializable) congViec);
                startActivity(intent);
                finish();
            }
        });
        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xacNhanXoa();
            }
        });
        btnQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void hienThiThongTin() {
        tvTieuDe.setText(congViec.getTieuDe());
        tvMoTa.setText(congViec.getMoTa().isEmpty() ? "Không có mô tả" : congViec.getMoTa());
        tvNgayBatDau.setText(congViec.getNgayBatDau().isEmpty() ? "Chưa xác định" : congViec.getNgayBatDau());
        tvNgayKetThuc.setText(congViec.getNgayKetThuc().isEmpty() ? "Chưa xác định" : congViec.getNgayKetThuc());
        tvTrangThai.setText(congViec.getTrangThai());
        tvMucDoUuTien.setText(congViec.getMucDoUuTien());
    }

    private void xacNhanXoa() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xác nhận xóa");
        builder.setMessage("Bạn có chắc chắn muốn xóa công việc này?");
        builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int result = dbHelper.xoaCongViec(congViec.getMaCongViec());
                if (result > 0) {
                    Toast.makeText(ChiTietCongViecActivity.this, "Xóa công việc thành công", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(ChiTietCongViecActivity.this, "Xóa công việc thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Hủy", null);
        builder.show();
    }
}
