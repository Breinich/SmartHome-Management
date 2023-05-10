
package SmartHome.com.smarthome;

import SmartHome.com.smarthome.Actuator.Actuator;
import SmartHome.com.smarthome.Actuator.ActuatorRepository;
import SmartHome.com.smarthome.Login.User.User;
import SmartHome.com.smarthome.Login.User.UserRepository;
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
   CommandLineRunner commandLineRunner(SensorRepository repository, RoomRepository roomRepository, SensorDataRepository dataRepository,
                                       ActuatorRepository actuatorRepository, UserRepository userRepository) {
        return args -> {
            Room room1 = new Room(
                    "Living Room");
            Room room2 = new Room(
                    "Bedroom");
            roomRepository.saveAll(
                    List.of(room1, room2)
            );




            Sensor sensor1 = new Sensor(
                    "Temperature sensor",
                    Type.TEMPERATURE,
                    room1 );
            Sensor sensor2 = new Sensor(
                    "Light sensor",
                    Type.LIGHT,
                    room2);
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

            Actuator actuator1 = new Actuator(
                    "Light",
                    Type.LIGHT,
                    room1);
            Actuator actuator2 = new Actuator(  "Heater",
                    Type.TEMPERATURE,
                    room2);

            actuatorRepository.saveAll(
                    List.of(actuator1, actuator2)
            );

            User user1 = new User(
                    "user1",
                    "user1",
                    "user1@gmail.com",
                    "user1");
            User user2 = new User(
                    "user2",
                    "user2",
                    "user2@gmail.com",
                    "user2");
            userRepository.saveAll(
                    List.of(user1, user2)
            );

        };
    }
}

