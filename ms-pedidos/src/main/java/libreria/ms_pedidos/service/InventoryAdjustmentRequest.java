package libreria.ms_pedidos.service;

import java.util.List;

public record InventoryAdjustmentRequest(List<Item> items) {

    public record Item(String titulo, Integer cantidad) {}
}
