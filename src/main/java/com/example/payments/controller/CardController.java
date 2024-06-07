package com.example.payments.controller;

import com.example.payments.entity.Card;
import com.example.payments.entity.Payment;
import com.example.payments.security.JwtConfig;
import com.example.payments.service.CardService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cards")
@CrossOrigin(origins = "http://localhost:4200")
public class CardController {
    private CardService cardService;

    private final JwtConfig jwtConfig;

    @Autowired
    public CardController(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    @Autowired
    public void setCardService(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping
    public ResponseEntity<?> getAllCards(@RequestHeader("Authorization") String authorizationHeader) {
        try{
            String token = authorizationHeader.replace("Bearer ", "");

            Claims claims = Jwts.parser().setSigningKey(jwtConfig.getSecretKey()).parseClaimsJws(token).getBody();
            Long userId = claims.get("userId", Long.class);

            if(userId == null) throw new RuntimeException("Invalid user");

            List<Card> payments = cardService.listAllCards(userId);
            return new ResponseEntity<>(payments, HttpStatus.OK);
        } catch (RuntimeException exception) {
            return ResponseEntity
                    .badRequest()
                    .body("Unauthorized");

        }
    }

    @PostMapping
    public ResponseEntity<?> createCard(@RequestBody Card card, @RequestHeader("Authorization") String authorizationHeader) {
        try{
            String token = authorizationHeader.replace("Bearer ", "");

            Claims claims = Jwts.parser().setSigningKey(jwtConfig.getSecretKey()).parseClaimsJws(token).getBody();
            Long userId = claims.get("userId", Long.class);

            if(userId == null) throw new RuntimeException("Invalid user");

            try {
                card.setSavedByUserId(userId);
                cardService.addCard(card);
                return new ResponseEntity<>(HttpStatus.CREATED);
            }
            catch (RuntimeException exception) {
                return ResponseEntity
                        .badRequest()
                        .build();
            }
        } catch (RuntimeException exception) {
            return ResponseEntity
                    .badRequest()
                    .body("Unauthorized");

        }
    }

    @DeleteMapping("/{id}" )
    public ResponseEntity<?> deleteCard(@PathVariable Long id, @RequestHeader("Authorization") String authorizationHeader) {
        try{
            String token = authorizationHeader.replace("Bearer ", "");

            Claims claims = Jwts.parser().setSigningKey(jwtConfig.getSecretKey()).parseClaimsJws(token).getBody();
            Long userId = claims.get("userId", Long.class);

            if(userId == null) throw new RuntimeException("Invalid user");

            cardService.removeCard(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException exception) {
            return ResponseEntity
                    .badRequest()
                    .body("Unauthorized");

        }
    }
}
