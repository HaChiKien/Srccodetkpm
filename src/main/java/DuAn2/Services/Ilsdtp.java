package DuAn2.Services;

import java.sql.Date;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import DuAn2.Model.Checkin;


public interface Ilsdtp extends CrudRepository<Checkin, Integer>{
	
	@Query(value = "SELECT dp FROM Checkin dp ORDER BY dp.maDatPhong DESC")
	public List<Checkin> findAllOrderByMaDesc(PageRequest pageRequest);
	
	@Query(value = "SELECT count(0) FROM Checkin dp")
	public Double countFindAll();

	@Query("SELECT dp FROM Checkin dp WHERE dp.ngayDat BETWEEN ?1 and ?2")
	public List<Checkin> timlsdtp(Date tungay, Date denngay);
	
	@Query("SELECT dp FROM Checkin dp where (dp.room.trangThai=1 or dp.room.trangThai=2) and dp.maDatPhong not in (select tp.datPhong.maDatPhong from Checkout tp) ORDER BY dp.room.tang,dp.room.soPhong ASC")
	public List<Checkin> timtrangthai();
	
	@Query("SELECT p FROM Checkin p WHERE p.room.maPhong = ?1")
	public List<Checkin> timphongchuatra(int maPhong);
	
	@Query("SELECT p FROM Checkin p WHERE p.maDatPhong = ?1 or p.hoTen=?2 or p.soDT=?2 or p.soCMND = ?2 or p.room.soPhong = ?2 or p.ngayDat=?3 or p.tenDangNhap=?2")
	public List<Checkin> timlsdtp(int maPhong, String data, java.util.Date date, PageRequest pageRequest);
	
	@Query("SELECT count(0) FROM Checkin p WHERE p.maDatPhong = ?1 or p.hoTen=?2 or p.soDT=?2 or p.soCMND = ?2 or p.room.soPhong = ?2 or p.ngayDat=?3 or p.tenDangNhap=?2")
	public Double countTimlsdtp(int maPhong,String data,java.util.Date date);
	
	@Query("SELECT p.room FROM Checkin p WHERE p.room.trangThai = 1")
	public List<Checkin> timtang();
	
	@Query(value = "select dp.* from checkin dp where dp.ma_phong = ?1 and dp.loai_dat = 'homestay' AND dp.ma_dat_phong not in (select ma_dat_phong from checkout)", nativeQuery = true)
	public List<Checkin> listHomestayByMaPhong(Integer maPhong);
	
}
