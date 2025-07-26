package DuAn2.Model;

import java.sql.Date;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "checkin")

public class Checkin {

    @Id
    private int maDatPhong;
    private String hoTen;
    @Column(name = "sodt")
    private String soDT;
    @Column(name = "email")
    private String email;
    @Column(name = "socmnd")
    private String soCMND;
    private Double tienCoc;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maPhong")
    private Room room;
    private Date ngayDat;
    @DateTimeFormat(pattern = "HH:mm")
    private java.util.Date gioDat;
    private String loaiDat;
    private String tenDangNhap;


    @OneToMany(mappedBy = "datPhong", fetch = FetchType.LAZY)
    private Collection<ServiceMenu> donDichVus;

    @OneToMany(mappedBy = "datPhong", fetch = FetchType.LAZY)
    private Collection<Checkout> traPhongs;

    public Checkin(int maDatPhong, String hoTen, String soDT, String email, Room phong, Date ngayDat) {
        this.maDatPhong = maDatPhong;
        this.hoTen = hoTen;
        this.soDT = soDT;
        this.email = email;
        this.room = phong;
        this.ngayDat = ngayDat;
    }

    public Checkin() {
    }

    public int getMaDatPhong() {
        return maDatPhong;
    }

    public void setMaDatPhong(int maDatPhong) {
        this.maDatPhong = maDatPhong;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getSoDT() {
        return soDT;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLoaiDat() {
        return loaiDat;
    }

    public void setLoaiDat(String loaiDat) {
        this.loaiDat = loaiDat;
    }

    public void setSoDT(String soDT) {
        this.soDT = soDT;
    }

    public String getSoCMND() {
        return soCMND;
    }

    public void setSoCMND(String soCMND) {
        this.soCMND = soCMND;
    }

    public Double getTienCoc() {
        return tienCoc;
    }

    public void setTienCoc(Double tienCoc) {
        this.tienCoc = tienCoc;
    }

    public Room getPhong() {
        return room;
    }

    public void setPhong(Room room) {
        this.room = room;
    }

    public Date getNgayDat() {
        return ngayDat;
    }

    public void setNgayDat(Date ngayDat) {
        this.ngayDat = ngayDat;
    }

    public java.util.Date getGioDat() {
        return gioDat;
    }

    public void setGioDat(java.util.Date gioDat) {
        this.gioDat = gioDat;
    }

    public String getTenDangNhap() {
        return tenDangNhap;
    }

    public void setTenDangNhap(String tenDangNhap) {
        this.tenDangNhap = tenDangNhap;
    }

    public Collection<ServiceMenu> getDonDichVus() {
        return donDichVus;
    }

    public void setDonDichVus(Collection<ServiceMenu> donDichVus) {
        this.donDichVus = donDichVus;
    }

    public Collection<Checkout> getTraPhongs() {
        return traPhongs;
    }

    public void setTraPhongs(Collection<Checkout> traPhongs) {
        this.traPhongs = traPhongs;
    }





}
