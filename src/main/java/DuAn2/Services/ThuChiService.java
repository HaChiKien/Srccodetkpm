package DuAn2.Services;

import java.sql.Date;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import DuAn2.Model.Collect;

public interface ThuChiService extends CrudRepository<Collect, Integer>{

	@Query("SELECT tc FROM Collect tc order by tc.collectDate desc, tc.collectTime desc")
	public List<Collect> getThuChi(PageRequest pageRequest);
	
	@Query("SELECT tc FROM Collect tc order by tc.collectDate desc, tc.collectTime desc")
	public List<Collect> getThuChi();
	
	@Query("SELECT sum(tc.money) FROM Collect tc where tc.collectType = 1")
	public Double getTongChi();
	
	@Query("SELECT sum(tc.money) FROM Collect tc where tc.collectType = 0")
	public Double getTongThu();
	
	@Query("SELECT count(0) FROM Collect tc")
	public Double countThuChi();
	
	@Query("SELECT tc FROM Collect tc where tc.collectType = 0 order by tc.collectDate desc, tc.collectTime desc")
	public List<Collect> getThu();
	
	@Query("SELECT tc FROM Collect tc where tc.collectType = 1 order by tc.collectDate desc, tc.collectTime desc")
	public List<Collect> getChi();
	
	@Query("SELECT tc FROM Collect tc where tc.collectDate between ?1 and ?2 order by tc.collectDate desc, tc.collectTime desc")
	public List<Collect> getTimThuChi(Date tuNgay, Date denNgay, PageRequest pageRequest);
	
	@Query("SELECT tc FROM Collect tc where tc.collectDate between ?1 and ?2 order by tc.collectDate desc, tc.collectTime desc")
	public List<Collect> getTimThuChi(Date tuNgay, Date denNgay);
	
	@Query("SELECT sum(tc.money) FROM Collect tc where tc.collectType = 1 and tc.collectDate between ?1 and ?2 order by tc.collectDate desc, tc.collectTime desc")
	public Double getTongChiTim(Date tuNgay, Date denNgay);
	
	@Query("SELECT sum(tc.money) FROM Collect tc where tc.collectType = 0 and tc.collectDate between ?1 and ?2 order by tc.collectDate desc, tc.collectTime desc")
	public Double getTongThuTim(Date tuNgay, Date denNgay);
	
	@Query("SELECT count(0) FROM Collect tc where tc.collectDate between ?1 and ?2")
	public Double countThuChiTim(Date tuNgay, Date denNgay);
}
