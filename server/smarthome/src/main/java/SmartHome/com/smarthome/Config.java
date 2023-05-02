package SmartHome.com.smarthome;

import SmartHome.com.smarthome.Room.Room;
import SmartHome.com.smarthome.Room.RoomRepository;
import SmartHome.com.smarthome.Sensor.Sensor;
import SmartHome.com.smarthome.Sensor.SensorRepository;
import SmartHome.com.smarthome.SensorData.SensorData;
import SmartHome.com.smarthome.SensorData.SensorDataRepository;
import SmartHome.com.smarthome.Type.Type;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class Config {
    //Azer commentelem ki, hogy ne keruljenek uj adatok a tablakba
    //Ha valamiert ki kell torolni a tablat, akkor celszeru ujra lefuttatni, hogy legyenek benne ujra adatok
   @Bean
   CommandLineRunner commandLineRunner(SensorRepository repository, RoomRepository roomRepository, SensorDataRepository dataRepository) {
        return args -> {
           List<Sensor> sensors1 = new ArrayList<>();
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

            List<SensorData> ures = new ArrayList<>();



            Sensor sensor1 = new Sensor(
                    "Temperature sensor",
                    Type.TEMPERATURE,
                    room1, ures );
            Sensor sensor2 = new Sensor(
                    "Light sensor",
                    Type.LIGHT,
                    room2, ures);
            repository.saveAll(
                    List.of(sensor1, sensor2)
            );

            room1.addSensor(sensor1);
            room2.addSensor(sensor2);



            SensorData data1 = new SensorData(Type.TEMPERATURE,sensor1,  23);
            SensorData data2 = new SensorData(Type.LIGHT, sensor1, 40);

            dataRepository.saveAll(
                    List.of(data1, data2)
            );

        };
    }
}
