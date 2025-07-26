package DuAn2.Controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import DuAn2.Common.Common;
import DuAn2.Model.Account;
import DuAn2.Services.ITaikhoanServices;

@Controller
@Transactional
public class TaikhoanController {
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	    binder.registerCustomEditor(Date.class,  new CustomDateEditor(dateFormat, true));
	}
	
	@Autowired
	ITaikhoanServices iTaikhoanServices;
	
	@ModelAttribute(name = "changeURL")
	public String changeURL() {
		return "qltk";
	}
	
	@RequestMapping("/qltk")
	public String listtk(ModelMap model, @ModelAttribute("taikhoan") Account taikhoan) {
		PageRequest pageable = PageRequest.of(0, 10);
		List<Account> ltk = (List<Account>) iTaikhoanServices.findAllTk(pageable);// lấy list
		activemenu(model);
		vitrihientai = 1;
		model.addAttribute("titlepage", "List of accounts"); // tiêu đề cho trang
		model.addAttribute("lTaikhoans", ltk); 
		model.addAttribute("listSoLuongTrang", listSoLuongTrang(iTaikhoanServices.countFindAllTk(), model));// số lượng các button chọn trang
		return "qltk"; // Tên trang index
	}

	@RequestMapping("/edittk")
	public String edittk(ModelMap model, @Validated @ModelAttribute("taikhoan") Account taikhoan,
			BindingResult errors) throws InterruptedException {
		activemenu(model);
		if (errors.hasErrors()) {
			model.addAttribute("errors", errors.getAllErrors());
			return listtk(model, taikhoan);
		} else {
			taikhoan.setMatKhau(Common.encode(taikhoan.getMatKhau()));
			Date today = new Date();
			taikhoan.setNgayTao(today);
			taikhoan.setGioTao(today);
			iTaikhoanServices.save(taikhoan);// nếu trùng id thì không thêm mà thành sửa.
			taikhoan = new Account();
			model.addAttribute("taikhoan", taikhoan);
			model.addAttribute("message", "Successfully fixed");
			return listtk(model, taikhoan); // sửa xong chạy lại trang hiển thị book
		}
	}
	@RequestMapping("/addtk")
	public String addtk(ModelMap model, @ModelAttribute("taikhoan") Account taikhoan) {
		model.addAttribute("chamshowcd", ".show");
		// show quan ly tai khoan
		model.addAttribute("chamshowqltk", ".show");
		model.addAttribute("activedstk", null);
		model.addAttribute("activettk", "active");

		model.addAttribute("titlepage", "Add a new account");
		return "addtk"; // Tên trang index
	}

	@RequestMapping("/actionaddtk")
	public String actionaddtk(ModelMap model, @Validated @ModelAttribute("taikhoan") Account taikhoan,
			BindingResult errors) {
		model.addAttribute("chamshowcd", ".show");
		// show quan ly tai khoan
		model.addAttribute("chamshowqltk", ".show");
		model.addAttribute("activedstk", null);
		model.addAttribute("activettk", "active");
		String a = null;
		try {
			a = iTaikhoanServices.findById(taikhoan.getTenDangNhap()).get().getTenDangNhap();
		} catch (Exception e) {
			a = "null";
		}
		if (errors.hasErrors()) {
			model.addAttribute("titlepage", "Add a new account");
			model.addAttribute("errors", errors.getAllErrors());
			return "addtk";
		} else if (a.equals(taikhoan.getTenDangNhap())) {
			model.addAttribute("titlepage", "Add a new account");

			model.addAttribute("errortk", "- Username available");
			return "addtk";
		}

		else {
			model.addAttribute("taikhoan", new Account());
			model.addAttribute("message", " More success");
			model.addAttribute("titlepage", "Add a new account");
			taikhoan.setMatKhau(Common.encode(taikhoan.getMatKhau()));
			Date today = new Date();
			taikhoan.setNgayTao(today);
			taikhoan.setGioTao(today);
			iTaikhoanServices.save(taikhoan);// nếu trùng id thì không thêm mà thành sửa.

			return "addtk"; // Thêm xong chạy lại trang hiển thị book
		}

	}

	@RequestMapping("/delete")
	public String delete(ModelMap model, @ModelAttribute("taikhoan") Account taikhoan,
			@RequestParam("tenDangNhap") String tenDangNhap) {
		activemenu(model);
		model.addAttribute("titlepage", "List of accounts");

		taikhoan.setTenDangNhap(tenDangNhap); // Set id vào book

		iTaikhoanServices.delete(taikhoan);// Xóa theo id vì thế set id vào
		
		PageRequest pageable = PageRequest.of(vitrihientai - 1, 10);
		List<Account> l = (List<Account>) iTaikhoanServices.findAllTk(pageable);// lấy list

		if (vitrihientai == 1) {
			model.addAttribute("lTaikhoans", l);
		} else if (l.isEmpty()) {
			pageable = PageRequest.of(vitrihientai - 2, 10);
			l = (List<Account>) iTaikhoanServices.findAllTk(pageable);
			model.addAttribute("lTaikhoans", l);
			vitrihientai--;
		} else {
			model.addAttribute("lTaikhoans", l);
		}

		model.addAttribute("message", "Deleted successfully");
		model.addAttribute("listSoLuongTrang", listSoLuongTrang(iTaikhoanServices.countFindAllTk(), model));

		return listtk(model, taikhoan); // xóa xong chạy lại trang hiển thị book
	}

	@RequestMapping("/timkiemidornametk")
	public String timkiemidornametk(ModelMap model, @ModelAttribute("taikhoan") Account taikhoan,
			HttpServletRequest request) {
		activemenu(model);
		
		PageRequest pageable = PageRequest.of(0, 10);
		String data = request.getParameter("data");
		List<Account> ltim;
		ltim = iTaikhoanServices.ListFindtdnOrName(data, pageable);
		model.addAttribute("data", data);
		
		vitrihientai = 1;
		model.addAttribute("danhsach", 0);
		if (!ltim.isEmpty()) {
			model.addAttribute("danhsachtim", 1);
		}

		model.addAttribute("lTaikhoans", ltim);
		model.addAttribute("listSoLuongTrang", listSoLuongTrangtim(iTaikhoanServices.countListFindtdnOrName(data), model));
		model.addAttribute("titlepage", "List of accounts");

		return "qltk"; // Chuyển sang trang timkiem
	}

	// phân 10 item trên 1 trang. phải có vị trí để tính xuất 10 item thứ bao nhiêu
	int vitrihientai = 1;

	// số lượng button bấm chuyển trang
	public List<Integer> listSoLuongTrang(Double count, ModelMap model) {
		List<Integer> lreturn = new ArrayList<>();
		double temp = Double.parseDouble(count + "") / 10.0;
		int tempfor = (int) Math.ceil(temp);
		int a = 3;
		int b = 3;

		if (vitrihientai == 1) {
			a = 0;
			b = 6;
		}
		if (vitrihientai == 2) {
			a = 1;
			b = 5;
		}
		if (vitrihientai == 3) {
			a = 2;
			b = 4;
		}
		if (vitrihientai == 4) {
			a = 3;
			b = 3;
		}

		if (vitrihientai == tempfor) {
			a = 6;
			b = 0;
		}
		if (vitrihientai == (tempfor - 1)) {
			a = 5;
			b = 1;
		}
		if (vitrihientai == (tempfor - 2)) {
			a = 4;
			b = 2;
		}
		if (vitrihientai == (tempfor - 3)) {
			a = 3;
			b = 3;
		}
		if (vitrihientai == 1 && vitrihientai == tempfor) {
			a = 0;
			b = 0;
		}
		if (vitrihientai == 1 && vitrihientai == tempfor - 1) {
			a = 0;
			b = 1;
		}
		if (vitrihientai == 1 && vitrihientai == tempfor - 2) {
			a = 0;
			b = 2;
		}
		if (vitrihientai == 1 && vitrihientai == tempfor - 3) {
			a = 0;
			b = 3;
		}
		if (vitrihientai == 1 && vitrihientai == tempfor - 4) {
			a = 0;
			b = 4;
		}
		if (vitrihientai == 1 && vitrihientai == tempfor - 5) {
			a = 0;
			b = 5;
		}
		if (vitrihientai == 1 && vitrihientai == tempfor - 6) {
			a = 0;
			b = 6;
		}

		// -------------
		if (vitrihientai == 2 && vitrihientai == tempfor) {
			a = 1;
			b = 0;
		}
		if (vitrihientai == 2 && vitrihientai == tempfor - 1) {
			a = 1;
			b = 1;
		}
		if (vitrihientai == 2 && vitrihientai == tempfor - 2) {
			a = 1;
			b = 2;
		}
		if (vitrihientai == 2 && vitrihientai == tempfor - 3) {
			a = 1;
			b = 3;
		}
		if (vitrihientai == 2 && vitrihientai == tempfor - 4) {
			a = 1;
			b = 4;
		}
		if (vitrihientai == 2 && vitrihientai == tempfor - 5) {
			a = 1;
			b = 5;
		}
		// -------------
		if (vitrihientai == 3 && vitrihientai == tempfor) {
			a = 2;
			b = 0;
		}
		if (vitrihientai == 3 && vitrihientai == tempfor - 1) {
			a = 2;
			b = 1;
		}
		if (vitrihientai == 3 && vitrihientai == tempfor - 2) {
			a = 2;
			b = 2;
		}
		if (vitrihientai == 3 && vitrihientai == tempfor - 3) {
			a = 2;
			b = 3;
		}
		if (vitrihientai == 3 && vitrihientai == tempfor - 4) {
			a = 2;
			b = 4;
		}
		// -------------
		if (vitrihientai == 4 && vitrihientai == tempfor) {
			a = 3;
			b = 0;
		}
		if (vitrihientai == 4 && vitrihientai == tempfor - 1) {
			a = 3;
			b = 1;
		}
		if (vitrihientai == 4 && vitrihientai == tempfor - 2) {
			a = 3;
			b = 2;
		}
		if (vitrihientai == 4 && vitrihientai == tempfor - 3) {
			a = 3;
			b = 3;
		}
		// -------------
		if (vitrihientai == 5 && vitrihientai == tempfor) {
			a = 4;
			b = 0;
		}
		if (vitrihientai == 5 && vitrihientai == tempfor - 1) {
			a = 4;
			b = 1;
		}
		if (vitrihientai == 5 && vitrihientai == tempfor - 2) {
			a = 4;
			b = 2;
		}
		if (vitrihientai == 5 && vitrihientai == tempfor - 3) {
			a = 4;
			b = 3;
		}
		// -------------
		if (vitrihientai == 6 && vitrihientai == tempfor) {
			a = 5;
			b = 0;
		}
		if (vitrihientai == 6 && vitrihientai == tempfor - 1) {
			a = 5;
			b = 1;
		}
		if (vitrihientai == 6 && vitrihientai == tempfor - 2) {
			a = 5;
			b = 2;
		}
		if (vitrihientai == 6 && vitrihientai == tempfor - 3) {
			a = 5;
			b = 3;
		}

		for (int i = vitrihientai - a; i <= vitrihientai + b; i++) {
			lreturn.add(i);
		}
		if (count.intValue() == 0) {
			lreturn.clear();
		}
		model.addAttribute("danhsach", count.intValue()); // để ẩn thanh button trang khi danh sách trống
		model.addAttribute("trangdau", 1);
		model.addAttribute("trangcuoi", tempfor);
		model.addAttribute("vitrihientai", vitrihientai);
		return lreturn;
	}

	// khi chọn button thì chạy cái này. lấy page xuất danh sách
	@RequestMapping("/tkpage")
	public String tkpage(ModelMap model, @RequestParam("page") int page,
			@ModelAttribute("taikhoan") Account taikhoan) {
		activemenu(model);
		PageRequest pageable = PageRequest.of(page - 1, 10);
		List<Account> l = (List<Account>) iTaikhoanServices.findAllTk(pageable);
		model.addAttribute("titlepage", "List of accounts");
		vitrihientai = page;

		model.addAttribute("lTaikhoans", l);
		model.addAttribute("listSoLuongTrang", listSoLuongTrang(iTaikhoanServices.countFindAllTk(), model));
		model.addAttribute("vitrihientai", vitrihientai);
		return "qltk";

	}

	// số lượng button bấm chuyển trang
	public List<Integer> listSoLuongTrangtim(Double count, ModelMap model) {
		List<Integer> lreturn = new ArrayList<>();
		double temp = Double.parseDouble(count + "") / 10.0;
		int tempfor = (int) Math.ceil(temp);
		int a = 3;
		int b = 3;

		if (vitrihientai == 1) {
			a = 0;
			b = 6;
		}
		if (vitrihientai == 2) {
			a = 1;
			b = 5;
		}
		if (vitrihientai == 3) {
			a = 2;
			b = 4;
		}
		if (vitrihientai == 4) {
			a = 3;
			b = 3;
		}

		if (vitrihientai == tempfor) {
			a = 6;
			b = 0;
		}
		if (vitrihientai == (tempfor - 1)) {
			a = 5;
			b = 1;
		}
		if (vitrihientai == (tempfor - 2)) {
			a = 4;
			b = 2;
		}
		if (vitrihientai == (tempfor - 3)) {
			a = 3;
			b = 3;
		}
		if (vitrihientai == 1 && vitrihientai == tempfor) {
			a = 0;
			b = 0;
		}
		if (vitrihientai == 1 && vitrihientai == tempfor - 1) {
			a = 0;
			b = 1;
		}
		if (vitrihientai == 1 && vitrihientai == tempfor - 2) {
			a = 0;
			b = 2;
		}
		if (vitrihientai == 1 && vitrihientai == tempfor - 3) {
			a = 0;
			b = 3;
		}
		if (vitrihientai == 1 && vitrihientai == tempfor - 4) {
			a = 0;
			b = 4;
		}
		if (vitrihientai == 1 && vitrihientai == tempfor - 5) {
			a = 0;
			b = 5;
		}
		if (vitrihientai == 1 && vitrihientai == tempfor - 6) {
			a = 0;
			b = 6;
		}

		// -------------
		if (vitrihientai == 2 && vitrihientai == tempfor) {
			a = 1;
			b = 0;
		}
		if (vitrihientai == 2 && vitrihientai == tempfor - 1) {
			a = 1;
			b = 1;
		}
		if (vitrihientai == 2 && vitrihientai == tempfor - 2) {
			a = 1;
			b = 2;
		}
		if (vitrihientai == 2 && vitrihientai == tempfor - 3) {
			a = 1;
			b = 3;
		}
		if (vitrihientai == 2 && vitrihientai == tempfor - 4) {
			a = 1;
			b = 4;
		}
		if (vitrihientai == 2 && vitrihientai == tempfor - 5) {
			a = 1;
			b = 5;
		}
		// -------------
		if (vitrihientai == 3 && vitrihientai == tempfor) {
			a = 2;
			b = 0;
		}
		if (vitrihientai == 3 && vitrihientai == tempfor - 1) {
			a = 2;
			b = 1;
		}
		if (vitrihientai == 3 && vitrihientai == tempfor - 2) {
			a = 2;
			b = 2;
		}
		if (vitrihientai == 3 && vitrihientai == tempfor - 3) {
			a = 2;
			b = 3;
		}
		if (vitrihientai == 3 && vitrihientai == tempfor - 4) {
			a = 2;
			b = 4;
		}
		// -------------
		if (vitrihientai == 4 && vitrihientai == tempfor) {
			a = 3;
			b = 0;
		}
		if (vitrihientai == 4 && vitrihientai == tempfor - 1) {
			a = 3;
			b = 1;
		}
		if (vitrihientai == 4 && vitrihientai == tempfor - 2) {
			a = 3;
			b = 2;
		}
		if (vitrihientai == 4 && vitrihientai == tempfor - 3) {
			a = 3;
			b = 3;
		}
		// -------------
		if (vitrihientai == 5 && vitrihientai == tempfor) {
			a = 4;
			b = 0;
		}
		if (vitrihientai == 5 && vitrihientai == tempfor - 1) {
			a = 4;
			b = 1;
		}
		if (vitrihientai == 5 && vitrihientai == tempfor - 2) {
			a = 4;
			b = 2;
		}
		if (vitrihientai == 5 && vitrihientai == tempfor - 3) {
			a = 4;
			b = 3;
		}
		// -------------
		if (vitrihientai == 6 && vitrihientai == tempfor) {
			a = 5;
			b = 0;
		}
		if (vitrihientai == 6 && vitrihientai == tempfor - 1) {
			a = 5;
			b = 1;
		}
		if (vitrihientai == 6 && vitrihientai == tempfor - 2) {
			a = 5;
			b = 2;
		}
		if (vitrihientai == 6 && vitrihientai == tempfor - 3) {
			a = 5;
			b = 3;
		}

		for (int i = vitrihientai - a; i <= vitrihientai + b; i++) {
			lreturn.add(i);
		}
		if (count.intValue() == 0) {
			lreturn.clear();
		}

		model.addAttribute("trangdau", 1);
		model.addAttribute("trangcuoi", tempfor);
		model.addAttribute("vitrihientai", vitrihientai);
		return lreturn;
	}

	// khi chọn button thì chạy cái này. lấy page xuất danh sách
	@RequestMapping("/tkpagetim")
	public String tkpagetim(ModelMap model, @RequestParam("page") int page,
			@ModelAttribute("taikhoan") Account taikhoan, HttpServletRequest request) {
		activemenu(model);
		model.addAttribute("titlepage", "List of accounts");
		vitrihientai = page;
		PageRequest pageable = PageRequest.of(page - 1, 10);
		String data = request.getParameter("data");
		List<Account> ltim;
		ltim = iTaikhoanServices.ListFindtdnOrName(data, pageable);
		model.addAttribute("data", data);
		
		model.addAttribute("lTaikhoans", ltim);
		model.addAttribute("listSoLuongTrang", listSoLuongTrangtim(iTaikhoanServices.countListFindtdnOrName(data), model));
		model.addAttribute("vitrihientai", vitrihientai);
		model.addAttribute("danhsachtim", 1);
		model.addAttribute("danhsach", 0);
		return "qltk";

	}

	private void activemenu(ModelMap model) {

		// cai dat. tat ca duoi deu show tren cai dat --------------------
		model.addAttribute("chamshowcd", ".show");
		// show quan ly tai khoan
		model.addAttribute("chamshowqltk", ".show");
		model.addAttribute("activedstk", "active");
		model.addAttribute("activettk", null);

		// quan ly loai phong
		model.addAttribute("chamshowqllp", null);
		model.addAttribute("activedslp", null);
		model.addAttribute("activetlp", null);

		// quan ly phong
		model.addAttribute("chamshowqlp", null);
		model.addAttribute("activedsp", null);
		model.addAttribute("activetp", null);

		// dich vu
		model.addAttribute("chamshowdv", null);
		model.addAttribute("activedv", null);
		model.addAttribute("activetdv", null);
	}

}
