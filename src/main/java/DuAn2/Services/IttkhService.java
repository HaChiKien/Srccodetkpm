package DuAn2.Services;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import DuAn2.Model.Checkin;

public interface IttkhService extends CrudRepository<Checkin, Integer>{

	@Query("SELECT dp from Checkin dp WHERE dp.hoTen = ?1 or dp.soDT = ?1 or dp.soCMND = ?1 order by dp.maDatPhong desc")
	public List<Checkin> timttkh(String id, PageRequest pageRequest);
	
	@Query("SELECT count(0) from Checkin dp WHERE dp.hoTen = ?1 or dp.soDT = ?1 or dp.soCMND = ?1")
	public Double countTimttkh(String id);
	
	@Query("SELECT dp from Checkin dp order by dp.maDatPhong desc")
	public List<Checkin> findAllOrderBy(PageRequest pageRequest);
	
	@Query("SELECT count(0) from Checkin dp")
	public Double countfindAll();

	public List<Checkin> findAllByRoomMaPhongOrderByNgayDatDesc(int maPhong);

}
