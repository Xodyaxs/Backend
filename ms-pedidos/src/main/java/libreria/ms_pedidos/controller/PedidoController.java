package libreria.ms_pedidos.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import libreria.ms_pedidos.dto.CrearPedidoRequest;
import libreria.ms_pedidos.model.Pedido;
import libreria.ms_pedidos.repository.PedidoRepository;
import libreria.ms_pedidos.service.CatalogInventoryClient;
import libreria.ms_pedidos.service.InventoryClientException;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PedidoController.class);

    private final PedidoRepository pedidoRepository;
    private final CatalogInventoryClient catalogInventoryClient;

    public PedidoController(
            PedidoRepository pedidoRepository,
            CatalogInventoryClient catalogInventoryClient) {
        this.pedidoRepository = pedidoRepository;
        this.catalogInventoryClient = catalogInventoryClient;
    }

    @GetMapping
    public List<Pedido> listarPedidos() {
        return pedidoRepository.findAll();
    }

    @GetMapping("/cliente/{email}")
    public List<Pedido> pedidosPorCliente(@PathVariable String email) {
        return pedidoRepository.findByClienteEmail(email);
    }

    @PostMapping
    public ResponseEntity<?> crearPedido(@RequestBody CrearPedidoRequest request) {
        if (request == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "El cuerpo del pedido es obligatorio"));
        }
        try {
            if (request.items() == null || request.items().isEmpty()) {
                throw new IllegalArgumentException("El pedido debe incluir productos con título y cantidad");
            }
            Pedido pedido = request.toPedido();
            catalogInventoryClient.reservar(request.items());
            try {
                return ResponseEntity.ok(pedidoRepository.save(pedido));
            } catch (DataAccessException saveFailure) {
                try {
                    catalogInventoryClient.liberar(request.items());
                } catch (InventoryClientException compensationFailure) {
                    LOGGER.error("No se pudo restaurar el inventario tras fallar el guardado del pedido", compensationFailure);
                    saveFailure.addSuppressed(compensationFailure);
                }
                throw saveFailure;
            }
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", exception.getMessage()));
        } catch (InventoryClientException exception) {
            return ResponseEntity.status(exception.getStatusCode())
                .body(Map.of("error", exception.getMessage()));
        }
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<Pedido> actualizarEstado(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return pedidoRepository.findById(id).map(pedido -> {
            pedido.setEstado(body.get("estado"));
            return ResponseEntity.ok(pedidoRepository.save(pedido));
        }).orElse(ResponseEntity.notFound().build());
    }
}