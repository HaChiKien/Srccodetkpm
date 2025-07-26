package DuAn2.Services;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import DuAn2.Model.Checkout;

public interface ITraPhong extends CrudRepository<Checkout, Integer> {

	@Query("SELECT tp FROM Checkout tp WHERE tp.ngayTra BETWEEN ?1 and ?2")
	public List<Checkout> timtkdt(Date tungay, Date denngay);

	public List<Checkout> findAll();

	public Checkout getByDatPhongMaDatPhong(int maDatPhong);
}
