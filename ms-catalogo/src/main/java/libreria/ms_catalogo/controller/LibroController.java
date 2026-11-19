package libreria.ms_catalogo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import libreria.ms_catalogo.model.Libro;
import libreria.ms_catalogo.repository.LibroRepository;

@RestController
@RequestMapping("/api/libros")
public class LibroController {

    private final LibroRepository libroRepository;

    public LibroController(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }

    @GetMapping
    public List<Libro> listarLibros() {
        return libroRepository.findAll();
    }

    @PostMapping
    public Libro agregarLibro(@Valid @RequestBody Libro nuevoLibro) {
        nuevoLibro.setId(null);
        return libroRepository.save(nuevoLibro);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Libro> actualizarLibro(
            @PathVariable Long id,
            @Valid @RequestBody Libro datosActualizados) {
        return libroRepository.findById(id).map(libro -> {
            libro.setTitulo(datosActualizados.getTitulo());
            libro.setAutor(datosActualizados.getAutor());
            libro.setPrecio(datosActualizados.getPrecio());
            libro.setDescripcion(datosActualizados.getDescripcion());
            libro.setCategoria(datosActualizados.getCategoria());
            libro.setStock(datosActualizados.getStock());
            libro.setImagen(datosActualizados.getImagen());
            libro.setNovedad(datosActualizados.isNovedad());
            return ResponseEntity.ok(libroRepository.save(libro));
        }).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/stock")
    public ResponseEntity<Libro> actualizarStock(@PathVariable Long id, @RequestBody Map<String, Integer> body) {
        Integer stock = body.get("stock");
        if (stock == null || stock < 0) {
            return ResponseEntity.badRequest().build();
        }
        return libroRepository.findById(id).map(libro -> {
            libro.setStock(stock);
            return ResponseEntity.ok(libroRepository.save(libro));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarLibro(@PathVariable Long id) {
        if (libroRepository.existsById(id)) {
            libroRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}