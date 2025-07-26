package DuAn2.Model;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "collect")
public class Collect {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String content;
	private Integer collectType;
	private Date collectDate;
	@DateTimeFormat(pattern = "HH:mm")
	private java.util.Date collectTime;
	private Double money;
	private String username;

	
	public Integer getLoaiThuChi() {
		return collectType;
	}

	public void setLoaiThuChi(Integer loaiThuChi) {
		this.collectType = loaiThuChi;
	}

	public Double getSoTien() {
		return money;
	}

	public void setSoTien(Double soTien) {
		this.money = soTien;
	}

	public Integer getMaThuChi() {
		return id;
	}

	public void setMaThuChi(Integer maThuChi) {
		this.id = maThuChi;
	}

	public String getNoiDungChi() {
		return content;
	}

	public void setNoiDungChi(String noiDungChi) {
		this.content = noiDungChi;
	}

	public Date getNgayChi() {
		return collectDate;
	}

	public void setNgayChi(Date ngayChi) {
		this.collectDate = ngayChi;
	}

	public java.util.Date getGioChi() {
		return collectTime;
	}

	public void setGioChi(java.util.Date gioChi) {
		this.collectTime = gioChi;
	}

	public String getTenDangNhap() {
		return username;
	}

	public void setTenDangNhap(String tenDangNhap) {
		this.username = tenDangNhap;
	}
}
