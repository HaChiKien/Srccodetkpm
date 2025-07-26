package DuAn2.Controller;

import DuAn2.Dto.BookingDTO;
import DuAn2.Event.OnBookingSuccessEvent;
import DuAn2.Model.Checkin;
import DuAn2.Model.CheckinCalendar;
import DuAn2.Model.Room;
import DuAn2.QR.QRCodeGenerator;
import DuAn2.Model.Checkout;
import DuAn2.Services.EmailService;
import DuAn2.Services.ITraPhong;
import DuAn2.Services.IttkhService;
import DuAn2.Services.LichDatPhongService;
import DuAn2.Services.QuanLyPhongService;
import DuAn2.hepler.HtmlEmailTemplateUtil;
import DuAn2.hepler.OnePayConfig;
import DuAn2.hepler.VnpayHashUtil;
import DuAn2.hepler.ZXingHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import com.google.zxing.WriterException;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;

import java.nio.charset.StandardCharsets;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;


@EnableAutoConfiguration(exclude = {ErrorMvcAutoConfiguration.class})
@Controller
public class HomeController {


    @Autowired
    private QuanLyPhongService quanLyPhongService;

    @Autowired
    private LichDatPhongService lichDatPhongService;

    @Autowired
    private ITraPhong iTraPhong;

    @Autowired
    private IttkhService ittkhService;


	
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @RequestMapping({"/", "/home"})
    public String index(ModelMap model) {
        return "home";
    }


