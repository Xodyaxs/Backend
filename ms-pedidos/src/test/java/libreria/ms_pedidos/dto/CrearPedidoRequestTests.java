package libreria.ms_pedidos.dto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

class CrearPedidoRequestTests {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void convierteElFormatoDelFrontendEnUnPedidoPersistible() throws Exception {
        String json = """
            {
              "cliente": {"nombre": "pepito", "email": "pepiton@gmail.com"},
              "nombre": "pepito",
              "email": "pepiton@gmail.com",
              "direccion": "psje petén 953",
              "ciudad": "Viña del Mar",
              "region": "Valparaíso",
              "fecha": "19 de noviembre de 2026",
              "items": [
                {"titulo": "Cien años de soledad", "cantidad": 1, "precio": "$18.990"}
              ],
              "metodoPago": "tarjeta",
              "total": "$18.990"
            }
            """;

        CrearPedidoRequest request = objectMapper.readValue(json, CrearPedidoRequest.class);
        var pedido = request.toPedido();

        assertThat(pedido.getClienteEmail()).isEqualTo("pepiton@gmail.com");
        assertThat(pedido.getNombreCliente()).isEqualTo("pepito");
        assertThat(pedido.getDireccion()).isEqualTo("psje petén 953");
        assertThat(pedido.getCiudad()).isEqualTo("Viña del Mar");
        assertThat(pedido.getRegion()).isEqualTo("Valparaíso");
        assertThat(pedido.getMetodoPago()).isEqualTo("tarjeta");
        assertThat(pedido.getDetalleProductos()).contains("Cien años de soledad x1 ($18.990)");
        assertThat(pedido.getTotal()).isEqualTo(18990);
        assertThat(pedido.getFecha()).isNotNull();
    }

    @Test
    void mantieneCompatibilidadConElFormatoAnterior() throws Exception {
        CrearPedidoRequest request = objectMapper.readValue(
            """
                {
                  "clienteEmail": "cliente@example.com",
                  "detalleProductos": "Libro x1",
                  "total": 18990
                }
                """,
            CrearPedidoRequest.class
        );

        var pedido = request.toPedido();

        assertThat(pedido.getClienteEmail()).isEqualTo("cliente@example.com");
        assertThat(pedido.getDetalleProductos()).isEqualTo("Libro x1");
        assertThat(pedido.getTotal()).isEqualTo(18990);
    }

    @Test
    void rechazaTotalConFormatoInvalido() throws Exception {
        CrearPedidoRequest request = objectMapper.readValue(
            """
                {
                  "cliente": {"email": "cliente@example.com"},
                  "items": [{"titulo": "Libro", "cantidad": 1}],
                  "total": "$abc"
                }
                """,
            CrearPedidoRequest.class
        );

        assertThatThrownBy(request::toPedido)
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("formato válido");
    }
}
