package DuAn2.Controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import DuAn2.Model.CheckinCalendar;
import DuAn2.Model.Room;
import DuAn2.Services.LichDatPhongService;
import DuAn2.Services.Ittp;

@Controller
@Transactional
public class LichDatPhongController {

	@Autowired
	LichDatPhongService lichDatPhongService;
	
	@Autowired
	Ittp phongService;
	
	@ModelAttribute(name = "changeURL")
	public String changeURL() {
		return "dslichdatphong";
	}

	@ModelAttribute(name = "activedptp")
	public String activedptp() {
		return "active";
	}
	
	@GetMapping("dslichdatphong")
	public String dslichdatphong(ModelMap model, @RequestParam("maPhong")Integer maPhong, @RequestParam("soPhong")Integer soPhong) {
		List<CheckinCalendar> danhsach = lichDatPhongService.listDatPhongByMaPhong(maPhong);
		
		 
		model.addAttribute("titlepage", "Book a room number " + soPhong);
		model.addAttribute("maPhong", maPhong);
		model.addAttribute("soPhong", soPhong);
		model.addAttribute("danhsach", danhsach);
		return "dslichdatphong/dslichdatphong";
	}
	
	@GetMapping("themlich")
	public String themlich(ModelMap model, HttpServletRequest httpServletRequest, @RequestParam("maPhong")Integer maPhong, @RequestParam("soPhong")Integer soPhong) {
		CheckinCalendar lichDatPhong = new CheckinCalendar();
		HttpSession session = httpServletRequest.getSession();
		String nguoiDung = session.getAttribute("nguoidung").toString();
		
		model.addAttribute("titlepage", "Add a room number calendar " + soPhong);
		model.addAttribute("nguoidung", nguoiDung);
		model.addAttribute("maPhong", maPhong);
		model.addAttribute("soPhong", soPhong);
		model.addAttribute("lichDatPhong", lichDatPhong);
		return "dslichdatphong/themlich";
	}
	
	@PostMapping("/actionthemlich")
	public String actionthemlich(ModelMap model, @ModelAttribute("lichDatPhong") CheckinCalendar lichDatPhong, @RequestParam("maPhong")Integer maPhong, @RequestParam("soPhong")Integer soPhong) {
		lichDatPhongService.save(lichDatPhong);
		updateCountDatLich(maPhong);
		model.addAttribute("message", "More success.");
		return dslichdatphong(model, maPhong, soPhong);
	}
	
	@GetMapping("/actionxoalich")
	public String actionthemlich(ModelMap model, @RequestParam("maPhong")Integer maPhong, @RequestParam("soPhong")Integer soPhong, @RequestParam("maLich")Integer maLich) {
		lichDatPhongService.deleteById(maLich);
		updateCountDatLich(maPhong);
		
		model.addAttribute("message", "Deleted successfully.");
		return dslichdatphong(model, maPhong, soPhong);
	}
	
	@GetMapping("/actionxoalichds")
	public String actionxoalichds(ModelMap model, @RequestParam("maPhong")Integer maPhong, @RequestParam("maLich")Integer maLich) {
		lichDatPhongService.deleteById(maLich);
		updateCountDatLich(maPhong);
		
		model.addAttribute("message", "Deleted successfully.");
		List<CheckinCalendar> danhsach = lichDatPhongService.tongdsdatlich();
		model.addAttribute("titlepage", "List of reservations");
		model.addAttribute("danhsach", danhsach);
		return "dslichdatphong/tongdslichdatphong";
	}
	
	public void updateCountDatLich(Integer maPhong) {
		Integer countDatLich = lichDatPhongService.countDatLich(maPhong);
		Room phong = phongService.findById(maPhong).get();
		phong.setCountDatLich(countDatLich);
		phongService.save(phong);
	}
}
