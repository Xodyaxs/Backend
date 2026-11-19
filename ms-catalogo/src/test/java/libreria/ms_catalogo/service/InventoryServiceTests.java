package libreria.ms_catalogo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import libreria.ms_catalogo.dto.InventoryAdjustmentRequest;
import libreria.ms_catalogo.model.Libro;
import libreria.ms_catalogo.repository.LibroRepository;

class InventoryServiceTests {

    @Test
    void descuentaLasUnidadesReservadas() {
        LibroRepository repository = mock(LibroRepository.class);
        Libro book = new Libro("Libro", "Autora", "$10.000", "Novela", 5, "libro.jpg");
        when(repository.findFirstByTituloIgnoreCaseForUpdate("Libro")).thenReturn(Optional.of(book));

        new InventoryService(repository).reservar(List.of(new InventoryAdjustmentRequest.Item("Libro", 2)));

        assertThat(book.getStock()).isEqualTo(3);
        verify(repository).saveAll(anyList());
    }

    @Test
    void noModificaNingunStockSiUnProductoNoTieneUnidadesSuficientes() {
        LibroRepository repository = mock(LibroRepository.class);
        Libro availableBook = new Libro("Libro A", "Autora", "$10.000", "Novela", 5, "a.jpg");
        Libro unavailableBook = new Libro("Libro B", "Autor", "$12.000", "Novela", 1, "b.jpg");
        when(repository.findFirstByTituloIgnoreCaseForUpdate("Libro A"))
            .thenReturn(Optional.of(availableBook));
        when(repository.findFirstByTituloIgnoreCaseForUpdate("Libro B"))
            .thenReturn(Optional.of(unavailableBook));

        assertThatThrownBy(() -> new InventoryService(repository).reservar(List.of(
            new InventoryAdjustmentRequest.Item("Libro A", 2),
            new InventoryAdjustmentRequest.Item("Libro B", 2)
        )))
            .isInstanceOf(ResponseStatusException.class)
            .hasMessageContaining("Stock insuficiente");

        assertThat(availableBook.getStock()).isEqualTo(5);
        assertThat(unavailableBook.getStock()).isEqualTo(1);
        verify(repository, never()).saveAll(anyList());
    }
}
