package libreria.ms_catalogo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import libreria.ms_catalogo.model.Libro;
import libreria.ms_catalogo.repository.LibroRepository;

@RestController
@RequestMapping("/api/libros")
public class LibroController {

    @Autowired
    private LibroRepository libroRepository;

    @GetMapping
    public List<Libro> listarLibros() {
        return libroRepository.findAll();
    }

    @PostMapping
    public Libro agregarLibro(@RequestBody Libro nuevoLibro) {
        return libroRepository.save(nuevoLibro);
    }

    @PutMapping("/{id}/stock")
    public ResponseEntity<Libro> actualizarStock(@PathVariable Long id, @RequestBody Map<String, Integer> body) {
        return libroRepository.findById(id).map(libro -> {
            libro.setStock(body.get("stock"));
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