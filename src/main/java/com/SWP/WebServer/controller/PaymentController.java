package com.SWP.WebServer.controller;

import com.SWP.WebServer.dto.PaymentDTO;
import com.SWP.WebServer.entity.HistoryPayment;
import com.SWP.WebServer.entity.User;
import com.SWP.WebServer.exception.ApiRequestException;
import com.SWP.WebServer.service.HistoryPaymentService;
import com.SWP.WebServer.service.PaymentService;
import com.SWP.WebServer.service.UserService;
import com.SWP.WebServer.token.JwtTokenProvider;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@RestController
@RequestMapping("/payment")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private HistoryPaymentService historyPaymentService;

    @PostMapping("/pay")
    public ResponseEntity<Map<String, String>> processPayment(@RequestBody PaymentDTO body,
                                                              HttpServletRequest req) throws IOException {

        String amount = body.getAmount();
        String bankCode = body.getBankCode();
        String language = body.getLanguage();
        String currency = body.getCurrency();
        if (Objects.equals(currency, "USD")) {
            float amountFloat = Float.parseFloat(amount);
            int amountInt = (int) (amountFloat * 25455);
            amount = String.valueOf(amountInt);
            currency = "VND";
            language = "en";
        }

        String paymentUrl = paymentService.getRequest(amount, bankCode, language, currency, req);
        Map<String, String> response = new HashMap<>();
        response.put("code", "00");
        response.put("message", "success");
        response.put("data", paymentUrl);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/return")
    public String validateVnpResponse(@RequestBody Map<String, String> fields, @RequestHeader("Authorization") String token) {
        String userId = "";
        try {
            userId = jwtTokenProvider.verifyToken(token);
        } catch (ExpiredJwtException e) {
            throw new ApiRequestException("expired_session", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        String vnpSecureHash = fields.get("vnp_SecureHash");
        boolean isValid = paymentService.checkVnpSecureHash(fields, vnpSecureHash);

        if (isValid) {
            if (fields.get("vnp_ResponseCode").equals("00")) {
                User user = userService.findUserByUserID(userId);
                float amount = Float.parseFloat(fields.get("vnp_Amount")) / 100 / 25455;
                user.setAccount_balance(user.getAccount_balance() + amount);
                userService.saveUser(user);
                HistoryPayment historyPayment = new HistoryPayment();
                historyPayment.setAmount(amount);
                historyPayment.setStatus("success");
                historyPayment.setType("deposit");
                historyPayment.setDescription("Deposit money to account");
                historyPayment.setUser(user);
                historyPaymentService.saveHistoryPayment(historyPayment);
                return "success";
            } else {
                return "failed";
            }
        } else {
            return "failed";
        }
    }

    @GetMapping("/history")
    public ResponseEntity<?> getHistoryPayment(@RequestHeader("Authorization") String token) {
        String userId = jwtTokenProvider.verifyToken(token);
        List<HistoryPayment> historyPayments = historyPaymentService.getHistoryPayment(Integer.parseInt(userId));
        return ResponseEntity.ok(historyPayments);
    }

    @PostMapping("/buy")
    public ResponseEntity<?> buyPackage(@RequestParam("amount") String price,  // Đổi @RequestAttribute thành @RequestParam
                                        @RequestHeader("Authorization") String token) {
        String userId = jwtTokenProvider.verifyToken(token);
        User user = userService.findUserByUserID(userId);
        int priceInt = Integer.parseInt(price);
        if (user.getAccount_balance() < priceInt) {
            throw new ApiRequestException("Not enough money", HttpStatus.BAD_REQUEST);
        }
        user.setAccount_balance(user.getAccount_balance() - priceInt);
        userService.saveUser(user);
        HistoryPayment historyPayment = new HistoryPayment();
        historyPayment.setAmount(priceInt);
        historyPayment.setStatus("success");
        historyPayment.setType("buy");
        historyPayment.setDescription("Buy template");
        historyPayment.setUser(user);
        historyPaymentService.saveHistoryPayment(historyPayment);
        return ResponseEntity.ok("success");
    }

}
