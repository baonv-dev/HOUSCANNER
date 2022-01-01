package com.example.btl.dataClass;

import java.util.ArrayList;
import java.util.List;

public class AttendanceManually {
    private String maSV,hoTen,lop,ngaySinh;
    private Boolean dadiemdanh = false;
    private List<String> diemDanh;

    public AttendanceManually() {
        diemDanh = new ArrayList<>();
    }

    public AttendanceManually(String maSV, String tenSV, String lop, String ngaySinh, List<String> diemDanh) {
        this.maSV = maSV;
        this.hoTen = tenSV;
        this.lop = lop;
        this.ngaySinh = ngaySinh;
        this.diemDanh = diemDanh;
    }

    public Boolean getDadiemdanh() {
        return dadiemdanh;
    }

    public void setDadiemdanh(Boolean dadiemdanh) {
        this.dadiemdanh = dadiemdanh;
    }

    public String getMaSV() {
        return maSV;
    }

    public void setMaSV(String maSV) {
        this.maSV = maSV;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getLop() {
        return lop;
    }

    public void setLop(String lop) {
        this.lop = lop;
    }

    public String getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(String ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public List<String> getDiemDanh() {
        return diemDanh;
    }

    public void setDiemDanh(List<String> diemDanh) {
        this.diemDanh = diemDanh;
    }

}
