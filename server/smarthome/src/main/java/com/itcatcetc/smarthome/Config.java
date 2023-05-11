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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Configuration
public class Config {

    private RestTemplate restTemplate;

    @Bean
    CommandLineRunner commandLineRunner(SensorRepository sensorRepository, RoomRepository roomRepository,
                                        ActuatorRepository actuatorRepository, UserRepository userRepository,
                                        PasswordEncoder passwordEncoder, RestTemplateBuilder restTemplateBuilder) {

        return args -> {
            if (userRepository.count() == 0)
                populateUsers(userRepository, passwordEncoder);

            if (roomRepository.count() == 0)
                populateRooms(roomRepository);

            if (actuatorRepository.count() == 0)
                populateActuators(actuatorRepository, roomRepository);

            if (sensorRepository.count() == 0)
                populateSensors(sensorRepository, roomRepository);

            restTemplate = restTemplateBuilder.build();

            //ping all sensors and actuators
            for(Sensor sensor : sensorRepository.findAll()){
                Long sent = System.currentTimeMillis();
                Long back = restTemplate.getForObject(sensor.getApiEndpoint() + "ping/{time}", Long.class, sent);
                if(back != null)
                    System.out.println(sensor.getName() + " - Ping: " + (back - sent) + "ms");

                else
                    System.out.println(sensor.getName() + " - Ping: null");
            }
            for (Actuator actuator : actuatorRepository.findAll()) {
                Long sent = System.currentTimeMillis();
                Long back = restTemplate.getForObject(actuator.getApiEndpoint() + "ping/{time}", Long.class, sent);
                if(back != null)
                    System.out.println(actuator.getName() + " - Ping: " + (back - sent) + "ms");

                else
                    System.out.println(actuator.getName() + " - Ping: null");
            }

            //read and write to DB
            Room testRoom = new Room("TestRoom");
            roomRepository.save(testRoom);
            System.out.println("Saved room: " + testRoom);
            Optional<Room> res = roomRepository.findRoomByName("TestRoom");
            if(res.isPresent()) {
                System.out.println("Found room: " + res.get());
                roomRepository.delete(res.get());
                System.out.println("Deleted room: " + res.get());
            }
            else
                System.out.println("Room not found, SOMEWHERE SOMETHING WENT WRONG");



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

