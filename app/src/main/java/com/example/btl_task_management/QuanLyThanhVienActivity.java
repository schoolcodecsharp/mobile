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
        recyclerViewThanhVien = findViewById(R.id.recyclerViewThanhVien);
        tvBack = findViewById(R.id.tvBack);
        tvDuAn = findViewById(R.id.tvDuAn);
        tvCongViec = findViewById(R.id.tvCongViec);
        tvThongKe = findViewById(R.id.tvThongKe);
        tvLich = findViewById(R.id.tvLich);
        recyclerViewThanhVien.setLayoutManager(new LinearLayoutManager(this));
        taiDanhSachThanhVien();
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvDuAn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuanLyThanhVienActivity.this, DanhSachDuAnActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        tvCongViec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuanLyThanhVienActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        tvThongKe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuanLyThanhVienActivity.this, ThongKeActivity.class);
                startActivity(intent);
            }
        });
        tvLich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuanLyThanhVienActivity.this, LichCongViecActivity.class);
                startActivity(intent);
            }
        });
    }

    private void taiDanhSachThanhVien() {
        danhSachThanhVien = dbHelper.layThanhVienDuAn(maDuAn);
        adapter = new ThanhVienAdapter(this, danhSachThanhVien, new ThanhVienAdapter.OnItemClickListener() {
            @Override
            public void onXoaClick(NguoiDung nguoiDung) {
                xacNhanXoa(nguoiDung);
            }
        });
        recyclerViewThanhVien.setAdapter(adapter);
    }

    private void xacNhanXoa(final NguoiDung nguoiDung) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xác nhận xóa");
        builder.setMessage("Bạn có chắc chắn muốn xóa " + nguoiDung.getTenNguoiDung() + " khỏi dự án?");
        builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int result = dbHelper.xoaThanhVienDuAn(maDuAn, nguoiDung.getMaNguoiDung());
                if (result > 0) {
                    Toast.makeText(QuanLyThanhVienActivity.this, "Xóa thành viên thành công", Toast.LENGTH_SHORT).show();
                    taiDanhSachThanhVien();
                } else {
                    Toast.makeText(QuanLyThanhVienActivity.this, "Xóa thành viên thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Hủy", null);
        builder.show();
    }
}
