package com.example.payments.service;

import com.example.payments.entity.Payment;
import com.example.payments.repository.PaymentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImpl implements PaymentService{
    @Autowired
    private PaymentRepo paymentRepository;
    private final WebClient webClient;

    @Autowired
    public PaymentServiceImpl(PaymentRepo paymentRepository, WebClient.Builder webClientBuilder) {
        this.paymentRepository = paymentRepository;
        this.webClient = webClientBuilder.baseUrl("http://localhost:5050/api/tokens").build(); // URL of the token vault service
    }

    @Override
    public List<Payment> listAllPayments(Long userId) {
        return paymentRepository.findByPayerUserId(userId).stream().map(this::addMaskedCardNumber).collect(Collectors.toList());
    }

    @Override
    public Payment findPayment(Long id) {
        return paymentRepository.findById(id).map(this::addMaskedCardNumber).orElse(null);
                //.orElseThrow(() -> new EntityNotFoundException("Payment not found with id: " + id));
    }

    @Override
    public void addPayment(Payment payment) throws Exception {
        System.out.println("here 2");
        String token = tokenizeCard(payment.getCardNumber());
        System.out.println("here 3");
        System.out.println(token);
        payment.setCardNumber(token);
        paymentRepository.save(payment);
    }

    private Payment addMaskedCardNumber(Payment payment) {
        try {
            String originalCardNumber = detokenizeCard(payment.getCardNumber());
            System.out.println(originalCardNumber);
            payment.setCardNumber("**** **** **** " + originalCardNumber.substring(7));
        } catch (Exception e) {
            payment.setCardNumber("**** **** **** ****");
        }
        return payment;
    }

    private String tokenizeCard(String cardNumber) throws Exception {
        System.out.println("here tok");
        System.out.println(this.webClient.post()
                .uri("/tokenize")
                .bodyValue(cardNumber)
                );
        return this.webClient.post()
                .uri("/tokenize")
                .bodyValue(cardNumber)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public String detokenizeCard(String token) throws Exception {
        return this.webClient.post()
                .uri("/detokenize")
                .bodyValue(token)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
