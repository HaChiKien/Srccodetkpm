package DuAn2.Services;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import DuAn2.Model.ServiceMenu;

public interface Idv extends CrudRepository<ServiceMenu, Integer>{

	@Query("SELECT ddv FROM ServiceMenu ddv WHERE ddv.datPhong.maDatPhong = ?1")
	public List<ServiceMenu> datdichvu(int madatphong);
}
