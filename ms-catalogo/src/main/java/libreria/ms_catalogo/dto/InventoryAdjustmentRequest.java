package libreria.ms_catalogo.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record InventoryAdjustmentRequest(@NotEmpty List<@Valid Item> items) {

    public record Item(
        @NotBlank String titulo,
        @NotNull @Min(1) Integer cantidad
    ) {}
}
