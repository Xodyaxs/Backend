package libreria.ms_catalogo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import libreria.ms_catalogo.model.Libro;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {
    Optional<Libro> findFirstByTituloIgnoreCase(String titulo);
}