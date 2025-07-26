package DuAn2.Services;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import DuAn2.Model.RoomType;



public interface ILoaiphongSercives extends CrudRepository<RoomType, String> {
	@Query("select lp from RoomType lp where lp.maLoaiPhong = ?1 or lp.tenLoaiPhong = ?2")
	public List<RoomType> ListFindtdnOrName(Integer maLoaiPhong, String tenLoaiPhong);
}
