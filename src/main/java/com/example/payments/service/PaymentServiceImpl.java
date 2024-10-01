package com.example.payments.service;

import com.example.payments.entity.Payment;
import com.example.payments.repository.PaymentRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService{
    @Autowired
    private PaymentRepo paymentRepository;

    private final WebClient webClient;
    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);


    @Autowired
    public PaymentServiceImpl(PaymentRepo paymentRepository, WebClient.Builder webClientBuilder) {
        this.paymentRepository = paymentRepository;
        this.webClient = webClientBuilder.baseUrl("http://localhost:5050/api/tokens").build(); // URL of the token vault service
    }

    @Override
    public List<Payment> listAllPayments(boolean isUser, Long userId) {
        if (isUser)
            return paymentRepository.findByPayerUserIdOrderByDateAndTimeDesc(userId)/*.stream().map(this::addMaskedCardNumber).collect(Collectors.toList())*/;
        else return paymentRepository.findAllByOrderByDateAndTimeDesc();
    }

    @Override
    public Payment findPayment(Long id) {
        return paymentRepository.findById(id)/*.map(this::addMaskedCardNumber)*/.orElse(null);
                //.orElseThrow(() -> new EntityNotFoundException("Payment not found with id: " + id));
    }

    @Override
    public void addPayment(Payment payment) throws Exception {
        /*String token = tokenizeCard(payment.getCardNumber());
        System.out.println(token);
        payment.setCardNumber(token);*/
        // Save the payment first to get the payment ID
        payment.setInvoiceId(generateInvoiceId());
        paymentRepository.save(payment);
    }

    /*private Payment addMaskedCardNumber(Payment payment) {
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
    }*/

    private String generateInvoiceId() {
        return "INV" + UUID.randomUUID().toString().replaceAll("-", "").substring(0, 8).toUpperCase();
    }

    private byte[] generatePdfInvoice(Payment payment) {
        Document document = new Document();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, byteArrayOutputStream);
            document.open();
            document.add(new Paragraph("Invoice"));
            document.add(new Paragraph("Invoice ID: " + payment.getInvoiceId()));
            document.add(new Paragraph("Payment ID: " + payment.getPayment_id()));
            document.add(new Paragraph("Amount: " + payment.getAmount()));
            document.add(new Paragraph("Description: " + payment.getDescription()));
            document.add(new Paragraph("Customer ID: " + payment.getPayerUserId()));
            document.add(new Paragraph("Payment Date: " + payment.getDateAndTime()));
            document.close();
        } catch (DocumentException e) {
            logger.error("Error generating PDF", e);
        }

        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public byte[] getInvoicePdf(String invoiceId) {
        Optional<Payment> optionalPayment = paymentRepository.findByInvoiceId(invoiceId);
        return optionalPayment.map(this::generatePdfInvoice).orElse(null);
    }

    /*private byte[] generatePdfInvoice(Long paymentId, BigDecimal amount, String description, Long customerId) {
        Document document = new Document();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, byteArrayOutputStream);
            document.open();
            document.add(new Paragraph("Invoice"));
            document.add(new Paragraph("Payment ID: " + paymentId));
            document.add(new Paragraph("Amount: " + amount));
            document.add(new Paragraph("Description: " + description));
            document.add(new Paragraph("Customer ID: " + customerId));
            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return byteArrayOutputStream.toByteArray();
    }

    public byte[] getInvoicePdf(String invoiceId) {
        Invoice invoice = invoiceRepository.findByInvoiceId(invoiceId);
        return invoice != null ? invoice.getPdf() : null;
    }*/
}
