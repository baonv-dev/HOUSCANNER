package com.example.btl.dataClass;

import java.util.ArrayList;

public class QuanLySinhVienLop {
    private String hoTen,lop,maSV,ngaySinh;
    private ArrayList<String> diemDanh = new ArrayList<String>();
    public QuanLySinhVienLop() { }
    public QuanLySinhVienLop(String maSV,String hoTen, String lop,  String ngaySinh) {
        this.hoTen = hoTen;
        this.lop = lop;
        this.maSV = maSV;
        this.ngaySinh = ngaySinh;
    }
    public String getHoTen() {
        return hoTen;
    }
    public String getLop() {
        return lop;
    }
    public String getMaSV() {
        return maSV;
    }
    public String getNgaySinh() {
        return ngaySinh;
    }
    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }
    public void setLop(String lop) {
        this.lop = lop;
    }
    public void setMaSV(String maSV) {
        this.maSV = maSV;
    }
    public void setNgaySinh(String ngaySinh) {
        this.ngaySinh = ngaySinh;
    }
}
