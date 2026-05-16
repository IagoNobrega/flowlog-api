package com.iago.flowlog.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.iago.flowlog.dto.FocusRecordRequest;
import com.iago.flowlog.dto.ProductivityDiagnosisResponse;
import com.iago.flowlog.model.FocusSession;
import com.iago.flowlog.service.FocusService;

@RestController
@RequestMapping
public class FocusController {

    private final FocusService service;

    public FocusController(FocusService service) {
        this.service = service;
    }

    @PostMapping("/registro-foco")
    public ResponseEntity<FocusSession> registrarFoco(@Valid @RequestBody FocusRecordRequest request) {
        FocusSession saved = service.saveSession(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/diagnostico-produtividade")
    public ResponseEntity<ProductivityDiagnosisResponse> diagnosticoProdutividade() {
        ProductivityDiagnosisResponse response = service.diagnose();
        return ResponseEntity.ok(response);
    }
}
