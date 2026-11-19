package libreria.ms_catalogo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import libreria.ms_catalogo.dto.InventoryAdjustmentRequest;
import libreria.ms_catalogo.service.InventoryService;

@RestController
@RequestMapping("/internal/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping("/reserve")
    public ResponseEntity<Void> reserve(@Valid @RequestBody InventoryAdjustmentRequest request) {
        inventoryService.reservar(request.items());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/release")
    public ResponseEntity<Void> release(@Valid @RequestBody InventoryAdjustmentRequest request) {
        inventoryService.liberar(request.items());
        return ResponseEntity.noContent().build();
    }
}
