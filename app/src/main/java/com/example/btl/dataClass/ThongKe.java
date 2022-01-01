package com.example.btl.dataClass;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ThongKe{
    private String hoTen,lop,maSV,ngaySinh;
    private ArrayList<String> diemDanh = new ArrayList<>();
    public ThongKe() { }
    public ThongKe(String maSV,String hoTen, String lop,  String ngaySinh,ArrayList<String> ngaydiemdanh) {
        this.hoTen = hoTen;
        this.lop = lop;
        this.maSV = maSV;
        this.ngaySinh = ngaySinh;
        this.diemDanh = ngaydiemdanh;
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
    public ArrayList<String> getDiemDanh() {
        diemDanh.removeAll(Collections.singleton(null));
        return diemDanh;
    }
    public void setDiemDanh(ArrayList<String> diemDanh) {
        this.diemDanh = diemDanh;
    }
}
