package DuAn2.hepler;

public class OnePayConfig {
    // Merchant ID do OnePAY cấp (Sandbox hoặc Production)
    public static final String MERCHANT_ID = "ONEPAY"; // Example Merchant ID

    // Access Code do OnePAY cấp
    public static final String ACCESS_CODE = "6BEB2546"; // Example Access Code

    // Hash Secret Key để ký SHA256 (Hex format)
    public static final String HASH_SECRET = "6A4D81FD2FB3A5FE"; // Example Hash Key

    // URL thanh toán OnePAY Quốc tế (sandbox hoặc production)
    public static final String PAYMENT_URL = "https://mtf.onepay.vn/vpcpay/vpcpay.op";

    // URL server nhận kết quả trả về từ OnePAY
    public static final String RETURN_URL = "http://localhost:9596/onepay-return";
}
