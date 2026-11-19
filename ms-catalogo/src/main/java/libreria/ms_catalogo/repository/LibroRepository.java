package libreria.ms_catalogo.repository;

import java.util.Optional;

import jakarta.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import libreria.ms_catalogo.model.Libro;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {
    Optional<Libro> findFirstByTituloIgnoreCase(String titulo);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select libro from Libro libro where lower(libro.titulo) = lower(:titulo)")
    Optional<Libro> findFirstByTituloIgnoreCaseForUpdate(@Param("titulo") String titulo);
}