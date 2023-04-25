package SmartHome.com.smarthome;

import SmartHome.com.smarthome.Data.Data;
import SmartHome.com.smarthome.Data.DataRepository;
import SmartHome.com.smarthome.Room.Room;
import SmartHome.com.smarthome.Room.RoomRepository;
import SmartHome.com.smarthome.Sensor.Sensor;
import SmartHome.com.smarthome.Sensor.SensorRepository;
import SmartHome.com.smarthome.Type.Type;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class Config {
    @Bean
    CommandLineRunner commandLineRunner(SensorRepository repository, RoomRepository roomRepository, DataRepository dataRepository) {
        return args -> {
            Sensor sensor1 = new Sensor(
                    "Temperature sensor",
                    Type.TEMPERATURE
            );
            Sensor sensor2 = new Sensor(
                    "Light sensor",
                    Type.LIGHT
            );
            repository.saveAll(
                    List.of(sensor1, sensor2)
            );

            Room room1 = new Room(
                    "Living Room"
            );
            Room room2 = new Room(
                    "Bedroom"
            );
            roomRepository.saveAll(
                    List.of(room1, room2)
            );


            Data data1 = new Data(3 );

            Data data2 = new Data(
                    4
            );

            dataRepository.saveAll(
                    List.of(data1, data2)
            );

        };
    }
}
