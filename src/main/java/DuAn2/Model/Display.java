package DuAn2.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "display")
public class Display {

	@Id
	private int id;
	private String positionName;
	private String address;
	private String phoneNumber;
	
	
	public String getDiaChi() {
		return address;
	}
	public void setDiaChi(String diaChi) {
		this.address = diaChi;
	}
	public String getSoDienThoai() {
		return phoneNumber;
	}
	public void setSoDienThoai(String soDienThoai) {
		this.phoneNumber = soDienThoai;
	}
	public int getMaGiaoDien() {
		return id;
	}
	public void setMaGiaoDien(int maGiaoDien) {
		this.id = maGiaoDien;
	}
	public String getTenToChuc() {
		return positionName;
	}
	public void setTenToChuc(String tenToChuc) {
		this.positionName = tenToChuc;
	}
	
	
}
