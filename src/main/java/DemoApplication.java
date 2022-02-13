import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Import({
		// swagger config@local
		com.tom.api.config.SwaggerConfig.class,
		// config@tom-db-service
		com.tom.db.beanServiceConfig.ServiceAccessConfig.class,
		////config@tom-redis-service
		com.tom.redis.beanServiceConfig.ServiceAccessConfig.class
})

@ComponentScan	(basePackages = {
		// controller@local
		"com.tom.api.controller",
		// aop@local
		"com.tom.api.aop",
		// aop@tom-db-service
		"com.tom.db.service.aop",
		// aop@tom-db-service-tool
		"com.tom.db.service.tool",
		// table access service@tom-db-service
		"com.tom.db.service.provider" ,
		// cache service@tom-db-service
		"com.tom.cache.service.provider"
})

@EnableJpaRepositories(
		// repositiry@tom-db-service
		"com.tom.db.repository"
)

@EntityScan(basePackages = {
		// entity@tom-db-entity
		"com.tom.db.entity"
})

@SpringBootApplication
public class DemoApplication {
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
}
