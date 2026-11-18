package libreria.ms_catalogo.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import libreria.ms_catalogo.model.Libro;
import libreria.ms_catalogo.repository.LibroRepository;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner initDatabase(LibroRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                repository.save(new Libro("La única historia", "Julian Barnes", "$16.990", "Novela", 10, "https://images.unsplash.com/photo-1544947950-fa07a98d237f?auto=format&fit=crop&w=400&q=80"));
                repository.save(new Libro("Blanco", "Han Kang", "$8.000", "Poesía / Ensayo", 8, "https://images.unsplash.com/photo-1512820790803-83ca734da794?auto=format&fit=crop&w=400&q=80"));
                repository.save(new Libro("La vegetariana", "Han Kang", "$14.500", "Novela", 12, "https://images.unsplash.com/photo-1543002588-bfa74002ed7e?auto=format&fit=crop&w=400&q=80"));
            }
        };
    }
}