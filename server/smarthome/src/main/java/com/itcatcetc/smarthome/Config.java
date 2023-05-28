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

/**
 * Config is a class that handles the configuration of the application
 * We create a bean that will be used to populate the database with some data if it is empty
 * We also create a bean that will be used to ping all sensors and actuators
 * and check if they are reachable
 */
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

            /*
             * Ping all sensors and actuators
             */
            for (Sensor sensor : sensorRepository.findAll()) {
                Long sent = System.currentTimeMillis();
                try {
                    Long back = restTemplate.getForObject(sensor.getApiEndpoint() + "ping/{time}", Long.class, sent);
                    if (back == null)
                        throw new ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Sensor not found");

                    logger.info(format("Sensor %s\t\t- Ping: %d ms", sensor.getName(), back - sent));

                } catch (Exception e) {
                    logger.error(format("Sensor %s is not reachable", sensor.getName()));
                }
            }
            for (Actuator actuator : actuatorRepository.findAll()) {
                Long sent = System.currentTimeMillis();
                try {
                    Long back = restTemplate.getForObject(actuator.getApiEndpoint() + "ping/{time}", Long.class, sent);
                    if (back != null)
                        logger.info(format("Actuator %s\t\t- Ping: %d ms", actuator.getName(), back - sent));
                } catch (Exception e) {
                    logger.error(format("Actuator %s is not reachable", actuator.getName()));
                }
            }

            /*
             * This is a test room that is created, saved, found, deleted
             * and then the application finishes
             * This is just to test the database
             */
            Room testRoom = new Room("TestRoomhfiufehiusdfhsdhfihweufhweufhweu");
            roomRepository.save(testRoom);
            logger.info(format("Saved room: %s", testRoom));
            Optional<Room> res = roomRepository.findRoomByName("TestRoomhfiufehiusdfhsdhfihweufhweufhweu");
            if (res.isPresent()) {
                logger.info(format("Found room: %s", res.get()));
                roomRepository.delete(res.get());
                logger.info(format("Deleted room: %s", res.get()));
            } else
                logger.error("Room not found, SOMEWHERE SOMETHING WENT WRONG");

            logger.info(format("Application finished checks at %d", System.currentTimeMillis()));

        };
    }

    /**
     * This method populates the database with some rooms
     * @param roomRepository the repository that handles the rooms
     */
    private void populateRooms(RoomRepository roomRepository) {
        Room room1 = new Room(
                "LivingRoom");
        Room room2 = new Room(
                "Bedroom");
        roomRepository.saveAll(
                List.of(room1, room2)
        );
    }

    /**
     * This method populates the database with some sensors
     * @param repository the repository that handles the sensors
     * @param roomRepository the repository that handles the rooms
     */
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

    /**
     * This method populates the database with some actuators
     * @param actuatorRepository  the repository that handles the actuators
     * @param roomRepository the repository that handles the rooms
     */
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


    /**
     * This method populates the database with some users
     * @param userRepository the repository that handles the users
     * @param passwordEncoder the encoder that encodes the passwords
     */
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

