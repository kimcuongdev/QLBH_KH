package com.project.qlbh_kh.entity;

public class MatHang {
    private String tenMatHang;
    private int soLuong;
    private String donViTinh;
    private double giaNhapVao;
    private double giaBanRa;

    public MatHang(String tenMatHang, int soLuong) {
        this.tenMatHang = tenMatHang;
        this.soLuong = soLuong;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public String getTenMatHang() {
        return tenMatHang;
    }
}
