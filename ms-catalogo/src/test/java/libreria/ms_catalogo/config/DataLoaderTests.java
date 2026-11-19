package libreria.ms_catalogo.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.CommandLineRunner;

import libreria.ms_catalogo.model.Libro;
import libreria.ms_catalogo.repository.LibroRepository;

class DataLoaderTests {

    @Test
    void importaLosVeintidosLibrosYActualizaLosQueYaExistian() throws Exception {
        LibroRepository repository = mock(LibroRepository.class);
        Libro existing = new Libro(
            "Cien años de soledad",
            "Autor anterior",
            "$1",
            "Anterior",
            1,
            "anterior.jpg"
        );
        existing.setId(5L);
        when(repository.count()).thenReturn(3L);
        when(repository.findFirstByTituloIgnoreCase(anyString())).thenAnswer(invocation ->
            "Cien años de soledad".equals(invocation.getArgument(0))
                ? Optional.of(existing)
                : Optional.empty()
        );

        CommandLineRunner loader = new DataLoader().initDatabase(repository, true);
        loader.run();

        ArgumentCaptor<Libro> captor = ArgumentCaptor.forClass(Libro.class);
        verify(repository, times(22)).save(captor.capture());
        List<Libro> imported = captor.getAllValues();
        assertThat(imported).hasSize(22);
        assertThat(existing.getId()).isEqualTo(5L);
        assertThat(existing.getPrecio()).isEqualTo("$18.990");
        assertThat(existing.getDescripcion()).isNotBlank();
        assertThat(imported.stream().filter(Libro::isNovedad).map(Libro::getTitulo).toList())
            .containsExactlyInAnyOrder(
                "La Vegetariana",
                "Del amor y otros demonios",
                "Kentukis",
                "La Única Historia"
            );
    }

    @Test
    void noReimportaElCatalogoEnCadaInicioConLaBaseYaPoblada() throws Exception {
        LibroRepository repository = mock(LibroRepository.class);
        when(repository.count()).thenReturn(3L);

        CommandLineRunner loader = new DataLoader().initDatabase(repository, false);
        loader.run();

        verify(repository, never()).findFirstByTituloIgnoreCase(anyString());
        verify(repository, never()).save(org.mockito.ArgumentMatchers.any(Libro.class));
    }
}
