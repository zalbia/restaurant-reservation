package zalbia.restaurant.reservation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "zalbia.restaurant.reservation.rest.api.v1")
public class ReservationApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReservationApiApplication.class, args);
	}

}
