package DuAn2.Services;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import DuAn2.Model.Account;

public interface ITaikhoanServices extends CrudRepository<Account, String> {
	
	@Query("select tk from Account tk")
	public List<Account> findAllTk(PageRequest pageRequest);
	
	@Query("select count(0) from Account tk")
	public Double countFindAllTk();

	@Query("select tk from Account tk where tk.username = ?1 or tk.fullName = ?1 or tk.identityCard = ?1 or tk.phoneNumber = ?1 or tk.email = ?1")
	public List<Account> ListFindtdnOrName(String data, PageRequest pageRequest);
	
	@Query("select count(0) from Account tk where tk.username = ?1 or tk.fullName = ?1 or tk.identityCard = ?1 or tk.phoneNumber = ?1 or tk.email = ?1")
	public Double countListFindtdnOrName(String data);
	
	@Query("select tk from Account tk where tk.username = ?1 and tk.password = ?2")
	public List<Account> findUser(String tendangnhap,String matkhau);
}
