package com.project.shopapp.controller;

import com.project.shopapp.helper.VnPayUtils;
import com.project.shopapp.models.PaymentRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("${api.prefix}/payment")
public class PaymentController {
    @PostMapping("/vn-pay")
    public Map<String, String> createVnPayPayment(@RequestBody PaymentRequest paymentRequest) throws Exception {
        String vnp_TmnCode = "BMJ2E0J0";
        String vnp_HashSecret = "EEBR3IOB4J3C8QYAG4816I8T3UDJLFQ4";
        String vnp_Url = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", "2.1.0");
        vnp_Params.put("vnp_Command", "pay");
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(paymentRequest.getAmount() * 100L));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", UUID.randomUUID().toString());
        vnp_Params.put("vnp_OrderInfo", paymentRequest.getOrderInfo());
        vnp_Params.put("vnp_OrderType", "Thanh toán bằng thẻ ");
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", paymentRequest.getReturnUrl());
        vnp_Params.put("vnp_IpAddr", "13.160.92.202");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String createDate = formatter.format(new Date());
        vnp_Params.put("vnp_CreateDate", createDate);

        Calendar expireDate = Calendar.getInstance();
        expireDate.add(Calendar.MINUTE, 15);
        String expireDateStr = formatter.format(expireDate.getTime());
        vnp_Params.put("vnp_ExpireDate", expireDateStr);

        String queryUrl = VnPayUtils.buildQueryString(vnp_Params);
        String vnpSecureHash = VnPayUtils.hmacSHA512(vnp_HashSecret, queryUrl);

        Map<String, String> response = new HashMap<>();
        response.put("paymentUrl", vnp_Url + "?" + queryUrl + "&vnp_SecureHash=" + vnpSecureHash);

        return response;
    }
}

