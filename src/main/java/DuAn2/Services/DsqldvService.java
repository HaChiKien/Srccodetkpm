package DuAn2.Services;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import DuAn2.Model.Service;


public interface DsqldvService extends CrudRepository<Service, Integer> {
	
	@Query("select d from Service d where d.maDichVu = ?1 or d.tenDichVu = ?2")
	public List<Service> timldsdv(int data1, String data2);
	
	@Query("select d from Service d where d.loaiDichVu = 0")
	public List<Service> selectanuong();

	@Query("select d from Service d where d.loaiDichVu = 1")
	public List<Service> selectgiatui();
	
	@Query("select d from Service d where d.loaiDichVu = 2")
	public List<Service> selectthugian();
	
	@Query("select d from Service d where d.loaiDichVu != 3 ORDER BY d.maDichVu DESC")
	public List<Service> findAllNotType3();
	
	@Query(value = "select d.ma_dich_vu from service d order by d.ma_dich_vu desc limit 1", nativeQuery = true)
	public Integer getLastIdDv();
}
