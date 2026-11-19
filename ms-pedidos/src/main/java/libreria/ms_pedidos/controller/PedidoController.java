package libreria.ms_pedidos.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    @Autowired
    private PedidoRepository pedidoRepository;

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
            return ResponseEntity.ok(pedidoRepository.save(request.toPedido()));
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
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