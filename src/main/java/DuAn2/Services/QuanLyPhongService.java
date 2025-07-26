package DuAn2.Services;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import DuAn2.Model.Room;




public interface QuanLyPhongService extends CrudRepository<Room, Integer> {
	@Query("SELECT p FROM Room p WHERE p.soPhong = ?1 or p.tang = ?1")
	public List<Room> TimMaPhong(int id, PageRequest pageRequest);
	
	@Query("SELECT p FROM Room p WHERE p.soPhong = ?1 or p.tang = ?1")
	public List<Room> TimMaPhong(int id);
	
	@Query("SELECT count(0) FROM Room p WHERE p.soPhong = ?1 or p.tang = ?1")
	public Double countTimMaPhong(int id);
	
	@Query("SELECT p FROM Room p ORDER BY p.maPhong DESC")
	public List<Room> findAllOrderByMaDesc();

	public List<Room> findAll();

	public List<Room> findAllByGiaPhongLessThanAndLoaiPhongTenLoaiPhong(double maxPrice, String typeRoom);

	@Query("select p from Room p where p.trangThai = 1")
	public List<Room> findAllByTrangThai();

	public List<Room> findAllByLoaiPhongTenLoaiPhong(String roomType);

	@Query("select p from Room p where p.trangThai = 1 and p.maPhong = ?1")
	public List<Room> findAllByTrangThaiAndMaPhong(int maPhong);

	public Room getByMaPhong(int maPhong);

	public Room getByGiaPhong(double giaPhong);

}
