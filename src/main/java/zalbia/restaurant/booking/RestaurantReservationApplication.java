package zalbia.restaurant.booking;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@OpenAPIDefinition(
        info = @Info(
                title = "Restaurant Reservation API",
                version = "1.0"
        )
)
@SpringBootApplication
@ComponentScan(basePackages = "zalbia.restaurant.booking.rest.api.v1")
@EnableJpaRepositories(basePackages = "zalbia.restaurant.booking.domain")
public class RestaurantReservationApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestaurantReservationApplication.class, args);
    }

}
