import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Import({
		com.tom.api.config.SwaggerConfig.class, 						// config@local
		com.tom.db.beanServiceConfig.ServiceAccessConfig.class			// config@tom-db-service
})

@ComponentScan	(basePackages = {
		"com.tom.api.controller," 		+ 	// controller@local
		"com.tom.api.aop," 				+ 	// aop@load
		"com.tom.db.service.aop," 		+ 	// aop@@tom-db-service
		"com.tom.db.service.tool,"		+	// aop@tom-db-service-tool
		"com.tom.db.service.provider" 		// table access service@tom-db-service
})

@EnableJpaRepositories(basePackages = {
		"com.tom.db.repository"				// repositiry@tom-db-service
})

@EntityScan(basePackages = {
		"com.tom.db.entity"					// entity@tom-db-entity
})

@SpringBootApplication
public class DemoApplication {
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
}
