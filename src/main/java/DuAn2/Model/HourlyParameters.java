package DuAn2.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "hourlyparameters")
public class HourlyParameters {
	@Id
	private Integer id;
	private Integer manyHoursFirst;
	private Integer hoursTurnToDays;

	public Integer getMaThongSo() {
		return id;
	}

	public void setMaThongSo(Integer maThongSo) {
		this.id = maThongSo;
	}

	public Integer getBaoNhieuGioDau() {
		return manyHoursFirst;
	}

	public void setBaoNhieuGioDau(Integer baoNhieuGioDau) {
		this.manyHoursFirst = baoNhieuGioDau;
	}

	public Integer getSoGioChuyenThanhNgay() {
		return hoursTurnToDays;
	}

	public void setSoGioChuyenThanhNgay(Integer soGioChuyenThanhNgay) {
		this.hoursTurnToDays = soGioChuyenThanhNgay;
	}

}