    @ModelAttribute(name = "changeURL")
    public String changeURL() {
        return "dptp";
    }

    
    //Search Room
    @GetMapping("/search-available")
    public String searchAvailable(@RequestParam("checkInDate") String checkInDate,
                                  @RequestParam("checkOutDate") String checkOutDate,
                                  @RequestParam("typeRoom") String typeRoom,
                                  @RequestParam("maxPrice") double maxPrice,
                                  Model model) {

        List<Room> phongs = quanLyPhongService.findAllByGiaPhongLessThanAndLoaiPhongTenLoaiPhong(maxPrice, typeRoom);
        List<Room> availbleRooms = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        for (Room phong : phongs) {
            if (ittkhService.findAllByRoomMaPhongOrderByNgayDatDesc(phong.getMaPhong()).isEmpty()) {

                availbleRooms.add(phong);
            } else {
                boolean checkDate = true;

                for (Checkin datPhong : ittkhService.findAllByRoomMaPhongOrderByNgayDatDesc(phong.getMaPhong())) {

                    Checkout traPhong = iTraPhong.getByDatPhongMaDatPhong(datPhong.getMaDatPhong());
                    if (traPhong == null) {

                        continue;
                    } else if ((simpleDateFormat.format(datPhong.getNgayDat()).compareTo(checkInDate) < 1 && simpleDateFormat.format(traPhong.getNgayTra()).compareTo(checkOutDate) > -1)) {

                        checkDate = false;
                        break;
                    }
                }
                if (checkDate)
                    availbleRooms.add(phong);
            }
        }

        model.addAttribute("listRoom", availbleRooms);
        return "room";
    }

    
    //Booking
    @RequestMapping(value = "/booking", method = RequestMethod.POST)
    public String bookingRoom(@Valid @ModelAttribute("bookingDTO") BookingDTO bookingDTO, Model model, WebRequest request) throws ParseException {
        Room phong = quanLyPhongService.getByMaPhong(bookingDTO.getRoomCode());
        if (phong == null) {
            if (!quanLyPhongService.findAllByLoaiPhongTenLoaiPhong(bookingDTO.getRoomType()).isEmpty()) {
                List<Room> phongs = quanLyPhongService.findAllByLoaiPhongTenLoaiPhong(bookingDTO.getRoomType());
                for (Room p : phongs) {
                    if (lichDatPhongService.findAllByRoomMaPhongOrderByNgayDatDesc(p.getMaPhong()).isEmpty()) {
                        phong = p;
                        break;
                    } else {

                        if (checkDate(p, bookingDTO.getCheckInDate(), bookingDTO.getCheckOutDate())) {
                            phong = p;
                            break;
                        }
                    }
                }
            } else {
                model.addAttribute("error", "Out of room");
                return "booking";
            }
        } else {
            if (!lichDatPhongService.findAllByRoomMaPhongOrderByNgayDatDesc(phong.getMaPhong()).isEmpty()) {

                if (!checkDate(phong, bookingDTO.getCheckInDate(), bookingDTO.getCheckOutDate())) {
                    model.addAttribute("error", "Room has been reserved");
                    return "booking";
                }

            }
        }

        if (phong == null) {
            model.addAttribute("error", "Out of room");
            return "booking";
        }

        if (bookingDTO.getCheckInDate().compareToIgnoreCase(new SimpleDateFormat("yyyy-MM-dd").format(new Date())) < 0) {
            System.out.println(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            System.out.println(bookingDTO.getCheckInDate());
            model.addAttribute("error", "Check in date can greater or equal today");
            return "booking";
        }


        if (bookingDTO.getCheckInDate().compareTo(bookingDTO.getCheckOutDate()) > -1) {
            model.addAttribute("error", "Check in date can't greater check out date");
            return "booking";
        }

        java.sql.Date sqlDate = java.sql.Date.valueOf(String.valueOf(bookingDTO.getCheckInDate()));
        java.sql.Date sqlDate1 = java.sql.Date.valueOf(String.valueOf(bookingDTO.getCheckOutDate()));
        CheckinCalendar checkinCalendar = new CheckinCalendar((int) (lichDatPhongService.countfindAll() + 1),

                bookingDTO.getName(),
                bookingDTO.getPhoneNumber(),
                bookingDTO.getEmail(),

                phong,

                sqlDate,
                sqlDate1
        );

        lichDatPhongService.save(checkinCalendar);

        try {
            String appUrl = request.getContextPath();
            eventPublisher.publishEvent(new OnBookingSuccessEvent(bookingDTO, appUrl, phong));
        } catch (Exception re) {
            re.printStackTrace();
        }
        model.addAttribute("errors", "Successful reservation");
        return "booking";
    }

    private boolean checkDate(Room phong, String checkInDate, String checkOutDate) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        boolean checkDate = true;

        for (Checkin datPhong : ittkhService.findAllByRoomMaPhongOrderByNgayDatDesc(phong.getMaPhong())) {

            Checkout traPhong = iTraPhong.getByDatPhongMaDatPhong(datPhong.getMaDatPhong());
            if (traPhong != null)
                if ((simpleDateFormat.format(datPhong.getNgayDat()).compareTo(checkInDate) < 1 && simpleDateFormat.format(traPhong.getNgayTra()).compareTo(checkInDate) > -1) ||
                        (simpleDateFormat.format(datPhong.getNgayDat()).compareTo(checkOutDate) < 1 && simpleDateFormat.format(traPhong.getNgayTra()).compareTo(checkOutDate) > -1) ||
                        (simpleDateFormat.format(datPhong.getNgayDat()).compareTo(checkOutDate) < 1 && simpleDateFormat.format(traPhong.getNgayTra()).compareTo(checkInDate) > -1)) {

                    checkDate = false;
                    break;
                }
        }
        return checkDate;
    }

    
    //CheckCodeQR
    @RequestMapping(value = "qrcode/{name}", method = RequestMethod.GET)
    public void qrcode(
            @PathVariable("name") String name,

            HttpServletResponse response) throws Exception {
        response.setContentType("image/png");
        OutputStream outputStream = response.getOutputStream();
        outputStream.write(ZXingHelper.getQRCodeImage(name, 100, 100));
        outputStream.flush();
        outputStream.close();
    }

