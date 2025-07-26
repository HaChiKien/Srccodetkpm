package DuAn2.Controller;

import java.sql.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.*;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import DuAn2.Common.Common;
import DuAn2.Model.Display;
import DuAn2.Model.HistoryLogin;
import DuAn2.Model.Account;
import DuAn2.Services.GiaoDienService;
import DuAn2.Services.ITaikhoanServices;
import DuAn2.Services.LichSuDangNhapService;

@Controller
@Transactional
public class DangNhapController {

	@Autowired
	ITaikhoanServices dangnhapservice;

	@Autowired
	LichSuDangNhapService lichSuDangNhapService;

	@Autowired
	GiaoDienService giaoDienService;

	int checklogin = 0;

	@ModelAttribute(name = "changeURL")
	public String changeURL() {
		return "login";
	}

	@RequestMapping("/login")
	public String login(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		session.invalidate();
		if (checklogin == 1) {
			model.addAttribute("message", "Username or password is incorrect");
			checklogin = 0;
		}
		// get Organization Name
		session = request.getSession();
		Display giaoDien = new Display();
		giaoDien = ((List<Display>) giaoDienService.findAll()).get(0);
		session.setAttribute("tenToChuc", giaoDien.getTenToChuc());
		session.setAttribute("diaChi", giaoDien.getDiaChi());
		session.setAttribute("soDienThoai", giaoDien.getSoDienThoai());
		// TODO Auto-generated method stub
		return "login";
	}

	@RequestMapping("/actionlogin")
	public String actiondangnhap(ModelMap model, HttpServletRequest httpServletRequest, HttpServletResponse response,
	                             @RequestParam("username") String tendangnhap, @RequestParam("password") String matkhau) {
		matkhau = Common.encode(matkhau);
		List<Account> l = dangnhapservice.findUser(tendangnhap, matkhau);

		if (l.isEmpty()) {
			checklogin = 1;
			return "redirect:/login";
		} else {
			HttpSession session = httpServletRequest.getSession();
			saveLichSuDangNhap(tendangnhap);
			session.setAttribute("nguoidung", tendangnhap);
			session.setAttribute("chucvu", l.get(0).getChucVu().getMaChucVu() + "");// 1 giam doc 2 nhan vien
			return "redirect:/dptp";
		}
	}

	public void saveLichSuDangNhap(String tendangnhap) {
		HistoryLogin lsdn = new HistoryLogin();
		Date date = new Date(System.currentTimeMillis());
		lsdn.setTaiKhoanDangNhap(tendangnhap);
		lsdn.setNgayDangNhap(date);
		lsdn.setGioDangNhap(date);
		lichSuDangNhapService.save(lsdn);
	}

	@RequestMapping("/dangxuat")
	public String dangxuat() {
		return "redirect:/login";
	}

	@RequestMapping("/doimatkhau")
	public String doimatkhau(HttpServletRequest httpServletRequest, ModelMap model,
	                         @ModelAttribute("taikhoan") Account taikhoan) {
		HttpSession session = httpServletRequest.getSession();
		Account gettaikhoan = dangnhapservice.findById(session.getAttribute("nguoidung").toString()).get();
		model.addAttribute("gettaikhoan", gettaikhoan);
		model.addAttribute("changeURL", "doimatkhau");
		model.addAttribute("titlepage", "Change Password");
		return "doimatkhau";
	}

	@RequestMapping("/actiondoimatkhau")
	public String actiondoimatkhau(HttpServletRequest httpServletRequest, ModelMap model,
	                               @ModelAttribute("taikhoan") Account taikhoan) {
		String matkhaucu = httpServletRequest.getParameter("matkhaucu");
		String matkhaumoi = httpServletRequest.getParameter("matkhaumoi");
		matkhaucu = Common.encode(matkhaucu);
		matkhaumoi = Common.encode(matkhaumoi);
		taikhoan.setMatKhau(Common.encode(taikhoan.getMatKhau()));
		model.addAttribute("titlepage", "Change Password");
		HttpSession session = httpServletRequest.getSession();
		Account gettaikhoan = dangnhapservice.findById(session.getAttribute("nguoidung").toString()).get();
		if (!matkhaucu.equals(gettaikhoan.getMatKhau())) {
			model.addAttribute("messageloi", "Old password is incorrect");
			taikhoan.setMatKhau("");
			return "doimatkhau";
		} else if (!matkhaumoi.equals(taikhoan.getMatKhau())) {
			model.addAttribute("messageloi", "Confirm the new password is incorrect");
			taikhoan.setMatKhau("");
			return "doimatkhau";
		} else if (matkhaumoi.length() < 9) {
			taikhoan.setMatKhau("");
			model.addAttribute("messageloi", "Password must be 8 characters or more");
			return "doimatkhau";
		} else {
			dangnhapservice.save(taikhoan);
			model.addAttribute("message", "Password changed successfully");
			taikhoan.setMatKhau("");
			return "doimatkhau";
		}
	}

}