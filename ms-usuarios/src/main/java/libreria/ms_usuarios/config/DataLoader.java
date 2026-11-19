package libreria.ms_usuarios.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import libreria.ms_usuarios.model.Usuario;
import libreria.ms_usuarios.repository.UsuarioRepository;

@Configuration
public class DataLoader {

    private static final String ADMIN_EMAIL = "parrafosegundo@admin.cl";

    @Bean
    CommandLineRunner initDatabase(
            UsuarioRepository repository,
            PasswordEncoder passwordEncoder,
            @Value("${ADMIN_INITIAL_PASSWORD:}") String initialAdminPassword) {
        return args -> {
            if (repository.count() == 0) {
                String adminEmail = initialAdminPassword.isBlank()
                    ? "admin@parrafosegundo.cl"
                    : ADMIN_EMAIL;
                String adminPassword = initialAdminPassword.isBlank()
                    ? "admin123"
                    : initialAdminPassword;
                repository.save(new Usuario(
                    "Administradora",
                    adminEmail,
                    passwordEncoder.encode(adminPassword),
                    "ADMIN"
                ));
                repository.save(new Usuario(
                    "Cliente Prueba",
                    "cliente@parrafosegundo.cl",
                    passwordEncoder.encode("cliente123"),
                    "CLIENTE"
                ));
                return;
            }

            if (!initialAdminPassword.isBlank()) {
                Usuario admin = repository.findByEmail(ADMIN_EMAIL)
                    .orElseGet(() -> new Usuario(
                        "Administradora",
                        ADMIN_EMAIL,
                        passwordEncoder.encode(initialAdminPassword),
                        "ADMIN"
                    ));
                admin.setRol("ADMIN");
                if (admin.getNombre() == null || admin.getNombre().isBlank()) {
                    admin.setNombre("Administradora");
                }
                if (!passwordEncoder.matches(initialAdminPassword, admin.getPassword())) {
                    admin.setPassword(passwordEncoder.encode(initialAdminPassword));
                }
                repository.save(admin);
            }
        };
    }
}