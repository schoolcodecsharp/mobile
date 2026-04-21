package com.example.btl_task_management;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.btl_task_management.adapter.DuAnAdapter;
import com.example.btl_task_management.database.DatabaseHelper;
import com.example.btl_task_management.model.DuAn;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

public class DanhSachDuAnActivity extends AppCompatActivity {
    private RecyclerView recyclerViewDuAn;
    private DuAnAdapter adapter;
    private DatabaseHelper dbHelper;
    private List<DuAn> danhSachDuAn;
    private FloatingActionButton fabThemDuAn;
    private TextView tvBack, tvTenNguoiDung, tvCongViec, tvThongKe, tvLich, tvThongTinCaNhan;
    private int maNguoiDung;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_du_an);
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        maNguoiDung = prefs.getInt("MaNguoiDung", -1);
        String tenNguoiDung = prefs.getString("TenNguoiDung", "");
        dbHelper = new DatabaseHelper(this);
        recyclerViewDuAn = findViewById(R.id.recyclerViewDuAn);
        fabThemDuAn = findViewById(R.id.fabThemDuAn);
        tvBack = findViewById(R.id.tvBack);
        tvTenNguoiDung = findViewById(R.id.tvTenNguoiDung);
        tvCongViec = findViewById(R.id.tvCongViec);
        tvThongKe = findViewById(R.id.tvThongKe);
        tvLich = findViewById(R.id.tvLich);
        tvThongTinCaNhan = findViewById(R.id.tvThongTinCaNhan);
        tvTenNguoiDung.setText("Dự án của " + tenNguoiDung);
        recyclerViewDuAn.setLayoutManager(new LinearLayoutManager(this));
        taiDanhSachDuAn();
        fabThemDuAn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DanhSachDuAnActivity.this, ThemDuAnActivity.class);
                startActivity(intent);
            }
        });
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvCongViec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvThongKe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DanhSachDuAnActivity.this, ThongKeActivity.class);
                startActivity(intent);
            }
        });
        tvLich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DanhSachDuAnActivity.this, LichCongViecActivity.class);
                startActivity(intent);
            }
        });
        tvThongTinCaNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DanhSachDuAnActivity.this, ThongTinCaNhanActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        taiDanhSachDuAn();
    }

    private void taiDanhSachDuAn() {
        danhSachDuAn = dbHelper.layDanhSachDuAn(maNguoiDung);
        adapter = new DuAnAdapter(this, danhSachDuAn, new DuAnAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DuAn duAn) {
                Intent intent = new Intent(DanhSachDuAnActivity.this, QuanLyDuAnActivity.class);
                intent.putExtra("MaDuAn", duAn.getMaDuAn());
                startActivity(intent);
            }
        });
        recyclerViewDuAn.setAdapter(adapter);
    }
}
