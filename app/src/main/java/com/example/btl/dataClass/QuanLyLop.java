package com.example.btl.dataClass;

public class QuanLyLop {
    Long soTinChi;
    String tenLop;
    String maLop;
    public QuanLyLop()
    {

    }
    public QuanLyLop(Long soTinChi, String tenLop, String maLop) {
        this.soTinChi = soTinChi;
        this.tenLop = tenLop;
        this.maLop = maLop;
    }

    public Long getSoTinChi() {
        return soTinChi;
    }

    public void setSoTinChi(Long soTinChi) {
        this.soTinChi = soTinChi;
    }

    public String getTenLop() {
        return tenLop;
    }

    public void setTenLop(String tenLop) {
        this.tenLop = tenLop;
    }

    public String getMaLop() {
        return maLop;
    }
    public Long getSoBuoi()
    {
        return soTinChi*3;
    }

    public void setMaLop(String maLop) {
        this.maLop = maLop;
    }

    @Override
    public String toString() {
        return tenLop;
    }
}
