package com.example.btl_task_management.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.btl_task_management.model.NguoiDung;
import com.example.btl_task_management.model.CongViec;
import com.example.btl_task_management.model.DuAn;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {
    private Context context;
    private SQLiteDatabase database;
    private static final String DATABASE_NAME = "QuanLyCongViec.db";

    public DatabaseHelper(Context context) {
        this.context = context;
        File dbFile = context.getDatabasePath(DATABASE_NAME);
        database = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        taoDatabase();
    }

    private void taoDatabase() {
        database.execSQL("CREATE TABLE IF NOT EXISTS NguoiDung (MaNguoiDung INTEGER PRIMARY KEY AUTOINCREMENT, TenNguoiDung TEXT NOT NULL, Email TEXT UNIQUE NOT NULL, MatKhau TEXT NOT NULL, LoaiTaiKhoan TEXT DEFAULT 'User')");
        database.execSQL("CREATE TABLE IF NOT EXISTS DuAn (MaDuAn INTEGER PRIMARY KEY AUTOINCREMENT, TenDuAn TEXT NOT NULL, MoTa TEXT, MaNguoiTao INTEGER, NgayTao TEXT)");
        database.execSQL("CREATE TABLE IF NOT EXISTS ThanhVienDuAn (MaThanhVien INTEGER PRIMARY KEY AUTOINCREMENT, MaDuAn INTEGER, MaNguoiDung INTEGER)");
        database.execSQL("CREATE TABLE IF NOT EXISTS CongViec (MaCongViec INTEGER PRIMARY KEY AUTOINCREMENT, MaNguoiDung INTEGER, MaDuAn INTEGER, MaNguoiDuocGiao INTEGER, MaNguoiTao INTEGER, DanhMuc TEXT DEFAULT 'Cá nhân', TieuDe TEXT NOT NULL, MoTa TEXT, NgayBatDau TEXT, NgayKetThuc TEXT, TrangThai TEXT DEFAULT 'Chưa hoàn thành', MucDoUuTien TEXT DEFAULT 'Trung bình')");
        themDuLieuMau();
    }

    private void themDuLieuMau() {
        Cursor cursor = database.rawQuery("SELECT COUNT(*) FROM NguoiDung", null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        if (count == 0) {
            database.execSQL("INSERT INTO NguoiDung (TenNguoiDung, Email, MatKhau, LoaiTaiKhoan) VALUES ('Admin Hệ Thống', 'a@gmail.com', '1', 'Admin')");
            database.execSQL("INSERT INTO NguoiDung (TenNguoiDung, Email, MatKhau, LoaiTaiKhoan) VALUES ('Nguyễn Văn Trường', 't@gmail.com', '1', 'User')");
            database.execSQL("INSERT INTO NguoiDung (TenNguoiDung, Email, MatKhau, LoaiTaiKhoan) VALUES ('Trần Thị Bình', 'binh@gmail.com', '123456', 'User')");
            database.execSQL("INSERT INTO NguoiDung (TenNguoiDung, Email, MatKhau, LoaiTaiKhoan) VALUES ('Lê Văn Cường', 'cuong@gmail.com', '123456', 'User')");
            database.execSQL("INSERT INTO DuAn (TenDuAn, MoTa, MaNguoiTao, NgayTao) VALUES ('Dự án Quản lý Công việc', 'Xây dựng ứng dụng quản lý công việc trên Android', 1, '01/04/2026')");
            database.execSQL("INSERT INTO DuAn (TenDuAn, MoTa, MaNguoiTao, NgayTao) VALUES ('Dự án Marketing', 'Chiến dịch marketing quý 2', 1, '05/04/2026')");
            database.execSQL("INSERT INTO ThanhVienDuAn (MaDuAn, MaNguoiDung) VALUES (1, 2)");
            database.execSQL("INSERT INTO ThanhVienDuAn (MaDuAn, MaNguoiDung) VALUES (1, 3)");
            database.execSQL("INSERT INTO ThanhVienDuAn (MaDuAn, MaNguoiDung) VALUES (2, 3)");
            database.execSQL("INSERT INTO ThanhVienDuAn (MaDuAn, MaNguoiDung) VALUES (2, 4)");
            database.execSQL("INSERT INTO CongViec (MaNguoiDung, MaDuAn, MaNguoiDuocGiao, MaNguoiTao, DanhMuc, TieuDe, MoTa, NgayBatDau, NgayKetThuc, TrangThai, MucDoUuTien) VALUES (2, 1, 2, 1, 'Dự án', 'Thiết kế giao diện', 'Thiết kế giao diện cho ứng dụng', '01/04/2026', '15/04/2026', 'Đang thực hiện', 'Cao')");
            database.execSQL("INSERT INTO CongViec (MaNguoiDung, MaDuAn, MaNguoiDuocGiao, MaNguoiTao, DanhMuc, TieuDe, MoTa, NgayBatDau, NgayKetThuc, TrangThai, MucDoUuTien) VALUES (3, 1, 3, 1, 'Dự án', 'Xây dựng database', 'Tạo cấu trúc database SQLite', '05/04/2026', '20/04/2026', 'Hoàn thành', 'Cao')");
            database.execSQL("INSERT INTO CongViec (MaNguoiDung, MaDuAn, MaNguoiDuocGiao, MaNguoiTao, DanhMuc, TieuDe, MoTa, NgayBatDau, NgayKetThuc, TrangThai, MucDoUuTien) VALUES (3, 2, 3, 1, 'Dự án', 'Viết content marketing', 'Viết nội dung cho chiến dịch', '08/04/2026', '18/04/2026', 'Đang thực hiện', 'Trung bình')");
            database.execSQL("INSERT INTO CongViec (MaNguoiDung, DanhMuc, TieuDe, MoTa, NgayBatDau, NgayKetThuc, TrangThai, MucDoUuTien) VALUES (2, 'Cá nhân', 'Mua sắm cuối tuần', 'Đi siêu thị mua đồ dùng', '13/04/2026', '13/04/2026', 'Chưa hoàn thành', 'Thấp')");
            database.execSQL("INSERT INTO CongViec (MaNguoiDung, DanhMuc, TieuDe, MoTa, NgayBatDau, NgayKetThuc, TrangThai, MucDoUuTien) VALUES (2, 'Học tập', 'Học Android Studio', 'Tìm hiểu về SQLite và RecyclerView', '05/04/2026', '20/04/2026', 'Đang thực hiện', 'Cao')");
            database.execSQL("INSERT INTO CongViec (MaNguoiDung, DanhMuc, TieuDe, MoTa, NgayBatDau, NgayKetThuc, TrangThai, MucDoUuTien) VALUES (4, 'Công việc', 'Hoàn thành báo cáo tháng', 'Viết báo cáo tổng kết công việc tháng 4', '01/04/2026', '22/04/2026', 'Chưa hoàn thành', 'Cao')");
        }
    }

    public long themNguoiDung(NguoiDung nguoiDung) {
        ContentValues values = new ContentValues();
        values.put("TenNguoiDung", nguoiDung.getTenNguoiDung());
        values.put("Email", nguoiDung.getEmail());
        values.put("MatKhau", nguoiDung.getMatKhau());
        values.put("LoaiTaiKhoan", nguoiDung.getLoaiTaiKhoan());
        return database.insert("NguoiDung", null, values);
    }

    public NguoiDung kiemTraDangNhap(String email, String matKhau) {
        Cursor cursor = database.query("NguoiDung", null, "Email=? AND MatKhau=?", new String[]{email, matKhau}, null, null, null);
        NguoiDung nguoiDung = null;
        if (cursor != null && cursor.moveToFirst()) {
            nguoiDung = new NguoiDung(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
            cursor.close();
        }
        return nguoiDung;
    }

    public long themCongViecCaNhan(CongViec congViec) {
        ContentValues values = new ContentValues();
        values.put("MaNguoiDung", congViec.getMaNguoiDung());
        values.put("DanhMuc", congViec.getDanhMuc());
        values.put("TieuDe", congViec.getTieuDe());
        values.put("MoTa", congViec.getMoTa());
        values.put("NgayBatDau", congViec.getNgayBatDau());
        values.put("NgayKetThuc", congViec.getNgayKetThuc());
        values.put("TrangThai", congViec.getTrangThai());
        values.put("MucDoUuTien", congViec.getMucDoUuTien());
        return database.insert("CongViec", null, values);
    }

    public List<CongViec> layDanhSachCongViec(int maNguoiDung) {
        List<CongViec> danhSach = new ArrayList<>();
        Cursor cursor = database.query("CongViec", null, "MaNguoiDung=?", new String[]{String.valueOf(maNguoiDung)}, null, null, "MaCongViec DESC");
        if (cursor != null && cursor.moveToFirst()) {
            do {
                CongViec cv = new CongViec();
                cv.setMaCongViec(cursor.getInt(0));
                cv.setMaNguoiDung(cursor.getInt(1));
                cv.setMaDuAn(cursor.getInt(2));
                cv.setMaNguoiDuocGiao(cursor.getInt(3));
                cv.setMaNguoiTao(cursor.getInt(4));
                cv.setDanhMuc(cursor.getString(5));
                cv.setTieuDe(cursor.getString(6));
                cv.setMoTa(cursor.getString(7));
                cv.setNgayBatDau(cursor.getString(8));
                cv.setNgayKetThuc(cursor.getString(9));
                cv.setTrangThai(cursor.getString(10));
                cv.setMucDoUuTien(cursor.getString(11));
                danhSach.add(cv);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return danhSach;
    }

    public int capNhatCongViec(CongViec congViec) {
        ContentValues values = new ContentValues();
        values.put("DanhMuc", congViec.getDanhMuc());
        values.put("TieuDe", congViec.getTieuDe());
        values.put("MoTa", congViec.getMoTa());
        values.put("NgayBatDau", congViec.getNgayBatDau());
        values.put("NgayKetThuc", congViec.getNgayKetThuc());
        values.put("TrangThai", congViec.getTrangThai());
        values.put("MucDoUuTien", congViec.getMucDoUuTien());
        return database.update("CongViec", values, "MaCongViec=?", new String[]{String.valueOf(congViec.getMaCongViec())});
    }

    public int xoaCongViec(int maCongViec) {
        return database.delete("CongViec", "MaCongViec=?", new String[]{String.valueOf(maCongViec)});
    }

    public int capNhatTenNguoiDung(int maNguoiDung, String tenMoi) {
        ContentValues values = new ContentValues();
        values.put("TenNguoiDung", tenMoi);
        return database.update("NguoiDung", values, "MaNguoiDung=?", new String[]{String.valueOf(maNguoiDung)});
    }

    public int capNhatMatKhau(int maNguoiDung, String matKhauMoi) {
        ContentValues values = new ContentValues();
        values.put("MatKhau", matKhauMoi);
        return database.update("NguoiDung", values, "MaNguoiDung=?", new String[]{String.valueOf(maNguoiDung)});
    }

    public List<NguoiDung> layDanhSachNguoiDung() {
        List<NguoiDung> danhSach = new ArrayList<>();
        Cursor cursor = database.query("NguoiDung", null, "LoaiTaiKhoan=?", new String[]{"User"}, null, null, "TenNguoiDung ASC");
        if (cursor != null && cursor.moveToFirst()) {
            do {
                NguoiDung nd = new NguoiDung(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
                danhSach.add(nd);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return danhSach;
    }

    public NguoiDung layNguoiDungTheoEmail(String email) {
        Cursor cursor = database.query("NguoiDung", null, "Email=?", new String[]{email}, null, null, null);
        NguoiDung nguoiDung = null;
        if (cursor != null && cursor.moveToFirst()) {
            nguoiDung = new NguoiDung(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
            cursor.close();
        }
        return nguoiDung;
    }

    public long themDuAn(DuAn duAn) {
        ContentValues values = new ContentValues();
        values.put("TenDuAn", duAn.getTenDuAn());
        values.put("MoTa", duAn.getMoTa());
        values.put("MaNguoiTao", duAn.getMaNguoiTao());
        values.put("NgayTao", duAn.getNgayTao());
        return database.insert("DuAn", null, values);
    }

    public List<DuAn> layDanhSachDuAn(int maNguoiTao) {
        List<DuAn> danhSach = new ArrayList<>();
        Cursor cursor = database.query("DuAn", null, "MaNguoiTao=?", new String[]{String.valueOf(maNguoiTao)}, null, null, "MaDuAn DESC");
        if (cursor != null && cursor.moveToFirst()) {
            do {
                DuAn da = new DuAn(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3), cursor.getString(4));
                danhSach.add(da);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return danhSach;
    }

    public long themThanhVienDuAn(int maDuAn, int maNguoiDung) {
        ContentValues values = new ContentValues();
        values.put("MaDuAn", maDuAn);
        values.put("MaNguoiDung", maNguoiDung);
        return database.insert("ThanhVienDuAn", null, values);
    }

    public List<NguoiDung> layThanhVienDuAn(int maDuAn) {
        List<NguoiDung> danhSach = new ArrayList<>();
        String query = "SELECT nd.* FROM NguoiDung nd INNER JOIN ThanhVienDuAn tv ON nd.MaNguoiDung = tv.MaNguoiDung WHERE tv.MaDuAn = ?";
        Cursor cursor = database.rawQuery(query, new String[]{String.valueOf(maDuAn)});
        if (cursor != null && cursor.moveToFirst()) {
            do {
                NguoiDung nd = new NguoiDung(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
                danhSach.add(nd);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return danhSach;
    }

    public List<CongViec> layDanhSachCongViecTheoDuAn(int maDuAn) {
        List<CongViec> danhSach = new ArrayList<>();
        Cursor cursor = database.query("CongViec", null, "MaDuAn=?", new String[]{String.valueOf(maDuAn)}, null, null, "MaCongViec DESC");
        if (cursor != null && cursor.moveToFirst()) {
            do {
                CongViec cv = new CongViec();
                cv.setMaCongViec(cursor.getInt(0));
                cv.setMaNguoiDung(cursor.getInt(1));
                cv.setMaDuAn(cursor.getInt(2));
                cv.setMaNguoiDuocGiao(cursor.getInt(3));
                cv.setMaNguoiTao(cursor.getInt(4));
                cv.setDanhMuc(cursor.getString(5));
                cv.setTieuDe(cursor.getString(6));
                cv.setMoTa(cursor.getString(7));
                cv.setNgayBatDau(cursor.getString(8));
                cv.setNgayKetThuc(cursor.getString(9));
                cv.setTrangThai(cursor.getString(10));
                cv.setMucDoUuTien(cursor.getString(11));
                danhSach.add(cv);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return danhSach;
    }

    public DuAn layDuAnTheoMa(int maDuAn) {
        Cursor cursor = database.query("DuAn", null, "MaDuAn=?", new String[]{String.valueOf(maDuAn)}, null, null, null);
        DuAn duAn = null;
        if (cursor != null && cursor.moveToFirst()) {
            duAn = new DuAn(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3), cursor.getString(4));
            cursor.close();
        }
        return duAn;
    }

    public long themCongViecDuAn(CongViec congViec) {
        ContentValues values = new ContentValues();
        values.put("MaNguoiDung", congViec.getMaNguoiDung());
        values.put("MaDuAn", congViec.getMaDuAn());
        values.put("MaNguoiDuocGiao", congViec.getMaNguoiDuocGiao());
        values.put("MaNguoiTao", congViec.getMaNguoiTao());
        values.put("DanhMuc", congViec.getDanhMuc());
        values.put("TieuDe", congViec.getTieuDe());
        values.put("MoTa", congViec.getMoTa());
        values.put("NgayBatDau", congViec.getNgayBatDau());
        values.put("NgayKetThuc", congViec.getNgayKetThuc());
        values.put("TrangThai", congViec.getTrangThai());
        values.put("MucDoUuTien", congViec.getMucDoUuTien());
        return database.insert("CongViec", null, values);
    }

    public int xoaThanhVienDuAn(int maDuAn, int maNguoiDung) {
        return database.delete("ThanhVienDuAn", "MaDuAn=? AND MaNguoiDung=?", new String[]{String.valueOf(maDuAn), String.valueOf(maNguoiDung)});
    }

    public int xoaNguoiDung(int maNguoiDung) {
        return database.delete("NguoiDung", "MaNguoiDung=?", new String[]{String.valueOf(maNguoiDung)});
    }

    public NguoiDung layNguoiDungTheoMa(int maNguoiDung) {
        Cursor cursor = database.query("NguoiDung", null, "MaNguoiDung=?", new String[]{String.valueOf(maNguoiDung)}, null, null, null);
        NguoiDung nguoiDung = null;
        if (cursor != null && cursor.moveToFirst()) {
            nguoiDung = new NguoiDung(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
            cursor.close();
        }
        return nguoiDung;
    }

    public int capNhatNguoiDung(int maNguoiDung, String tenNguoiDung, String email, String matKhau, String loaiTaiKhoan) {
        ContentValues values = new ContentValues();
        values.put("TenNguoiDung", tenNguoiDung);
        values.put("Email", email);
        values.put("MatKhau", matKhau);
        values.put("LoaiTaiKhoan", loaiTaiKhoan);
        return database.update("NguoiDung", values, "MaNguoiDung=?", new String[]{String.valueOf(maNguoiDung)});
    }
}
