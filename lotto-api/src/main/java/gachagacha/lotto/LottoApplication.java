package gachagacha.lotto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableJpaAuditing
@EntityScan(basePackages = "gachagacha.db")
@EnableJpaRepositories(basePackages = "gachagacha.db")
@SpringBootApplication(scanBasePackages = "gachagacha")
public class LottoApplication {

	public static void main(String[] args) {
		try {
			SpringApplication.run(LottoApplication.class, args);
		} catch (Exception e) {
			System.err.println("An error occurred while starting the application:");
			e.printStackTrace();
			System.exit(1);
		}

	}

}
