package libreria.ms_catalogo.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import libreria.ms_catalogo.model.Libro;
import libreria.ms_catalogo.repository.LibroRepository;

class LibroControllerTests {

    @Test
    void actualizaTodosLosCamposSinCambiarElId() {
        LibroRepository repository = mock(LibroRepository.class);
        Libro existente = new Libro("Título anterior", "Autor anterior", "$1", "Drama", 1, "anterior.jpg");
        existente.setId(8L);
        Libro cambios = new Libro("Título nuevo", "Autor nuevo", "$2", "Historia", 3, "nuevo.jpg");
        cambios.setDescripcion("Descripción nueva");
        cambios.setNovedad(true);
        cambios.setId(99L);
        when(repository.findById(8L)).thenReturn(Optional.of(existente));
        when(repository.save(existente)).thenReturn(existente);

        var response = new LibroController(repository).actualizarLibro(8L, cambios);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isSameAs(existente);
        assertThat(response.getBody().getId()).isEqualTo(8L);
        assertThat(response.getBody().getTitulo()).isEqualTo("Título nuevo");
        assertThat(response.getBody().getAutor()).isEqualTo("Autor nuevo");
        assertThat(response.getBody().getPrecio()).isEqualTo("$2");
        assertThat(response.getBody().getDescripcion()).isEqualTo("Descripción nueva");
        assertThat(response.getBody().getCategoria()).isEqualTo("Historia");
        assertThat(response.getBody().getStock()).isEqualTo(3);
        assertThat(response.getBody().getImagen()).isEqualTo("nuevo.jpg");
        assertThat(response.getBody().isNovedad()).isTrue();
        verify(repository).save(existente);
    }

    @Test
    void devuelve404CuandoNoExisteElLibro() {
        LibroRepository repository = mock(LibroRepository.class);
        when(repository.findById(8L)).thenReturn(Optional.empty());

        var response = new LibroController(repository).actualizarLibro(
            8L,
            new Libro("Libro", "Autor", "$1", "Drama", 1, "libro.jpg")
        );

        assertThat(response.getStatusCode().value()).isEqualTo(404);
    }
}
