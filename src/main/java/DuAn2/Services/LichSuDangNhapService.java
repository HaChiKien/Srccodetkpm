package DuAn2.Services;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import DuAn2.Model.HistoryLogin;

public interface LichSuDangNhapService extends CrudRepository<HistoryLogin, Integer> {

	@Query("select d from HistoryLogin d where d.accountLogin = ?1 ORDER BY loginDate desc, loginTime desc")
	public List<HistoryLogin> timlsdn(String data, PageRequest pageRequest);
	
	@Query("select count(0) from HistoryLogin d where d.accountLogin = ?1")
	public Double countTimlsdn(String data);
	
	@Query("select d from HistoryLogin d ORDER BY loginDate desc, loginTime desc")
	public List<HistoryLogin> findAllOrderBy(PageRequest pageRequest);
	
	@Query("select count(0) from HistoryLogin d")
	public Double countFindAll();
}
