package libreria.ms_pedidos.dto;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;

import libreria.ms_pedidos.model.Pedido;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CrearPedidoRequest(
    ClienteRequest cliente,
    String nombre,
    String email,
    String clienteEmail,
    String direccion,
    String ciudad,
    String region,
    String metodoPago,
    JsonNode total,
    List<ItemRequest> items,
    String detalleProductos
) {

    public Pedido toPedido() {
        String customerEmail = firstNonBlank(
            cliente == null ? null : cliente.email(),
            email,
            clienteEmail
        );
        if (customerEmail == null) {
            throw new IllegalArgumentException("Falta el email del cliente");
        }

        String details = buildDetails();
        int totalInPesos = parseTotal(total);
        String customerName = firstNonBlank(
            cliente == null ? null : cliente.nombre(),
            nombre
        );

        Pedido pedido = new Pedido(customerEmail, details, totalInPesos);
        pedido.setNombreCliente(customerName);
        pedido.setDireccion(trimToNull(direccion));
        pedido.setCiudad(trimToNull(ciudad));
        pedido.setRegion(trimToNull(region));
        pedido.setMetodoPago(trimToNull(metodoPago));
        return pedido;
    }

    private String buildDetails() {
        if (items == null || items.isEmpty()) {
            String legacyDetails = trimToNull(detalleProductos);
            if (legacyDetails == null) {
                throw new IllegalArgumentException("El pedido debe incluir al menos un producto");
            }
            if (legacyDetails.length() > 1000) {
                throw new IllegalArgumentException("El detalle de productos supera el máximo permitido");
            }
            return legacyDetails;
        }

        String details = items.stream()
            .map(item -> {
                if (item == null || trimToNull(item.titulo()) == null) {
                    throw new IllegalArgumentException("Cada producto debe tener un título");
                }
                if (item.cantidad() == null || item.cantidad() < 1) {
                    throw new IllegalArgumentException("La cantidad de cada producto debe ser mayor que cero");
                }
                String price = item.precio() == null || item.precio().isNull()
                    ? ""
                    : " (" + item.precio().asText() + ")";
                return item.titulo().trim() + " x" + item.cantidad() + price;
            })
            .collect(Collectors.joining(", "));

        if (details.length() > 1000) {
            throw new IllegalArgumentException("El detalle de productos supera el máximo permitido");
        }
        return details;
    }

    private static int parseTotal(JsonNode value) {
        if (value == null || value.isNull()) {
            throw new IllegalArgumentException("Falta el total del pedido");
        }

        BigDecimal amount;
        if (value.isNumber()) {
            amount = value.decimalValue();
        } else if (value.isTextual()) {
            String normalized = value.asText()
                .trim()
                .replaceAll("(?i)CLP", "")
                .replace("$", "")
                .replace("\u00a0", "")
                .replaceAll("\\s", "");
            ParsePosition position = new ParsePosition(0);
            Number parsed = NumberFormat.getNumberInstance(Locale.forLanguageTag("es-CL"))
                .parse(normalized, position);
            if (parsed == null || position.getIndex() != normalized.length()) {
                throw new IllegalArgumentException("El total del pedido no tiene un formato válido");
            }
            amount = new BigDecimal(parsed.toString());
        } else {
            throw new IllegalArgumentException("El total del pedido debe ser un número o texto de moneda");
        }

        if (amount.signum() < 0) {
            throw new IllegalArgumentException("El total del pedido no puede ser negativo");
        }
        try {
            return amount.intValueExact();
        } catch (ArithmeticException exception) {
            throw new IllegalArgumentException("El total del pedido debe ser un entero dentro del rango permitido");
        }
    }

    private static String firstNonBlank(String... values) {
        for (String value : values) {
            String normalized = trimToNull(value);
            if (normalized != null) {
                return normalized;
            }
        }
        return null;
    }

    private static String trimToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ClienteRequest(String nombre, String email) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ItemRequest(String titulo, Integer cantidad, JsonNode precio) {}
}
