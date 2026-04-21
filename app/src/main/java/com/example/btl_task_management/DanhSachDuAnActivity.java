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
        dbHelper = new DatabaseHelper(this);
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        maNguoiDung = prefs.getInt("MaNguoiDung", -1);
        initViews();
        tvTenNguoiDung.setText("Dự án của " + prefs.getString("TenNguoiDung", ""));
        taiDanhSachDuAn();
        setupListeners();
    }

    private void initViews() {
        recyclerViewDuAn = findViewById(R.id.recyclerViewDuAn);
        fabThemDuAn = findViewById(R.id.fabThemDuAn);
        tvBack = findViewById(R.id.tvBack);
        tvTenNguoiDung = findViewById(R.id.tvTenNguoiDung);
        tvCongViec = findViewById(R.id.tvCongViec);
        tvThongKe = findViewById(R.id.tvThongKe);
        tvLich = findViewById(R.id.tvLich);
        tvThongTinCaNhan = findViewById(R.id.tvThongTinCaNhan);
        recyclerViewDuAn.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupListeners() {
        fabThemDuAn.setOnClickListener(v -> startActivity(new Intent(this, ThemDuAnActivity.class)));
        tvBack.setOnClickListener(v -> finish());
        tvCongViec.setOnClickListener(v -> finish());
        tvThongKe.setOnClickListener(v -> startActivity(new Intent(this, ThongKeActivity.class)));
        tvLich.setOnClickListener(v -> startActivity(new Intent(this, LichCongViecActivity.class)));
        tvThongTinCaNhan.setOnClickListener(v -> startActivity(new Intent(this, ThongTinCaNhanActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        taiDanhSachDuAn();
    }

    private void taiDanhSachDuAn() {
        danhSachDuAn = dbHelper.layDanhSachDuAn(maNguoiDung);
        adapter = new DuAnAdapter(this, danhSachDuAn, duAn -> {
            Intent intent = new Intent(this, QuanLyDuAnActivity.class);
            intent.putExtra("MaDuAn", duAn.getMaDuAn());
            startActivity(intent);
        });
        recyclerViewDuAn.setAdapter(adapter);
    }
}
