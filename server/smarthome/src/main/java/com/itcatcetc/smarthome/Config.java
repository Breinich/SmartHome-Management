package com.itcatcetc.smarthome;

import com.itcatcetc.smarthome.actuator.Actuator;
import com.itcatcetc.smarthome.actuator.ActuatorRepository;
import com.itcatcetc.smarthome.login.Role;
import com.itcatcetc.smarthome.login.user.User;
import com.itcatcetc.smarthome.login.user.UserRepository;
import com.itcatcetc.smarthome.room.Room;
import com.itcatcetc.smarthome.room.RoomRepository;
import com.itcatcetc.smarthome.sensor.Sensor;
import com.itcatcetc.smarthome.sensor.SensorRepository;
import com.itcatcetc.smarthome.type.Type;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
public class Config {

    @Bean
    CommandLineRunner commandLineRunner(SensorRepository sensorRepository, RoomRepository roomRepository,
                                        ActuatorRepository actuatorRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {

        return args -> {
            if (userRepository.count() == 0)
                populateUsers(userRepository, passwordEncoder);

            if (roomRepository.count() == 0)
                populateRooms(roomRepository);

            if (actuatorRepository.count() == 0)
                populateActuators(actuatorRepository, roomRepository);

            if (sensorRepository.count() == 0)
                populateSensors(sensorRepository, roomRepository);



        };
    }

    private void populateRooms(RoomRepository roomRepository) {
        Room room1 = new Room(
                "LivingRoom");
        Room room2 = new Room(
                "Bedroom");
        roomRepository.saveAll(
                List.of(room1, room2)
        );
    }

    private void populateSensors(SensorRepository repository, RoomRepository roomRepository) {
        Room room1 = roomRepository.findAll().get(0);

        Sensor sensor1 = new Sensor(
                "TemperatureSensor",
                Type.TEMPERATURE,
                room1,
                "192.168.0.3/");
        Sensor sensor2 = new Sensor(
                "LightSensor",
                Type.LIGHT,
                room1,
                "192.168.0.4/");
        repository.saveAll(
                List.of(sensor1, sensor2)
        );
    }

    private void populateActuators(ActuatorRepository actuatorRepository, RoomRepository roomRepository) {
        Room room1 = roomRepository.findAll().get(0);

        Actuator actuator1 = new Actuator(
                "Light",
                Type.LIGHT,
                room1,
                "192.168.0.1/");
        Actuator actuator2 = new Actuator("Heater",
                Type.TEMPERATURE,
                room1,
                "192.168.0.2/");

        actuatorRepository.saveAll(List.of(actuator1, actuator2));
    }


    private void populateUsers(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        User user1 = new User();
        user1.setFirstName("Adam");
        user1.setLastName("Nagy");
        user1.setEmail("user1@gmail.com");
        user1.setPassword(passwordEncoder.encode("user1"));
        user1.setRoles(List.of(Role.HOMIE));

        User user2 = new User();
        user2.setFirstName("Eva");
        user2.setLastName("Kiss");
        user2.setEmail("user2@gmail.com");
        user2.setPassword(passwordEncoder.encode("user2"));
        user2.setRoles(List.of(Role.GUEST));
        userRepository.saveAll(
                List.of(user1, user2)
        );
    }


}

