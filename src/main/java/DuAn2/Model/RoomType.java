package DuAn2.Model;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "roomtype")
public class RoomType {
	@Id
	private int maLoaiPhong;
	@NotBlank(message = "- Room type cannot be left empty")
	private String tenLoaiPhong;
	@NotBlank(message = "- Description cannot be left blank")
	private String moTa;

	@OneToMany(mappedBy = "loaiPhong", fetch = FetchType.LAZY)

	private Collection<Room> phongs;

	public int getMaLoaiPhong() {
		return maLoaiPhong;
	}

	public void setMaLoaiPhong(int maLoaiPhong) {
		this.maLoaiPhong = maLoaiPhong;
	}

	public String getTenLoaiPhong() {
		return tenLoaiPhong;
	}

	public void setTenLoaiPhong(String tenLoaiPhong) {
		this.tenLoaiPhong = tenLoaiPhong;
	}

	public String getMoTa() {
		return moTa;
	}

	public void setMoTa(String moTa) {
		this.moTa = moTa;
	}

	public Collection<Room> getPhongs() {
		return phongs;
	}

	public void setPhongs(Collection<Room> phongs) {
		this.phongs = phongs;
	}
}
