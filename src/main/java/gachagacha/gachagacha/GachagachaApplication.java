package gachagacha.gachagacha;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class GachagachaApplication {

	public static void main(String[] args) {
		SpringApplication.run(GachagachaApplication.class, args);
	}

}
