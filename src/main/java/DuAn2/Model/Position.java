package DuAn2.Model;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "position")
public class Position {
	@Id
	private int positionId;
	private String positionName;

	@OneToMany(mappedBy = "position", fetch = FetchType.LAZY)
	private Collection<Account> accounts;

	public int getMaChucVu() {
		return positionId;
	}

	public void setMaChucVu(int maChucVu) {
		this.positionId = maChucVu;
	}

	public String getTenChucVu() {
		return positionName;
	}

	public void setTenChucVu(String tenChucVu) {
		this.positionName = tenChucVu;
	}

	public Collection<Account> getTaikhoans() {
		return accounts;
	}

	public void setTaikhoans(Collection<Account> taikhoans) {
		this.accounts = taikhoans;
	}

}