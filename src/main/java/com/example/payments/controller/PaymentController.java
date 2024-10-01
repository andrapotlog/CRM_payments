package com.example.payments.controller;

import com.example.payments.entity.Payment;
import com.example.payments.entity.Template;
import com.example.payments.security.JwtConfig;
import com.example.payments.service.PaymentService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "http://localhost:4200")
public class PaymentController {
    private PaymentService paymentService;

    private final JwtConfig jwtConfig;

    @Autowired
    public PaymentController(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    @Autowired
    public void setPaymentService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping
    public ResponseEntity<?> getAllPayments(@RequestHeader("Authorization") String authorizationHeader) {
        try{
            String token = authorizationHeader.replace("Bearer ", "");

            Claims claims = Jwts.parser().setSigningKey(jwtConfig.getSecretKey()).parseClaimsJws(token).getBody();
            Long userId = claims.get("userId", Long.class);
            List<String> userRoles = claims.get("roles", List.class);

            if(userId == null) return new ResponseEntity<>("Invalid user", HttpStatus.BAD_REQUEST);
            if(userRoles == null) return new ResponseEntity<>("Invalid roles", HttpStatus.BAD_REQUEST);

            List<Payment> payments = paymentService.listAllPayments(userRoles.contains("ROLE_USER"),userId);
            return new ResponseEntity<>(payments, HttpStatus.OK);
        } catch (RuntimeException exception) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);

        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPaymentById(@PathVariable Long id, @RequestHeader("Authorization") String authorizationHeader) {
        try{
            String token = authorizationHeader.replace("Bearer ", "");

            Claims claims = Jwts.parser().setSigningKey(jwtConfig.getSecretKey()).parseClaimsJws(token).getBody();
            Long userId = claims.get("userId", Long.class);

            if(userId == null) throw new RuntimeException("Invalid user");

            Payment payment = paymentService.findPayment(id);
            return ResponseEntity.ok(payment);
        } catch (RuntimeException exception) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);

        }
    }

    @GetMapping("/invoice/{invoiceId}")
    public ResponseEntity<byte[]> getInvoicePdf(@PathVariable String invoiceId) {
        byte[] pdf = paymentService.getInvoicePdf(invoiceId);
        if (pdf != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("inline", "invoice.pdf");
            return ResponseEntity.ok().headers(headers).body(pdf);
        } else {
            return ResponseEntity.status(404).body(null);
        }
    }

    @PostMapping
    public ResponseEntity<?> createPayment(@RequestBody Payment payment, @RequestHeader("Authorization") String authorizationHeader) {
        try{
            String token = authorizationHeader.replace("Bearer ", "");

            Claims claims = Jwts.parser().setSigningKey(jwtConfig.getSecretKey()).parseClaimsJws(token).getBody();
            Long userId = claims.get("userId", Long.class);

            if(userId == null) throw new RuntimeException("Invalid user");

            try {
                payment.setPayerUserId(userId);
                payment.setInvoiceId("ABC123456");
                payment.setCardType("VISA");
                paymentService.addPayment(payment);
                return new ResponseEntity<>(HttpStatus.CREATED);
            }
            catch (RuntimeException exception) {
                return ResponseEntity
                        .badRequest()
                        .build();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } catch (RuntimeException exception) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
    }
}