    @Autowired
    private JavaMailSender javaMailSender;



    
    //SendMail
    @RequestMapping(value = "/send")
    public String sendmail(@RequestParam("name") String name,
                           @RequestParam("to") String to,
                           @RequestParam("subject") String subject,
                           @RequestParam("content") String content) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setText(name);
        msg.setTo(to);
        msg.setSubject(subject);
        msg.setText(content);
        msg.setFrom("no-reply@example.com");
        javaMailSender.send(msg);

        return "redirect:contact";
    }


//Booking 
    @RequestMapping(value = "/bookingroom", method = RequestMethod.POST)
    public String bookinginvoice(@Valid @ModelAttribute("bookingDTO") BookingDTO bookingDTO, Model model, WebRequest request, HttpSession session, ModelMap modelMap) throws ParseException {
        Room phong = quanLyPhongService.getByMaPhong(bookingDTO.getRoomCode());
        
        if (phong == null) {
            if (!quanLyPhongService.findAllByLoaiPhongTenLoaiPhong(bookingDTO.getRoomType()).isEmpty()) {
                List<Room> phongs = quanLyPhongService.findAllByLoaiPhongTenLoaiPhong(bookingDTO.getRoomType());
                for (Room p : phongs) {
                    if (ittkhService.findAllByRoomMaPhongOrderByNgayDatDesc(p.getMaPhong()).isEmpty()) {
                        phong = p;
                        break;
                    } else {

                        if (checkDate(p, bookingDTO.getCheckInDate(), bookingDTO.getCheckOutDate())) {
                            phong = p;
                            break;
                        }
                    }
                }
            } else {
                model.addAttribute("error", "Out of room");
                return "bookingroom";
            }
        } else {
            if (!ittkhService.findAllByRoomMaPhongOrderByNgayDatDesc(phong.getMaPhong()).isEmpty()) {

                if (!checkDate(phong, bookingDTO.getCheckInDate(), bookingDTO.getCheckOutDate())) {
                    model.addAttribute("error", "Room has been reserved");
                    return "bookingroom";
                }

            }
        }

        if (phong == null) {
            model.addAttribute("error", "Out of room");
            return "bookingroom";
        }

        if (bookingDTO.getCheckInDate().compareToIgnoreCase(new SimpleDateFormat("yyyy-MM-dd").format(new Date())) < 0) {
            System.out.println(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            //ở đây viết đúng nó hiện số chuẩn :v
            System.out.println(bookingDTO.getCheckInDate());
            model.addAttribute("error", "Check in date can greater or equal today");
            return "bookingroom";
        }


        if (bookingDTO.getCheckInDate().compareTo(bookingDTO.getCheckOutDate()) > -1) {
            model.addAttribute("error", "Check in date can't greater check out date");
            return "bookingroom";
        }

        java.sql.Date sqlDate = java.sql.Date.valueOf(String.valueOf(bookingDTO.getCheckInDate()));
        java.sql.Date sqlDate1 = java.sql.Date.valueOf(String.valueOf(bookingDTO.getCheckOutDate()));

        Checkin datPhong = new Checkin((int) (ittkhService.countfindAll() + 1),
                bookingDTO.getName(),
                bookingDTO.getPhoneNumber(),
                bookingDTO.getEmail(),
                phong,
                sqlDate
        );

        Checkout traPhong = new Checkout(iTraPhong.findAll().size() + 1,
                datPhong,
                sqlDate1,
                phong.getGiaPhong());

        session.setAttribute("bookingDTO", bookingDTO);
        session.setAttribute("datPhong", datPhong);
        session.setAttribute("traPhong", traPhong);
        session.setAttribute("phong", phong);
        model.addAttribute("bookingDTO", session.getAttribute("bookingDTO"));
        model.addAttribute("datPhong", session.getAttribute("datPhong"));
        model.addAttribute("traPhong", session.getAttribute("traPhong"));
        model.addAttribute("phong", session.getAttribute("phong"));
        return "invoice";
    }
    
  /*  MoMo Payment
  	@RequestMapping(value = "/momo-payment", method = RequestMethod.GET)
    public String momoPayment(HttpSession session, Model model) throws Exception {
        Room phong = (Room) session.getAttribute("phong");
        BookingDTO bookingDTO = (BookingDTO) session.getAttribute("bookingDTO");

        String endpoint = "https://test-payment.momo.vn/v2/gateway/api/create";
        String partnerCode = "MOMO0HGO20210715"; // <-- Thay bằng mã của bạn
        String accessKey = "F8BBA842ECF85";
        String secretKey = "K951B6PE1waDMi640xX08PD3vg6EkVlz";

        String orderInfo = "Thanh toán đặt phòng tại HomieHotel";
        String redirectUrl = "http://localhost:9596/momo-return";
        String ipnUrl = "http://localhost:9596/momo-ipn";
        Double giaPhong = phong.getGiaPhong();
        String amount = String.valueOf(giaPhong.intValue());
        System.out.println("Class of giaPhong: " + phong.getGiaPhong().getClass());
        String orderId = UUID.randomUUID().toString();
        String requestId = UUID.randomUUID().toString();
        String requestType = "captureWallet";
        String extraData = "";

        // Chuỗi tạo chữ ký
        String rawHash = "accessKey=" + accessKey +
                "&amount=" + amount +
                "&extraData=" + extraData +
                "&ipnUrl=" + ipnUrl +
                "&orderId=" + orderId +
                "&orderInfo=" + orderInfo +
                "&partnerCode=" + partnerCode +
                "&redirectUrl=" + redirectUrl +
                "&requestId=" + requestId +
                "&requestType=" + requestType;

        // Tạo chữ ký HMAC SHA256
        String signature = hmacSHA256(rawHash, secretKey);

        Map<String, Object> body = new HashMap<>();
        body.put("partnerCode", partnerCode);
        body.put("partnerName", "HomieHotel");
        body.put("storeId", "Homie001");
        body.put("requestId", requestId);
        body.put("amount", amount);
        body.put("orderId", orderId);
        body.put("orderInfo", orderInfo);
        body.put("redirectUrl", redirectUrl);
        body.put("ipnUrl", ipnUrl);
        body.put("lang", "vi");
        body.put("extraData", extraData);
        body.put("requestType", requestType);
        body.put("signature", signature);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest momoRequest = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(new ObjectMapper().writeValueAsString(body)))
                .build();

        HttpResponse<String> response = client.send(momoRequest, HttpResponse.BodyHandlers.ofString());
        JsonNode jsonNode = new ObjectMapper().readTree(response.body());

        String respBody = response.body();
        System.out.println("MoMo response: " + respBody);

        JsonNode jsonNode1 = new ObjectMapper().readTree(respBody);
        JsonNode payUrlNode = jsonNode1.get("payUrl");

        if (payUrlNode == null) {
            String errorMsg = jsonNode1.path("message")
                                .asText(jsonNode1.path("localMessage").asText("Không rõ lỗi"));
            System.err.println("❌ MoMo tạo đơn lỗi: " + errorMsg);
            model.addAttribute("bookingDTO", new BookingDTO());
            model.addAttribute("error", "Thanh toán MoMo thất bại: " + errorMsg);
            return "bookingroom";  // hoặc view báo lỗi
        }

        String payUrl = payUrlNode.asText();
        return "redirect:" + payUrl;
    }

    private String hmacSHA256(String data, String key) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(secretKeySpec);
        byte[] result = mac.doFinal(data.getBytes("UTF-8"));
        return Hex.encodeHexString(result);
    }

    @RequestMapping("/momo-return")
    public String momoReturn(@RequestParam Map<String, String> params, Model model, HttpSession session) {
        String resultCode = params.get("resultCode");

        if ("0".equals(resultCode)) {
            Checkin datPhong = (Checkin) session.getAttribute("datPhong");
            Checkout traPhong = (Checkout) session.getAttribute("traPhong");

            ittkhService.save(datPhong);
            iTraPhong.save(traPhong);

            model.addAttribute("message", "Thanh toán MoMo thành công!");
            return "success";
        } else {
            model.addAttribute("message", "Thanh toán thất bại.");
            return "bookingroom";
        }
    }
  */
   
    
    @GetMapping("/vnpay-payment")
    public String vnpayPayment(HttpServletRequest request, HttpSession session) throws UnsupportedEncodingException {
        Room phong = (Room) session.getAttribute("phong");
        BookingDTO bookingDTO = (BookingDTO) session.getAttribute("bookingDTO");

        String vnp_TmnCode = "9INU7Y9V";
        String vnp_HashSecret = "S8D4ZJVIM8WUSK2F3F483BFC6CF80WF8";
        String vnp_Url = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
        String vnp_Returnurl = "http://localhost:9596/vnpay-return";

        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String orderType = "billpayment";
        String vnp_TxnRef = String.valueOf(System.currentTimeMillis());
        String vnp_IpAddr = request.getRemoteAddr();
        String vnp_TmnCodeFinal = vnp_TmnCode;
        int amount = phong.getGiaPhong().intValue() * 259950; // Quy đổi USD to VND

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCodeFinal);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Booking payment for HomieHotel.");
        vnp_Params.put("vnp_OrderType", orderType);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", vnp_Returnurl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
        vnp_Params.put("vnp_CreateDate", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));

        // B1: Sort parameters
        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();

        for (String fieldName : fieldNames) {
            String value = vnp_Params.get(fieldName);
            if (value != null && !value.isEmpty()) {
                hashData.append(fieldName).append('=').append(URLEncoder.encode(value, StandardCharsets.US_ASCII)).append('&');
                query.append(fieldName).append('=').append(URLEncoder.encode(value, StandardCharsets.US_ASCII)).append('&');
            }
        }

        // B2: Tạo secure hash
        String queryString = query.substring(0, query.length() - 1);
        String rawData = hashData.substring(0, hashData.length() - 1);
        String secureHash = VnpayHashUtil.getSecureHash(vnp_Params, vnp_HashSecret);
        String redirectUrl = vnp_Url + "?" + queryString + "&vnp_SecureHash=" + secureHash;

        return "redirect:" + redirectUrl;
    }

    
    @Autowired
    private EmailService emailService;

	private Map<String, String> variables;
    @GetMapping("/vnpay-return")
    public String vnpayReturn(@RequestParam Map<String, String> params, Model model, HttpSession session) throws WriterException, IOException {
        String responseCode = params.get("vnp_ResponseCode");
        if ("00".equals(responseCode)) {
            Checkin datPhong = (Checkin) session.getAttribute("datPhong");
            Checkout traPhong = (Checkout) session.getAttribute("traPhong");
            BookingDTO bookingDTO = (BookingDTO) session.getAttribute("bookingDTO");
            Room phong = datPhong.getPhong();
            String bookingCode = "ORDER-" + datPhong.getMaDatPhong();
            String qrText = "https://homiehotel.vn/confirm?code=" + bookingCode;
            byte[] qrImage = QRCodeGenerator.getQRCodeImage(qrText, 200, 200);
            String qrBase64 = Base64.getEncoder().encodeToString(qrImage);
            
            String imagePath = "src/main/webapp/hinh/phong/" + phong.getHinhAnh();
            String roomImageBase64 = QRCodeGenerator.encodeImageToBase64(imagePath);
            String roomImageData = "data:image/jpg;base64," + roomImageBase64;

            ittkhService.save(datPhong);
            iTraPhong.save(traPhong);
            Map<String, String> variables = Map.of(
            	    "name", bookingDTO.getName(),
            	    "room", datPhong.getPhong().getSoPhong().toString(),
            	    "roomType", phong.getLoaiPhong().getTenLoaiPhong(),  
            	    "roomfloat", phong.getTang().toString(),                         
            	    "roomImage", roomImageData,
            	    "checkin", datPhong.getNgayDat().toString(),
            	    "checkout", traPhong.getNgayTra().toString(),
            	    "amount", String.valueOf(traPhong.getTongTien()),
            	    "qrcode", qrBase64
            	    		
            	);

            	String html = HtmlEmailTemplateUtil.loadTemplate("templates/email/invoice.html", variables);
            	try {
					emailService.sendHtmlEmail(bookingDTO.getEmail(), "Xác nhận đơn HomieHotel (QR)", html);
				} catch (MessagingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            model.addAttribute("message", "Payment via VNPAY successful!");
            return "success";
        } else {
        	BookingDTO bookingDTO = (BookingDTO) session.getAttribute("bookingDTO");
            model.addAttribute("bookingDTO", bookingDTO != null ? bookingDTO : new BookingDTO());
            model.addAttribute("error", "Payment via VNPAY failed.");
            return "bookingroom";
        }
        
    }

    @GetMapping("/qr")
    public String showQRCode(Model model) throws Exception {
        String qrText = "https://homiehotel.vn/booking/12345";
        byte[] image = QRCodeGenerator.getQRCodeImage(qrText, 250, 250);
        String base64Image = Base64.getEncoder().encodeToString(image);
        model.addAttribute("qr", base64Image);
        return "invoice"; 
    }
    //onepay
    @PostMapping("/onepay-international")
    public RedirectView createInternationalPayment(HttpServletRequest request) throws Exception {
        String orderInfo = request.getParameter("orderInfo");
        String amountStr = request.getParameter("amount");

        if (amountStr == null || amountStr.trim().isEmpty()) {
            amountStr = "100"; // Hoặc throw lỗi rõ ràng tùy bạn
        }
        int amount = Integer.parseInt(amountStr); 
        String txnRef = UUID.randomUUID().toString().replace("-", "").substring(0,20);
        String ipAddr = request.getRemoteAddr();

        Map<String, String> fields = new HashMap<>();
        fields.put("vpc_Version", "2");
        fields.put("vpc_Command", "pay");
        fields.put("vpc_AccessCode", OnePayConfig.ACCESS_CODE);
        fields.put("vpc_Merchant", OnePayConfig.MERCHANT_ID);
        fields.put("vpc_MerchTxnRef", txnRef);
        fields.put("vpc_OrderInfo", orderInfo);
        fields.put("vpc_Amount", String.valueOf(amount));
        fields.put("vpc_Currency", "VND");
        fields.put("vpc_Locale", "en"); // hoặc "vn"
        fields.put("vpc_ReturnURL", OnePayConfig.RETURN_URL);
        fields.put("vpc_TicketNo", ipAddr);

        SortedMap<String, String> sortedFields = new TreeMap<>(fields);

        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        for (Map.Entry<String, String> entry : sortedFields.entrySet()) {
            hashData.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            query.append(URLEncoder.encode(entry.getKey(), "UTF-8"))
                 .append("=")
                 .append(URLEncoder.encode(entry.getValue(), "UTF-8"))
                 .append("&");
        }
        hashData.deleteCharAt(hashData.length() - 1);
        query.deleteCharAt(query.length() - 1);

        String secureHash = hmacSHA256(hashData.toString(), OnePayConfig.HASH_SECRET);

        String redirectUrl = OnePayConfig.PAYMENT_URL + "?" + query.toString() + "&vpc_SecureHash=" + secureHash;
        System.out.println("Redirect URL: " + redirectUrl);
        return new RedirectView(redirectUrl);
        
    }
    
    

    @GetMapping("/onepay-return") 
    public String handleOnePayReturn(@RequestParam Map<String, String> params, Model model, HttpSession session) throws WriterException, 
    IOException, MessagingException { String txnResponseCode = params.get("vpc_TxnResponseCode"); String receivedHash = params.get("vpc_SecureHash");
    
 // Bước 1: Xác minh chữ ký hash trả về từ OnePAY
    try {
    	
    	BookingDTO bookingDTO = (BookingDTO) session.getAttribute("bookingDTO"); 
    	if (bookingDTO == null) bookingDTO = new BookingDTO(); 
    	model.addAttribute("bookingDTO", bookingDTO);
        boolean isValid = verifySecureHash(params, receivedHash);
        System.out.println("✅ [DEBUG] Hash valid? " + isValid);
        if (!isValid) {
            model.addAttribute("error", "Dữ liệu trả về từ OnePAY không hợp lệ (sai chữ ký).");
            return "bookingroom";
        }
    } catch (Exception e) {
        model.addAttribute("error", "Lỗi xác minh chữ ký OnePAY: " + e.getMessage());
        return "bookingroom";
    }

    // Bước 2: Kiểm tra mã phản hồi
    if ("0".equals(txnResponseCode)) {
        // Giao dịch thành công
        Checkin datPhong = (Checkin) session.getAttribute("datPhong");
        Checkout traPhong = (Checkout) session.getAttribute("traPhong");

        if (datPhong != null) ittkhService.save(datPhong);
        if (traPhong != null) iTraPhong.save(traPhong);

        // Tạo mã QR
        String bookingCode = "ORDER-" + datPhong.getMaDatPhong();
        String qrText     = "https://homiehotel.vn/confirm?code=" + bookingCode;
        byte[] qrImage    = QRCodeGenerator.getQRCodeImage(qrText, 250, 250);
        String qrBase64   = Base64.getEncoder().encodeToString(qrImage);
        model.addAttribute("duongDan", qrImage);

        // Gửi email mã QR    
        String email = datPhong != null ? datPhong.getEmail() : null;
        if (email != null && !email.isEmpty()) {
            // Build nội dung email, ví dụ embed QR base64 trong HTML
            String html = "<h3>HomieHotel – Xác nhận thanh toán</h3>"
                        + "<p>Đơn " + bookingCode + " đã thanh toán thành công!</p>"
                        + "<img src='data:image/png;base64," + qrBase64 + "' />";
            emailService.sendHtmlEmail(email, "Xác nhận đơn HomieHotel", html);
        }

        model.addAttribute("message", "Thanh toán thành công bằng OnePAY.");
        return "success";
    } else {
    	BookingDTO bookingDTO = (BookingDTO) session.getAttribute("bookingDTO");
    	if (bookingDTO == null) bookingDTO = new BookingDTO();
    	model.addAttribute("bookingDTO", bookingDTO);
    	model.addAttribute("error", "Thanh toán thất bại từ OnePAY.");
    	return "bookingroom";
    }

        
    }
    private boolean verifySecureHash(Map<String, String> params, String receivedHash) throws Exception {
        SortedMap<String, String> sorted = new TreeMap<>();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (!key.equals("vpc_SecureHash") && !key.equals("vpc_SecureHashType") && value != null && !value.isEmpty()) {
                sorted.put(key, value);
            }
        }

        StringBuilder hashData = new StringBuilder();
        for (Map.Entry<String, String> entry : sorted.entrySet()) {
            hashData.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        if (hashData.length() > 0) hashData.setLength(hashData.length() - 1);

        String expectedHash = hmacSHA256(hashData.toString(), OnePayConfig.HASH_SECRET);
        System.out.println("🔐 Expected Hash: " + expectedHash);
        System.out.println("🔐 Received Hash: " + receivedHash);
        return expectedHash.equalsIgnoreCase(receivedHash);
    }
    
    private String hmacSHA256(String data, String key) throws Exception {
        javax.crypto.Mac mac = javax.crypto.Mac.getInstance("HmacSHA256");
        javax.crypto.spec.SecretKeySpec secret_key = new javax.crypto.spec.SecretKeySpec(
                hexStringToByteArray(key), "HmacSHA256");
        mac.init(secret_key);
        byte[] hash = mac.doFinal(data.getBytes("UTF-8"));
        return bytesToHex(hash).toUpperCase();
    }

    private static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                                 + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }
    

}
