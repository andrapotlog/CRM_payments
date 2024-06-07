package com.example.payments.controller;

import com.example.payments.entity.Template;
import com.example.payments.security.JwtConfig;
import com.example.payments.service.TemplateService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/templates")
@CrossOrigin(origins = "http://localhost:4200")
public class TemplateController {
    private TemplateService templateService;

    private final JwtConfig jwtConfig;

    @Autowired
    public TemplateController(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    @Autowired
    public void setTemplateService(TemplateService templateService) {
        this.templateService = templateService;
    }

    @GetMapping
    public ResponseEntity<?> getAllTemplates(@RequestHeader("Authorization") String authorizationHeader) {
        try{
            String token = authorizationHeader.replace("Bearer ", "");

            Claims claims = Jwts.parser().setSigningKey(jwtConfig.getSecretKey()).parseClaimsJws(token).getBody();
            Long userId = claims.get("userId", Long.class);

            if(userId == null) throw new RuntimeException("Invalid user");

            List<Template> templates = templateService.listTemplates(userId);
            return new ResponseEntity<>(templates, HttpStatus.OK);
        } catch (RuntimeException exception) {
            return ResponseEntity
                    .badRequest()
                    .body("Unauthorized");

        }
    }

    @PostMapping
    public ResponseEntity<?> createTemplate(@RequestBody Template template, @RequestHeader("Authorization") String authorizationHeader) {
        try{
            String token = authorizationHeader.replace("Bearer ", "");

            Claims claims = Jwts.parser().setSigningKey(jwtConfig.getSecretKey()).parseClaimsJws(token).getBody();
            Long userId = claims.get("userId", Long.class);

            if(userId == null) throw new RuntimeException("Invalid user");

            try {
                template.setCreatedByUserId(userId);
                templateService.addTemplate(template);
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

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTemplate(@PathVariable Long id, @RequestHeader("Authorization") String authorizationHeader) {
        try{
            String token = authorizationHeader.replace("Bearer ", "");

            Claims claims = Jwts.parser().setSigningKey(jwtConfig.getSecretKey()).parseClaimsJws(token).getBody();
            Long userId = claims.get("userId", Long.class);

            if(userId == null) throw new RuntimeException("Invalid user");

            templateService.removeTemplate(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException exception) {
            return ResponseEntity
                    .badRequest()
                    .body("Unauthorized");

        }
    }
}
