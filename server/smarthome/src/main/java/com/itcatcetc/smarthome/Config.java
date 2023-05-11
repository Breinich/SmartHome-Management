
package com.itcatcetc.smarthome;

import com.itcatcetc.smarthome.actuator.ActuatorRepository;
import com.itcatcetc.smarthome.login.Role;
import com.itcatcetc.smarthome.login.user.User;
import com.itcatcetc.smarthome.login.user.UserRepository;
import com.itcatcetc.smarthome.room.RoomRepository;
import com.itcatcetc.smarthome.sensor.SensorRepository;
import com.itcatcetc.smarthome.sensor.data.SensorDataRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
public class Config {
    //Azert commentelem ki, hogy ne keruljenek uj adatok a tablakba
    //Ha valamiert ki kell torolni a tablat, akkor celszeru ujra lefuttatni, hogy legyenek benne ujra adatok
   @Bean
   CommandLineRunner commandLineRunner(SensorRepository repository, RoomRepository roomRepository, SensorDataRepository dataRepository,
                                       ActuatorRepository actuatorRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {

        return args -> {
            /*
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


            User user1 = new User();
            user1.setFirstName("user1");
            user1.setLastName("user1");
            user1.setEmail("user1@gmail.com");
            user1.setPassword(passwordEncoder.encode("user1"));
            user1.setRoles(List.of(Role.HOMIE));

            User user2 = new User();
            user2.setFirstName("user2");
            user2.setLastName("user2");
            user2.setEmail("user2@gmail.com");
            user2.setPassword(passwordEncoder.encode("user2"));
            user2.setRoles(List.of(Role.GUEST));
            userRepository.saveAll(
                    List.of(user1, user2)
            );

            */

        };


    }
}

