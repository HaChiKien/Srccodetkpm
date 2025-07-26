package DuAn2.Model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name="historylogin")
public class HistoryLogin {

	@Id
	private int id;
	@Column(name="login_date")
	private Date loginDate;
	@Column(name="login_time")
	@DateTimeFormat(pattern="HH:mm")
	private java.util.Date loginTime;
	@Column(name="account_login")
	private String accountLogin;
	public int getMaLichSuDangNhap() {
		return id;
	}
	public void setMaLichSuDangNhap(int maLichSuDangNhap) {
		this.id = maLichSuDangNhap;
	}
	public Date getNgayDangNhap() {
		return loginDate;
	}
	public void setNgayDangNhap(Date ngayDangNhap) {
		this.loginDate = ngayDangNhap;
	}
	public java.util.Date getGioDangNhap() {
		return loginTime;
	}
	public void setGioDangNhap(java.util.Date gioDangNhap) {
		this.loginTime = gioDangNhap;
	}
	public String getTaiKhoanDangNhap() {
		return accountLogin;
	}
	public void setTaiKhoanDangNhap(String taiKhoanDangNhap) {
		this.accountLogin = taiKhoanDangNhap;
	}
	
	
}
