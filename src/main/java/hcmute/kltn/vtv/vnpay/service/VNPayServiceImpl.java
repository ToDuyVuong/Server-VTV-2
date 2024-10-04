package hcmute.kltn.vtv.vnpay.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.storage.HttpMethod;
import com.google.gson.JsonObject;
import hcmute.kltn.vtv.service.user.IOrderService;
import hcmute.kltn.vtv.vnpay.VNPayConfig;
import hcmute.kltn.vtv.vnpay.model.VNPayDTO;
import hcmute.kltn.vtv.vnpay.model.VNPayResponse;
import hcmute.kltn.vtv.vnpay.service.impl.IVNPayService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class VNPayServiceImpl implements IVNPayService {

    private final IOrderService orderService;

    @Override
    public VNPayResponse createPaymentByVNPay(UUID orderId, String ipAddress, String username) {

        Long amount = orderService.getTotalPaymentByOrderId(orderId, username);
        Map<String, String> vnp_Params = getVNPayParams(amount, orderId.toString(), ipAddress, VNPayConfig.return_url_default);
        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringJoiner hashData = new StringJoiner("&");
        StringJoiner query = new StringJoiner("&");
        for (String fieldName : fieldNames) {
            String fieldValue = vnp_Params.get(fieldName);
            if (fieldValue != null && !fieldValue.isEmpty()) {
                //Build hash data
                hashData.add(fieldName + "=" + URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                //Build query
                query.add(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII) + "=" + URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = VNPayConfig.vnp_PayUrl + "?" + queryUrl;


        return VNPayResponse.vnPayResponse(paymentUrl, "Tạo mã thanh toán cho đơn hàng thành công.", "Success");
    }

    @Override
    public VNPayResponse createPaymentByVNPayForWeb(UUID orderId, String ipAddress, String username) {
        Long amount = orderService.getTotalPaymentByOrderId(orderId, username);
        Map<String, String> vnp_Params = getVNPayParams(amount, orderId.toString(), ipAddress, VNPayConfig.return_url_for_web);
        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringJoiner hashData = new StringJoiner("&");
        StringJoiner query = new StringJoiner("&");
        for (String fieldName : fieldNames) {
            String fieldValue = vnp_Params.get(fieldName);
            if (fieldValue != null && !fieldValue.isEmpty()) {
                //Build hash data
                hashData.add(fieldName + "=" + URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                //Build query
                query.add(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII) + "=" + URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = VNPayConfig.vnp_PayUrl + "?" + queryUrl;


        return VNPayResponse.vnPayResponse(paymentUrl, "Tạo mã thanh toán cho đơn hàng thành công.", "Success");
    }


    @Override
    public VNPayResponse createPaymentByVNPayWithMultipleOrder(List<UUID> orderIds, String ipAddress, String username) {
        Long amount  = 0L;
        for (UUID orderId : orderIds) {
            amount += orderService.getTotalPaymentByOrderId(orderId, username);
        }
        String vnp_TxnRef = orderIds.stream().map(UUID::toString).reduce((s1, s2) -> s1 + "," + s2).orElse("");
        Map<String, String> vnp_Params = getVNPayParams(amount, vnp_TxnRef, ipAddress, VNPayConfig.return_url_default);
        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringJoiner hashData = new StringJoiner("&");
        StringJoiner query = new StringJoiner("&");
        for (String fieldName : fieldNames) {
            String fieldValue = vnp_Params.get(fieldName);
            if (fieldValue != null && !fieldValue.isEmpty()) {
                //Build hash data
                hashData.add(fieldName + "=" + URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                //Build query
                query.add(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII) + "=" + URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = VNPayConfig.vnp_PayUrl + "?" + queryUrl;

        return VNPayResponse.vnPayResponse(paymentUrl, "Tạo mã thanh toán cho nhiều đơn hàng thành công.", "Success");
    }

    @Override
    public VNPayResponse createPaymentByVNPayWithMultipleOrderForWeb(List<UUID> orderIds, String ipAddress, String username) {
        Long amount  = 0L;
        for (UUID orderId : orderIds) {
            amount += orderService.getTotalPaymentByOrderId(orderId, username);
        }
        String vnp_TxnRef = orderIds.stream().map(UUID::toString).reduce((s1, s2) -> s1 + "," + s2).orElse("");
        Map<String, String> vnp_Params = getVNPayParams(amount, vnp_TxnRef, ipAddress, VNPayConfig.return_url_for_web);
        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringJoiner hashData = new StringJoiner("&");
        StringJoiner query = new StringJoiner("&");
        for (String fieldName : fieldNames) {
            String fieldValue = vnp_Params.get(fieldName);
            if (fieldValue != null && !fieldValue.isEmpty()) {
                //Build hash data
                hashData.add(fieldName + "=" + URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                //Build query
                query.add(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII) + "=" + URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = VNPayConfig.vnp_PayUrl + "?" + queryUrl;

        return VNPayResponse.vnPayResponse(paymentUrl, "Tạo mã thanh toán cho nhiều đơn hàng thành công.", "Success");
    }


    @Override
    public VNPayDTO checkPaymentByVNPay(String vnp_TxnRef, String ipAddress, HttpServletRequest req) throws Exception {
        JsonObject vnpParams = getVNPayParams(vnp_TxnRef, ipAddress);

        return sendPostRequest(VNPayConfig.vnp_ApiUrl, vnpParams);
    }


    private VNPayDTO sendPostRequest(String apiUrl, JsonObject data) throws IOException {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(apiUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(HttpMethod.POST.name());
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream();
                 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8))) {
                writer.write(data.toString());
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    ObjectMapper mapper = new ObjectMapper();
                    return mapper.readValue(response.toString(), VNPayDTO.class);
                }
            } else {
                throw new IOException("HTTP error code: " + responseCode);
            }
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private JsonObject getVNPayParams(String vnp_TxnRef, String ipAddress) {
        String vnp_RequestId = VNPayConfig.getRandomNumber(8);
        String vnp_Version = "2.1.0";
        String vnp_Command = "querydr";
        String vnp_TmnCode = VNPayConfig.vnp_TmnCode;
        String vnp_OrderInfo = "Kiem tra ket qua GD OrderId:" + vnp_TxnRef;
        String vnp_TransDate = getVNPayCreateDate();

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());

        String vnp_IpAddr = ipAddress;

        JsonObject vnp_Params = new JsonObject();

        vnp_Params.addProperty("vnp_RequestId", vnp_RequestId);
        vnp_Params.addProperty("vnp_Version", vnp_Version);
        vnp_Params.addProperty("vnp_Command", vnp_Command);
        vnp_Params.addProperty("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.addProperty("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.addProperty("vnp_OrderInfo", vnp_OrderInfo);
        vnp_Params.addProperty("vnp_TransactionDate", vnp_TransDate);
        vnp_Params.addProperty("vnp_CreateDate", vnp_CreateDate);
        vnp_Params.addProperty("vnp_IpAddr", vnp_IpAddr);

        String hash_Data = String.join("|", vnp_RequestId, vnp_Version, vnp_Command, vnp_TmnCode, vnp_TxnRef, vnp_TransDate, vnp_CreateDate, vnp_IpAddr, vnp_OrderInfo);
        String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.secretKey, hash_Data);

        vnp_Params.addProperty("vnp_SecureHash", vnp_SecureHash);

        return vnp_Params;
    }


    private Map<String, String> getVNPayParams(Long amount, String vnp_TxnRef, String vnp_IpAddr, String vnp_ReturnUrl) {
        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", VNPayConfig.vnp_Version);
        vnp_Params.put("vnp_Command", VNPayConfig.vnp_Command);
        vnp_Params.put("vnp_TmnCode", VNPayConfig.vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount*100));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_BankCode", "NCB");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
        vnp_Params.put("vnp_OrderType", "other");
        vnp_Params.put("vnp_Locale", "vn");
//        vnp_Params.put("vnp_ReturnUrl", VNPayConfig.vnp_ReturnUrl);
        vnp_Params.put("vnp_ReturnUrl", vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
        vnp_Params.put("vnp_CreateDate", getVNPayCreateDate());
        vnp_Params.put("vnp_ExpireDate", getVNPayExpireDate());

        return vnp_Params;
    }


    private String getVNPayCreateDate() {
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        return formatter.format(cld.getTime());
    }


    private String getVNPayExpireDate() {
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        cld.add(Calendar.MINUTE, 15);
        return formatter.format(cld.getTime());
    }

}
