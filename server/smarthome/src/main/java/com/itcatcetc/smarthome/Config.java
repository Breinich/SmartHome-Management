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
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static java.lang.String.format;

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
            Logger logger = LoggerFactory.getLogger(Config.class);

            //ping all sensors and actuators
            for(Sensor sensor : sensorRepository.findAll()){
                Long sent = System.currentTimeMillis();
                try {
                    Long back = restTemplate.getForObject(sensor.getApiEndpoint() + "ping/{time}", Long.class, sent);
                    if(back == null)
                        throw new ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Sensor not found");

                    logger.info(format("Sensor %s\t\t- Ping: %d ms",sensor.getName(), back - sent));

                }
                catch (Exception e){
                    logger.error(format("Sensor %s is not reachable", sensor.getName()));
                }
            }
            for (Actuator actuator : actuatorRepository.findAll()) {
                Long sent = System.currentTimeMillis();
                try {
                    Long back = restTemplate.getForObject(actuator.getApiEndpoint() + "ping/{time}", Long.class, sent);
                    if (back != null)
                        logger.info(format("Actuator %s\t\t- Ping: %d ms",actuator.getName(), back - sent));
                }
                catch (Exception e){
                    logger.error(format("Actuator %s is not reachable", actuator.getName()));
                }
            }

            //read and write to DB
            Room testRoom = new Room("TestRoom");
            roomRepository.save(testRoom);
            logger.info(format("Saved room: %s", testRoom));
            Optional<Room> res = roomRepository.findRoomByName("TestRoom");
            if(res.isPresent()) {
                logger.info(format("Found room: %s", res.get()));
                roomRepository.delete(res.get());
                logger.info(format("Deleted room: %s", res.get()));
            }
            else
                logger.error("Room not found, SOMEWHERE SOMETHING WENT WRONG");

            logger.info(format("Application finished checks at %d", System.currentTimeMillis()));

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
        user1.setPassword(passwordEncoder.encode("User1.User1"));
        user1.setRoles(List.of(Role.HOMIE));

        User user2 = new User();
        user2.setFirstName("Eva");
        user2.setLastName("Kiss");
        user2.setEmail("user2@gmail.com");
        user2.setPassword(passwordEncoder.encode("User2.User2"));
        user2.setRoles(List.of(Role.GUEST));
        userRepository.saveAll(
                List.of(user1, user2)
        );
    }


}

