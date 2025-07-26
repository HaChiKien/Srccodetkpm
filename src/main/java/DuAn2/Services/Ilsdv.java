package DuAn2.Services;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;


import DuAn2.Model.ServiceMenu;

public interface Ilsdv extends CrudRepository<ServiceMenu, Integer> {
	
	@Query("SELECT ddv FROM ServiceMenu ddv ORDER BY ddv.maDonDichVu DESC")
	public List<ServiceMenu> findAllOrderByMaDesc(PageRequest pageRequest);
	
	@Query("SELECT count(0) FROM ServiceMenu ddv")
	public Double countFindAll();

	@Query("SELECT ddv FROM ServiceMenu ddv WHERE ddv.maDonDichVu = ?1 OR ddv.datPhong.maDatPhong = ?2 OR ddv.dichVu.tenDichVu = ?3 or ddv.dichVu.loaiDichVu = ?3 or ddv.ngayDat = ?4 ORDER BY ddv.maDonDichVu DESC")
	public List<ServiceMenu> timlsdv(Integer maddv, Integer madp, String tendv, java.util.Date ngaydat, PageRequest pageRequest);
	
	@Query("SELECT count(0) FROM ServiceMenu ddv WHERE ddv.maDonDichVu = ?1 OR ddv.datPhong.maDatPhong = ?2 OR ddv.dichVu.tenDichVu = ?3 or ddv.dichVu.loaiDichVu = ?3 or ddv.ngayDat = ?4")
	public Double countTimlsdv(Integer maddv,Integer madp, String tendv,java.util.Date ngaydat);
	
	@Query("SELECT ddv FROM ServiceMenu ddv WHERE ddv.ngayDat BETWEEN ?1 and ?2")
	public List<ServiceMenu> timdsvtungaydenngay(java.util.Date tungay, java.util.Date denngay);
}
