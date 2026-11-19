package libreria.ms_usuarios.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import libreria.ms_usuarios.model.Usuario;
import libreria.ms_usuarios.repository.UsuarioRepository;

class DataLoaderTests {

    private static final String ADMIN_EMAIL = "parrafosegundo@admin.cl";
    private static final String INITIAL_PASSWORD = "temporary-test-password";

    @Test
    void actualizaLaClaveHasheadaDelAdminExistente() throws Exception {
        UsuarioRepository repository = mock(UsuarioRepository.class);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Usuario admin = new Usuario("Administradora", ADMIN_EMAIL, passwordEncoder.encode("old-password"), "CLIENTE");
        when(repository.count()).thenReturn(1L);
        when(repository.findByEmail(ADMIN_EMAIL)).thenReturn(Optional.of(admin));

        CommandLineRunner loader = new DataLoader().initDatabase(repository, passwordEncoder, INITIAL_PASSWORD);
        loader.run();

        assertThat(admin.getRol()).isEqualTo("ADMIN");
        assertThat(passwordEncoder.matches(INITIAL_PASSWORD, admin.getPassword())).isTrue();
        verify(repository).save(admin);
    }

    @Test
    void creaElAdminSolicitadoSiNoExiste() throws Exception {
        UsuarioRepository repository = mock(UsuarioRepository.class);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        when(repository.count()).thenReturn(1L);
        when(repository.findByEmail(ADMIN_EMAIL)).thenReturn(Optional.empty());

        CommandLineRunner loader = new DataLoader().initDatabase(repository, passwordEncoder, INITIAL_PASSWORD);
        loader.run();

        org.mockito.ArgumentCaptor<Usuario> captor = org.mockito.ArgumentCaptor.forClass(Usuario.class);
        verify(repository).save(captor.capture());
        Usuario createdAdmin = captor.getValue();
        assertThat(createdAdmin.getEmail()).isEqualTo(ADMIN_EMAIL);
        assertThat(createdAdmin.getRol()).isEqualTo("ADMIN");
        assertThat(passwordEncoder.matches(INITIAL_PASSWORD, createdAdmin.getPassword())).isTrue();
    }
}
