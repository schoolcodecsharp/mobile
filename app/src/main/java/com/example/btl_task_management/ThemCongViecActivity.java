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
        maNguoiDung = getSharedPreferences("UserPrefs", MODE_PRIVATE).getInt("MaNguoiDung", -1);
        
        initViews();
        setupSpinners();
        loadDataIfEdit();
        setupListeners();
    }
    
    private void initViews() {
        edtTieuDe = findViewById(R.id.edtTieuDe);
        edtMoTa = findViewById(R.id.edtMoTa);
        edtNgayBatDau = findViewById(R.id.edtNgayBatDau);
        edtNgayKetThuc = findViewById(R.id.edtNgayKetThuc);
        spinnerDanhMuc = findViewById(R.id.spinnerDanhMuc);
        spinnerTrangThai = findViewById(R.id.spinnerTrangThai);
        spinnerMucDoUuTien = findViewById(R.id.spinnerMucDoUuTien);
        btnLuu = findViewById(R.id.btnLuu);
        btnHuy = findViewById(R.id.btnHuy);
    }
    
    private void setupSpinners() {
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
    }
    
    private void loadDataIfEdit() {
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
    }
    
    private void setupListeners() {
        edtNgayBatDau.setOnClickListener(v -> showDatePicker(edtNgayBatDau));
        edtNgayKetThuc.setOnClickListener(v -> showDatePicker(edtNgayKetThuc));
        btnLuu.setOnClickListener(v -> luuCongViec());
        btnHuy.setOnClickListener(v -> finish());
    }

    private void showDatePicker(EditText editText) {
        Calendar cal = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, day) -> 
            editText.setText(String.format("%02d/%02d/%d", day, month + 1, year)),
            cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    private void luuCongViec() {
        String tieuDe = edtTieuDe.getText().toString().trim();
        if (tieuDe.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập tiêu đề", Toast.LENGTH_SHORT).show();
            return;
        }

        CongViec cv = congViecSua != null ? congViecSua : new CongViec();
        cv.setMaNguoiDung(maNguoiDung);
        cv.setDanhMuc(spinnerDanhMuc.getSelectedItem().toString());
        cv.setTieuDe(tieuDe);
        cv.setMoTa(edtMoTa.getText().toString().trim());
        cv.setNgayBatDau(edtNgayBatDau.getText().toString().trim());
        cv.setNgayKetThuc(edtNgayKetThuc.getText().toString().trim());
        cv.setTrangThai(spinnerTrangThai.getSelectedItem().toString());
        cv.setMucDoUuTien(spinnerMucDoUuTien.getSelectedItem().toString());

        long result = congViecSua == null ? dbHelper.themCongViecCaNhan(cv) : dbHelper.capNhatCongViec(cv);
        String action = congViecSua == null ? "Thêm" : "Cập nhật";
        String message = result > 0 ? action + " công việc thành công" : action + " công việc thất bại";
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        if (result > 0) finish();
    }

    private int getIndexDanhMuc(String danhMuc) {
        String[] array = {"Cá nhân", "Học tập", "Công việc"};
        for (int i = 0; i < array.length; i++) 
            if (array[i].equals(danhMuc)) return i;
        return 0;
    }

    private int getIndexTrangThai(String trangThai) {
        String[] array = getResources().getStringArray(R.array.trang_thai_array);
        for (int i = 0; i < array.length; i++) 
            if (array[i].equals(trangThai)) return i;
        return 0;
    }

    private int getIndexUuTien(String uuTien) {
        String[] array = getResources().getStringArray(R.array.muc_do_uu_tien_array);
        for (int i = 0; i < array.length; i++) 
            if (array[i].equals(uuTien)) return i;
        return 1;
    }
}
