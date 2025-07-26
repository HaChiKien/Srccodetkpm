package DuAn2.hepler;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class VnpayHashUtil {

    // Tạo secure hash HmacSHA512 từ map đã sort
    public static String getSecureHash(Map<String, String> params, String secretKey) {
        try {
            // B1: Sắp xếp key A-Z
            List<String> fieldNames = new ArrayList<>(params.keySet());
            Collections.sort(fieldNames);

            // B2: Gộp thành chuỗi dạng key1=value1&key2=value2...
            StringBuilder sb = new StringBuilder();
            for (String fieldName : fieldNames) {
                String value = params.get(fieldName);
                if (value != null && value.length() > 0) {
                    sb.append(fieldName).append('=')
                      .append(URLEncoder.encode(value, StandardCharsets.US_ASCII)).append('&');
                }
            }

            // B3: Xóa ký tự & cuối cùng
            String rawData = sb.substring(0, sb.length() - 1);

            // B4: Tạo HMAC SHA512
            Mac hmac = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "HmacSHA512");
            hmac.init(secretKeySpec);
            byte[] bytes = hmac.doFinal(rawData.getBytes());
            StringBuilder hash = new StringBuilder();
            for (byte b : bytes) {
                hash.append(String.format("%02x", b));
            }
            System.out.println("VNPAY RawData: " + rawData);
            System.out.println("Generated Hash: " + hash.toString());
            return hash.toString();

        } catch (Exception e) {
            throw new RuntimeException("Error generating VNPAY signature.", e);
            
        }
        
    }
}