package DuAn2.Services;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import DuAn2.Model.Room;

public interface Ittp extends CrudRepository<Room, Integer> {

	@Query("SELECT p FROM Room p WHERE p.soPhong = ?1 or p.loaiPhong.tenLoaiPhong = ?2")
	public List<Room> TimPhong(Integer soPhong, String loaiPhong);
	
	@Query("SELECT p FROM Room p ORDER BY tang,soPhong ASC")
	public List<Room> findAllOrderByASC();
	
	@Query("SELECT p FROM Room p ORDER BY p.maPhong DESC")
	public List<Room> findAllOrderByMaDesc(PageRequest pageRequest);
	
	@Query("SELECT count(0) FROM Room p")
	public Integer countFindAll();
	
	@Query("SELECT p FROM Room p where p.trangThai = 1 or p.trangThai = 2 ORDER BY p.tang, p.soPhong ASC")
	public List<Room> findFilterPCKOrderByASC();
	
	@Query("SELECT p FROM Room p where p.trangThai = 0 ORDER BY p.tang, p.soPhong ASC")
	public List<Room> findFilterPTOrderByASC();
	
	@Query("SELECT p FROM Room p where p.countDatLich > 0 and p.countDatLich is not null")
	public List<Room> findFilterPDTOrderByASC();
	
	@Query(value = "SELECT count(0) FROM phong where trang_thai = 1 or trang_thai = 2", nativeQuery = true)
	public Integer countRoomCoKhach();
	
	@Query(value = "SELECT count(0) FROM phong where trang_thai = 0", nativeQuery = true)
	public Integer countRoomTrong();
	
	@Query(value = "SELECT count(0) FROM phong where trang_thai = 2", nativeQuery = true)
	public Integer countPhongDatTruoc();
	
	@Query(value = "select count(0) from checkin dp where dp.ma_phong = ?1 and dp.loai_dat = 'homestay' AND dp.ma_dat_phong not in (select ma_dat_phong from checkout)", nativeQuery = true)
	public Integer countHomestayByMaPhong(Integer maPhong);
}
