package libreria.ms_catalogo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import libreria.ms_catalogo.dto.InventoryAdjustmentRequest;
import libreria.ms_catalogo.model.Libro;
import libreria.ms_catalogo.repository.LibroRepository;

@Service
public class InventoryService {

    private final LibroRepository libroRepository;

    public InventoryService(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }

    @Transactional
    public void reservar(List<InventoryAdjustmentRequest.Item> items) {
        adjust(items, false);
    }

    @Transactional
    public void liberar(List<InventoryAdjustmentRequest.Item> items) {
        adjust(items, true);
    }

    private void adjust(List<InventoryAdjustmentRequest.Item> items, boolean release) {
        List<ReservationLine> lines = aggregate(items);
        List<Libro> books = new ArrayList<>(lines.size());

        for (ReservationLine line : lines) {
            Libro book = libroRepository.findFirstByTituloIgnoreCaseForUpdate(line.titulo())
                .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "No existe el libro: " + line.titulo()
                ));
            int currentStock = book.getStock() == null ? 0 : book.getStock();
            if (!release && currentStock < line.cantidad()) {
                throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Stock insuficiente para: " + line.titulo()
                );
            }
            if (release && currentStock > Integer.MAX_VALUE - line.cantidad()) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "El stock supera el máximo permitido");
            }
            books.add(book);
        }

        for (int index = 0; index < lines.size(); index++) {
            Libro book = books.get(index);
            int currentStock = book.getStock() == null ? 0 : book.getStock();
            int quantity = lines.get(index).cantidad();
            book.setStock(release ? currentStock + quantity : currentStock - quantity);
        }
        libroRepository.saveAll(books);
    }

    private static List<ReservationLine> aggregate(List<InventoryAdjustmentRequest.Item> items) {
        if (items == null || items.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El pedido no contiene productos");
        }

        Map<String, ReservationLine> aggregated = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        for (InventoryAdjustmentRequest.Item item : items) {
            if (item == null || item.titulo() == null || item.titulo().isBlank()
                    || item.cantidad() == null || item.cantidad() < 1) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Producto o cantidad inválidos");
            }

            String title = item.titulo().trim();
            ReservationLine current = aggregated.get(title);
            int quantity = item.cantidad();
            if (current != null) {
                if (current.cantidad() > Integer.MAX_VALUE - quantity) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cantidad solicitada fuera de rango");
                }
                quantity += current.cantidad();
                title = current.titulo();
            }
            aggregated.put(title, new ReservationLine(title, quantity));
        }
        return List.copyOf(aggregated.values());
    }

    private record ReservationLine(String titulo, int cantidad) {}
}
