package libreria.ms_usuarios.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import libreria.ms_usuarios.model.Usuario;
import libreria.ms_usuarios.repository.UsuarioRepository;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner initDatabase(UsuarioRepository repository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (repository.count() == 0) {
                repository.save(new Usuario(
                    "Administradora",
                    "admin@parrafosegundo.cl",
                    passwordEncoder.encode("admin123"),
                    "ADMIN"
                ));
                repository.save(new Usuario(
                    "Cliente Prueba",
                    "cliente@parrafosegundo.cl",
                    passwordEncoder.encode("cliente123"),
                    "CLIENTE"
                ));
            }
        };
    }
}