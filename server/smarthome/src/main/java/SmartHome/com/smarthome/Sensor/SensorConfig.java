package SmartHome.com.smarthome.Sensor;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SensorConfig {
    @Bean
    CommandLineRunner commandLineRunner(SensorRepository repository){
        return args -> {
            Sensor sensor1 = new Sensor(
                    "Lamp",
                    "Living Room"
            );
            Sensor sensor2 = new Sensor(
                    "Lamp",
                    "Bedroom"
            );
            repository.saveAll(
                    List.of(sensor1, sensor2)
            );
        };
    }
}
