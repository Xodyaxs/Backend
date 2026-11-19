package libreria.ms_pedidos;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "app.jwt.secret=TestSecretMustBeAtLeast32BytesLong!")
class MsPedidosApplicationTests {

	@Test
	void contextLoads() {
	}

}
