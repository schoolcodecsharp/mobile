package com.example.btl_task_management;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.btl_task_management.database.DatabaseHelper;
import com.example.btl_task_management.model.CongViec;
import java.util.Calendar;

public class ThemCongViecActivity extends AppCompatActivity {
    private EditText edtTieuDe, edtMoTa, edtNgayBatDau, edtNgayKetThuc;
    private Spinner spinnerDanhMuc, spinnerTrangThai, spinnerMucDoUuTien;
    private Button btnLuu, btnHuy;
    private DatabaseHelper dbHelper;
    private int maNguoiDung;
    private CongViec congViecSua;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_cong_viec);
        dbHelper = new DatabaseHelper(this);
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        maNguoiDung = prefs.getInt("MaNguoiDung", -1);
        edtTieuDe = findViewById(R.id.edtTieuDe);
        edtMoTa = findViewById(R.id.edtMoTa);
        edtNgayBatDau = findViewById(R.id.edtNgayBatDau);
        edtNgayKetThuc = findViewById(R.id.edtNgayKetThuc);
        spinnerDanhMuc = findViewById(R.id.spinnerDanhMuc);
        spinnerTrangThai = findViewById(R.id.spinnerTrangThai);
        spinnerMucDoUuTien = findViewById(R.id.spinnerMucDoUuTien);
        btnLuu = findViewById(R.id.btnLuu);
        btnHuy = findViewById(R.id.btnHuy);
        String[] danhMuc = {"Cá nhân", "Học tập", "Công việc"};
        ArrayAdapter<String> adapterDanhMuc = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, danhMuc);
        adapterDanhMuc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDanhMuc.setAdapter(adapterDanhMuc);
        ArrayAdapter<CharSequence> adapterTrangThai = ArrayAdapter.createFromResource(this, R.array.trang_thai_array, android.R.layout.simple_spinner_item);
        adapterTrangThai.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTrangThai.setAdapter(adapterTrangThai);
        ArrayAdapter<CharSequence> adapterUuTien = ArrayAdapter.createFromResource(this, R.array.muc_do_uu_tien_array, android.R.layout.simple_spinner_item);
        adapterUuTien.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMucDoUuTien.setAdapter(adapterUuTien);
        if (getIntent().hasExtra("CongViec")) {
            congViecSua = (CongViec) getIntent().getSerializableExtra("CongViec");
            edtTieuDe.setText(congViecSua.getTieuDe());
            edtMoTa.setText(congViecSua.getMoTa());
            edtNgayBatDau.setText(congViecSua.getNgayBatDau());
            edtNgayKetThuc.setText(congViecSua.getNgayKetThuc());
            if (congViecSua.getDanhMuc() != null) {
                spinnerDanhMuc.setSelection(getIndexDanhMuc(congViecSua.getDanhMuc()));
            }
            spinnerTrangThai.setSelection(getIndexTrangThai(congViecSua.getTrangThai()));
            spinnerMucDoUuTien.setSelection(getIndexUuTien(congViecSua.getMucDoUuTien()));
        }
        edtNgayBatDau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(edtNgayBatDau);
            }
        });
        edtNgayKetThuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(edtNgayKetThuc);
            }
        });
        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                luuCongViec();
            }
        });
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void showDatePicker(final EditText editText) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String date = String.format("%02d/%02d/%d", dayOfMonth, month + 1, year);
                editText.setText(date);
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    private void luuCongViec() {
        String tieuDe = edtTieuDe.getText().toString().trim();
        String moTa = edtMoTa.getText().toString().trim();
        String ngayBatDau = edtNgayBatDau.getText().toString().trim();
        String ngayKetThuc = edtNgayKetThuc.getText().toString().trim();
        String danhMuc = spinnerDanhMuc.getSelectedItem().toString();
        String trangThai = spinnerTrangThai.getSelectedItem().toString();
        String mucDoUuTien = spinnerMucDoUuTien.getSelectedItem().toString();
        if (tieuDe.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập tiêu đề", Toast.LENGTH_SHORT).show();
            return;
        }
        if (congViecSua == null) {
            CongViec congViec = new CongViec();
            congViec.setMaNguoiDung(maNguoiDung);
            congViec.setDanhMuc(danhMuc);
            congViec.setTieuDe(tieuDe);
            congViec.setMoTa(moTa);
            congViec.setNgayBatDau(ngayBatDau);
            congViec.setNgayKetThuc(ngayKetThuc);
            congViec.setTrangThai(trangThai);
            congViec.setMucDoUuTien(mucDoUuTien);
            long result = dbHelper.themCongViecCaNhan(congViec);
            if (result > 0) {
                Toast.makeText(this, "Thêm công việc thành công", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Thêm công việc thất bại", Toast.LENGTH_SHORT).show();
            }
        } else {
            congViecSua.setDanhMuc(danhMuc);
            congViecSua.setTieuDe(tieuDe);
            congViecSua.setMoTa(moTa);
            congViecSua.setNgayBatDau(ngayBatDau);
            congViecSua.setNgayKetThuc(ngayKetThuc);
            congViecSua.setTrangThai(trangThai);
            congViecSua.setMucDoUuTien(mucDoUuTien);
            int result = dbHelper.capNhatCongViec(congViecSua);
            if (result > 0) {
                Toast.makeText(this, "Cập nhật công việc thành công", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Cập nhật công việc thất bại", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private int getIndexDanhMuc(String danhMuc) {
        String[] array = {"Cá nhân", "Học tập", "Công việc"};
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(danhMuc)) return i;
        }
        return 0;
    }

    private int getIndexTrangThai(String trangThai) {
        String[] array = getResources().getStringArray(R.array.trang_thai_array);
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(trangThai)) return i;
        }
        return 0;
    }

    private int getIndexUuTien(String uuTien) {
        String[] array = getResources().getStringArray(R.array.muc_do_uu_tien_array);
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(uuTien)) return i;
        }
        return 1;
    }
}
