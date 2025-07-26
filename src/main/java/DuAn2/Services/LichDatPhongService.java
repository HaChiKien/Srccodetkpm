package DuAn2.Services;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import DuAn2.Model.CheckinCalendar;

public interface LichDatPhongService extends CrudRepository<CheckinCalendar, Integer> {

	@Query("select l from CheckinCalendar l where l.room.maPhong = ?1")
	List<CheckinCalendar> listDatPhongByMaPhong(Integer maPhong);
	
	@Query("select l from CheckinCalendar l order by l.ngayDat asc, l.gioDat asc")
	List<CheckinCalendar> tongdsdatlich();
	
	@Query("select count(0) from CheckinCalendar l where l.room.maPhong = ?1")
	Integer countDatLich(Integer maPhong);

	
	@Query(value = "select l.ma_phong from checkincalendar l group by l.ma_phong", nativeQuery = true)
	List<Integer> listLichDatPhongCount();

	@Query("SELECT count(0) from CheckinCalendar l")
	public Double countfindAll();

	public List<CheckinCalendar> findAllByRoomMaPhongOrderByNgayDatDesc(int maPhong);


}

