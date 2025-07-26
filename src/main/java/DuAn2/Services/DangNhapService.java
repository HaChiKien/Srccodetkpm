package DuAn2.Services;


import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import DuAn2.Model.Account;


public interface DangNhapService extends CrudRepository<Account, String> {

	@Query("SELECT tk FROM Account tk WHERE tk.username = ?1 and tk.password = ?2")
	public List<Account> TimTenDangNhap(String tendangnhap, String matkhau);
}
