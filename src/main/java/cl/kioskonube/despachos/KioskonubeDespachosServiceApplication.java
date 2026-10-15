package cl.kioskonube.despachos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class KioskonubeDespachosServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(KioskonubeDespachosServiceApplication.class, args);
    }
}
