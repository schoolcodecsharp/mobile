package com.example.btl_task_management.model;

import java.io.Serializable;

public class DuAn implements Serializable {
    private int maDuAn;
    private String tenDuAn;
    private String moTa;
    private int maNguoiTao;
    private String ngayTao;

    public DuAn() {
    }

    public DuAn(String tenDuAn, String moTa, int maNguoiTao, String ngayTao) {
        this.tenDuAn = tenDuAn;
        this.moTa = moTa;
        this.maNguoiTao = maNguoiTao;
        this.ngayTao = ngayTao;
    }

    public DuAn(int maDuAn, String tenDuAn, String moTa, int maNguoiTao, String ngayTao) {
        this.maDuAn = maDuAn;
        this.tenDuAn = tenDuAn;
        this.moTa = moTa;
        this.maNguoiTao = maNguoiTao;
        this.ngayTao = ngayTao;
    }

    public int getMaDuAn() {
        return maDuAn;
    }

    public void setMaDuAn(int maDuAn) {
        this.maDuAn = maDuAn;
    }

    public String getTenDuAn() {
        return tenDuAn;
    }

    public void setTenDuAn(String tenDuAn) {
        this.tenDuAn = tenDuAn;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public int getMaNguoiTao() {
        return maNguoiTao;
    }

    public void setMaNguoiTao(int maNguoiTao) {
        this.maNguoiTao = maNguoiTao;
    }

    public String getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(String ngayTao) {
        this.ngayTao = ngayTao;
    }
}
