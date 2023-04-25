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

import java.util.ArrayList;
import java.util.List;

@Configuration
public class Config {
    @Bean
    CommandLineRunner commandLineRunner(SensorRepository repository, RoomRepository roomRepository, DataRepository dataRepository) {
        return args -> {
            List <Sensor> sensors1 = new ArrayList<>();
            List <Sensor> sensors2 = new ArrayList<>();
            Room room1 = new Room(
                    "Living Room",
                    sensors1);
            Room room2 = new Room(
                    "Bedroom",
                    sensors2);
            roomRepository.saveAll(
                    List.of(room1, room2)
            );

            Sensor sensor1 = new Sensor(
                    "Temperature sensor",
                    Type.TEMPERATURE,
                    room1);
            Sensor sensor2 = new Sensor(
                    "Light sensor",
                    Type.LIGHT,
                    room2);
            repository.saveAll(
                    List.of(sensor1, sensor2)
            );

            room1.addSensor(sensor1);
            room2.addSensor(sensor2);



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
