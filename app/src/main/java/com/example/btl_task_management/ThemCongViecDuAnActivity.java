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
import com.example.btl_task_management.model.DuAn;
import com.example.btl_task_management.model.NguoiDung;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ThemCongViecDuAnActivity extends AppCompatActivity {
    private EditText edtTieuDe, edtMoTa, edtNgayBatDau, edtNgayKetThuc;
    private Spinner spinnerThanhVien, spinnerTrangThai, spinnerUuTien;
    private Button btnThem, btnHuy;
    private DatabaseHelper dbHelper;
    private int maDuAn, maNguoiTao;
    private List<NguoiDung> danhSachThanhVien;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_cong_viec_du_an);
        
        dbHelper = new DatabaseHelper(this);
        maNguoiTao = getSharedPreferences("UserPrefs", MODE_PRIVATE).getInt("MaNguoiDung", -1);
        maDuAn = getIntent().getIntExtra("MaDuAn", -1);
        
        if (maDuAn == -1) {
            Toast.makeText(this, "Không tìm thấy dự án", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        initViews();
        loadDanhSachThanhVien();
        setupSpinners();
        setupListeners();
    }
    
    private void initViews() {
        edtTieuDe = findViewById(R.id.edtTieuDe);
        edtMoTa = findViewById(R.id.edtMoTa);
        edtNgayBatDau = findViewById(R.id.edtNgayBatDau);
        edtNgayKetThuc = findViewById(R.id.edtNgayKetThuc);
        spinnerThanhVien = findViewById(R.id.spinnerThanhVien);
        spinnerTrangThai = findViewById(R.id.spinnerTrangThai);
        spinnerUuTien = findViewById(R.id.spinnerUuTien);
        btnThem = findViewById(R.id.btnThem);
        btnHuy = findViewById(R.id.btnHuy);
    }
    
    private void loadDanhSachThanhVien() {
        danhSachThanhVien = new ArrayList<>();
        DuAn duAn = dbHelper.layDuAnTheoMa(maDuAn);
        
        if (duAn != null) {
            NguoiDung nguoiTao = dbHelper.layNguoiDungTheoMa(duAn.getMaNguoiTao());
            if (nguoiTao != null) danhSachThanhVien.add(nguoiTao);
        }
        
        List<NguoiDung> thanhVienKhac = dbHelper.layThanhVienDuAn(maDuAn);
        for (NguoiDung nd : thanhVienKhac) {
            if (danhSachThanhVien.stream().noneMatch(ndDaCo -> ndDaCo.getMaNguoiDung() == nd.getMaNguoiDung())) {
                danhSachThanhVien.add(nd);
            }
        }
    }
    
    private void setupSpinners() {
        List<String> tenThanhVien = new ArrayList<>();
        for (NguoiDung nd : danhSachThanhVien) {
            tenThanhVien.add(nd.getTenNguoiDung() + " (" + nd.getEmail() + ")");
        }
        if (tenThanhVien.isEmpty()) tenThanhVien.add("Chưa có thành viên");
        
        ArrayAdapter<String> adapterThanhVien = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tenThanhVien);
        adapterThanhVien.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerThanhVien.setAdapter(adapterThanhVien);
        
        String[] trangThai = {"Chưa hoàn thành", "Đang thực hiện", "Hoàn thành"};
        ArrayAdapter<String> adapterTrangThai = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, trangThai);
        adapterTrangThai.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTrangThai.setAdapter(adapterTrangThai);
        
        String[] uuTien = {"Cao", "Trung bình", "Thấp"};
        ArrayAdapter<String> adapterUuTien = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, uuTien);
        adapterUuTien.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUuTien.setAdapter(adapterUuTien);
        spinnerUuTien.setSelection(1);
    }
    
    private void setupListeners() {
        edtNgayBatDau.setOnClickListener(v -> showDatePicker(edtNgayBatDau));
        edtNgayKetThuc.setOnClickListener(v -> showDatePicker(edtNgayKetThuc));
        btnThem.setOnClickListener(v -> themCongViec());
        btnHuy.setOnClickListener(v -> finish());
    }
    
    private void themCongViec() {
        String tieuDe = edtTieuDe.getText().toString().trim();
        if (tieuDe.isEmpty() || edtNgayBatDau.getText().toString().isEmpty() || 
            edtNgayKetThuc.getText().toString().isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }
        if (danhSachThanhVien.isEmpty()) {
            Toast.makeText(this, "Dự án chưa có thành viên. Vui lòng thêm thành viên trước", Toast.LENGTH_SHORT).show();
            return;
        }
        
        NguoiDung nguoiGiao = danhSachThanhVien.get(spinnerThanhVien.getSelectedItemPosition());
        CongViec cv = new CongViec();
        cv.setMaNguoiDung(nguoiGiao.getMaNguoiDung());
        cv.setMaDuAn(maDuAn);
        cv.setMaNguoiDuocGiao(nguoiGiao.getMaNguoiDung());
        cv.setMaNguoiTao(maNguoiTao);
        cv.setDanhMuc("Dự án");
        cv.setTieuDe(tieuDe);
        cv.setMoTa(edtMoTa.getText().toString().trim());
        cv.setNgayBatDau(edtNgayBatDau.getText().toString().trim());
        cv.setNgayKetThuc(edtNgayKetThuc.getText().toString().trim());
        cv.setTrangThai(spinnerTrangThai.getSelectedItem().toString());
        cv.setMucDoUuTien(spinnerUuTien.getSelectedItem().toString());
        
        long result = dbHelper.themCongViecDuAn(cv);
        String message = result > 0 ? "Thêm công việc thành công" : "Thêm công việc thất bại";
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        if (result > 0) finish();
    }

    private void showDatePicker(EditText editText) {
        Calendar cal = Calendar.getInstance();
        new DatePickerDialog(this, 
            (view, year, month, day) -> editText.setText(String.format("%02d/%02d/%d", day, month + 1, year)),
            cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)
        ).show();
    }
}
