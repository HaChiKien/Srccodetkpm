package DuAn2.Model;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "service")
public class Service {
	@Id
	private int maDichVu;
	@NotBlank(message = "- Service name cannot be left blank")
	private String tenDichVu;
	private Integer loaiDichVu;
	@NotNull(message = "- Prices cannot be left blank")
	private Double giaDichVu;

	@OneToMany(mappedBy = "dichVu", fetch = FetchType.LAZY)

	private Collection<ServiceMenu> donDichVus;

	public int getMaDichVu() {
		return maDichVu;
	}

	public void setMaDichVu(int maDichVu) {
		this.maDichVu = maDichVu;
	}

	public String getTenDichVu() {
		return tenDichVu;
	}

	public void setTenDichVu(String tenDichVu) {
		this.tenDichVu = tenDichVu;
	}

	public Integer getLoaiDichVu() {
		return loaiDichVu;
	}

	public void setLoaiDichVu(Integer loaiDichVu) {
		this.loaiDichVu = loaiDichVu;
	}

	public Double getGiaDichVu() {
		return giaDichVu;
	}

	public void setGiaDichVu(Double giaDichVu) {
		this.giaDichVu = giaDichVu;
	}

	public Collection<ServiceMenu> getDonDichVus() {
		return donDichVus;
	}

	public void setDonDichVus(Collection<ServiceMenu> donDichVus) {
		this.donDichVus = donDichVus;
	}

}