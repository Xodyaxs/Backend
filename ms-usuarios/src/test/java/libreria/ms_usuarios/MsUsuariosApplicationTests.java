package libreria.ms_usuarios;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "app.jwt.secret=TestSecretMustBeAtLeast32BytesLong!")
class MsUsuariosApplicationTests {

	@Test
	void contextLoads() {
	}

}
