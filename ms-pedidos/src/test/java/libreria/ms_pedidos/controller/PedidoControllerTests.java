package libreria.ms_pedidos.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import libreria.ms_pedidos.dto.CrearPedidoRequest;
import libreria.ms_pedidos.model.Pedido;
import libreria.ms_pedidos.repository.PedidoRepository;
import libreria.ms_pedidos.service.CatalogInventoryClient;
import libreria.ms_pedidos.service.InventoryClientException;

class PedidoControllerTests {

    @Test
    void reservaInventarioAntesDeGuardarElPedido() {
        PedidoRepository repository = mock(PedidoRepository.class);
        CatalogInventoryClient inventoryClient = mock(CatalogInventoryClient.class);
        when(repository.save(any(Pedido.class))).thenAnswer(invocation -> invocation.getArgument(0));
        PedidoController controller = new PedidoController(repository, inventoryClient);
        CrearPedidoRequest request = request();

        var response = controller.crearPedido(request);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        verify(inventoryClient).reservar(request.items());
        verify(repository).save(any(Pedido.class));
    }

    @Test
    void noGuardaElPedidoSiElCatalogoRechazaLaReserva() {
        PedidoRepository repository = mock(PedidoRepository.class);
        CatalogInventoryClient inventoryClient = mock(CatalogInventoryClient.class);
        doThrow(new InventoryClientException(HttpStatus.CONFLICT, "Stock insuficiente", null))
            .when(inventoryClient).reservar(any());
        PedidoController controller = new PedidoController(repository, inventoryClient);

        var response = controller.crearPedido(request());

        assertThat(response.getStatusCode().value()).isEqualTo(409);
        verify(repository, never()).save(any(Pedido.class));
    }

    private static CrearPedidoRequest request() {
        return new CrearPedidoRequest(
            new CrearPedidoRequest.ClienteRequest("Cliente", "cliente@example.com"),
            "Cliente",
            "cliente@example.com",
            null,
            "Calle 1",
            "Santiago",
            "Metropolitana",
            "2026-11-19",
            "tarjeta",
            "$18.990",
            List.of(new CrearPedidoRequest.ItemRequest("Libro", 1, "$18.990")),
            null
        );
    }
}
