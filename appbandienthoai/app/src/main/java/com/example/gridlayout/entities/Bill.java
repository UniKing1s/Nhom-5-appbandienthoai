package com.example.gridlayout.entities;

import java.util.List;

public class Bill {
    private String username;
    private String tenNguoiNhan;
    private String phoneNumber;
    private String diaChi;
    private double tongTien;
    private String loaiThanhToan;
    private boolean tinhtrang;
    private List<CartItem> chiTietHoaDon;

    public Bill(String username, String tenNguoiNhan, String phoneNumber, String diaChi, double tongTien, String loaiThanhToan, boolean tinhtrang, List<CartItem> chiTietHoaDon) {
        this.username = username;
        this.tenNguoiNhan = tenNguoiNhan;
        this.phoneNumber = phoneNumber;
        this.diaChi = diaChi;
        this.tongTien = tongTien;
        this.loaiThanhToan = loaiThanhToan;
        this.tinhtrang = tinhtrang;
        this.chiTietHoaDon = chiTietHoaDon;
    }

    public Bill() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTenNguoiNhan() {
        return tenNguoiNhan;
    }

    public void setTenNguoiNhan(String tenNguoiNhan) {
        this.tenNguoiNhan = tenNguoiNhan;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public double getTongTien() {
        return tongTien;
    }

    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
    }

    public String getLoaiThanhToan() {
        return loaiThanhToan;
    }

    public void setLoaiThanhToan(String loaiThanhToan) {
        this.loaiThanhToan = loaiThanhToan;
    }

    public boolean isTinhtrang() {
        return tinhtrang;
    }

    public void setTinhtrang(boolean tinhtrang) {
        this.tinhtrang = tinhtrang;
    }

    public List<CartItem> getChiTietHoaDon() {
        return chiTietHoaDon;
    }

    public void setChiTietHoaDon(List<CartItem> chiTietHoaDon) {
        this.chiTietHoaDon = chiTietHoaDon;
    }
}
