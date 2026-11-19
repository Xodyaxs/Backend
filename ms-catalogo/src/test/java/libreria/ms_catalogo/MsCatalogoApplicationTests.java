package libreria.ms_catalogo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "app.jwt.secret=TestSecretMustBeAtLeast32BytesLong!")
class MsCatalogoApplicationTests {

	@Test
	void contextLoads() {
	}

}
