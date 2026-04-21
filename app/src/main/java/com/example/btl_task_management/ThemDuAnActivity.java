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
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        maNguoiDung = prefs.getInt("MaNguoiDung", -1);
        edtTenDuAn = findViewById(R.id.edtTenDuAn);
        edtMoTaDuAn = findViewById(R.id.edtMoTaDuAn);
        btnTaoDuAn = findViewById(R.id.btnTaoDuAn);
        btnHuy = findViewById(R.id.btnHuy);
        btnTaoDuAn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tenDuAn = edtTenDuAn.getText().toString().trim();
                String moTa = edtMoTaDuAn.getText().toString().trim();
                if (tenDuAn.isEmpty()) {
                    Toast.makeText(ThemDuAnActivity.this, "Vui lòng nhập tên dự án", Toast.LENGTH_SHORT).show();
                    return;
                }
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                String ngayTao = sdf.format(new Date());
                DuAn duAn = new DuAn(tenDuAn, moTa, maNguoiDung, ngayTao);
                long result = dbHelper.themDuAn(duAn);
                if (result > 0) {
                    Toast.makeText(ThemDuAnActivity.this, "Tạo dự án thành công", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(ThemDuAnActivity.this, "Tạo dự án thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
