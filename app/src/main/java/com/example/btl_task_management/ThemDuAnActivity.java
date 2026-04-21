package com.example.btl_task_management;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.btl_task_management.database.DatabaseHelper;
import com.example.btl_task_management.model.DuAn;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ThemDuAnActivity extends AppCompatActivity {
    private EditText edtTenDuAn, edtMoTaDuAn;
    private Button btnTaoDuAn, btnHuy;
    private DatabaseHelper dbHelper;
    private int maNguoiDung;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_du_an);
        dbHelper = new DatabaseHelper(this);
        maNguoiDung = getSharedPreferences("UserPrefs", MODE_PRIVATE).getInt("MaNguoiDung", -1);
        initViews();
        setupListeners();
    }

    private void initViews() {
        edtTenDuAn = findViewById(R.id.edtTenDuAn);
        edtMoTaDuAn = findViewById(R.id.edtMoTaDuAn);
        btnTaoDuAn = findViewById(R.id.btnTaoDuAn);
        btnHuy = findViewById(R.id.btnHuy);
    }

    private void setupListeners() {
        btnTaoDuAn.setOnClickListener(v -> taoDuAn());
        btnHuy.setOnClickListener(v -> finish());
    }

    private void taoDuAn() {
        String tenDuAn = edtTenDuAn.getText().toString().trim();
        String moTa = edtMoTaDuAn.getText().toString().trim();

        if (tenDuAn.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập tên dự án", Toast.LENGTH_SHORT).show();
            return;
        }

        String ngayTao = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        DuAn duAn = new DuAn(tenDuAn, moTa, maNguoiDung, ngayTao);
        long result = dbHelper.themDuAn(duAn);
        String message = result > 0 ? "Tạo dự án thành công" : "Tạo dự án thất bại";
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        if (result > 0) finish();
    }
}
