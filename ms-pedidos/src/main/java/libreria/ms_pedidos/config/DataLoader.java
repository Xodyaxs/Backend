package libreria.ms_pedidos.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import libreria.ms_pedidos.model.Pedido;
import libreria.ms_pedidos.repository.PedidoRepository;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner initDatabase(PedidoRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                repository.save(new Pedido(
                    "cliente@parrafosegundo.cl",
                    "1x La única historia ($16.990), 1x Blanco ($8.000)",
                    24990
                ));
            }
        };
    }
}