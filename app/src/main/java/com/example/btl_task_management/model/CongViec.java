package com.example.btl_task_management.model;

import java.io.Serializable;

public class CongViec implements Serializable {
    private int maCongViec;
    private int maNguoiDung;
    private int maDuAn;
    private int maNguoiDuocGiao;
    private int maNguoiTao;
    private String danhMuc;
    private String tieuDe;
    private String moTa;
    private String ngayBatDau;
    private String ngayKetThuc;
    private String trangThai;
    private String mucDoUuTien;

    public CongViec() {
    }

    public CongViec(int maNguoiDung, String tieuDe, String moTa, String ngayBatDau, String ngayKetThuc, String trangThai, String mucDoUuTien) {
        this.maNguoiDung = maNguoiDung;
        this.tieuDe = tieuDe;
        this.moTa = moTa;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
        this.trangThai = trangThai;
        this.mucDoUuTien = mucDoUuTien;
    }

    public CongViec(int maCongViec, int maNguoiDung, String tieuDe, String moTa, String ngayBatDau, String ngayKetThuc, String trangThai, String mucDoUuTien) {
        this.maCongViec = maCongViec;
        this.maNguoiDung = maNguoiDung;
        this.tieuDe = tieuDe;
        this.moTa = moTa;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
        this.trangThai = trangThai;
        this.mucDoUuTien = mucDoUuTien;
    }

    public int getMaCongViec() {
        return maCongViec;
    }

    public void setMaCongViec(int maCongViec) {
        this.maCongViec = maCongViec;
    }

    public int getMaNguoiDung() {
        return maNguoiDung;
    }

    public void setMaNguoiDung(int maNguoiDung) {
        this.maNguoiDung = maNguoiDung;
    }

    public int getMaDuAn() {
        return maDuAn;
    }

    public void setMaDuAn(int maDuAn) {
        this.maDuAn = maDuAn;
    }

    public int getMaNguoiDuocGiao() {
        return maNguoiDuocGiao;
    }

    public void setMaNguoiDuocGiao(int maNguoiDuocGiao) {
        this.maNguoiDuocGiao = maNguoiDuocGiao;
    }

    public int getMaNguoiTao() {
        return maNguoiTao;
    }

    public void setMaNguoiTao(int maNguoiTao) {
        this.maNguoiTao = maNguoiTao;
    }

    public String getDanhMuc() {
        return danhMuc;
    }

    public void setDanhMuc(String danhMuc) {
        this.danhMuc = danhMuc;
    }

    public String getTieuDe() {
        return tieuDe;
    }

    public void setTieuDe(String tieuDe) {
        this.tieuDe = tieuDe;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getNgayBatDau() {
        return ngayBatDau;
    }

    public void setNgayBatDau(String ngayBatDau) {
        this.ngayBatDau = ngayBatDau;
    }

    public String getNgayKetThuc() {
        return ngayKetThuc;
    }

    public void setNgayKetThuc(String ngayKetThuc) {
        this.ngayKetThuc = ngayKetThuc;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getMucDoUuTien() {
        return mucDoUuTien;
    }

    public void setMucDoUuTien(String mucDoUuTien) {
        this.mucDoUuTien = mucDoUuTien;
    }
}
