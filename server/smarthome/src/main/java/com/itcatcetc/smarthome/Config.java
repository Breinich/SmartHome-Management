
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
           if(userRepository.count() == 0)
               populateUsers(userRepository, passwordEncoder);

           if(roomRepository.count() == 0)
               populateRooms(roomRepository);

           if(actuatorRepository.count() == 0)
               populateActuators(actuatorRepository, roomRepository);

           if (sensorRepository.count() == 0)
               populateSensors(sensorRepository, roomRepository);



       };
   }

    private void populateRooms(RoomRepository roomRepository) {
       Room room1 = new Room(
               "Living Room");
       Room room2 = new Room(
               "Bedroom");
       roomRepository.saveAll(
               List.of(room1, room2)
       );
   }

   private void populateSensors(SensorRepository repository, RoomRepository roomRepository){
       Room room1 = roomRepository.findAll().get(0);

       Sensor sensor1 = new Sensor(
               "Temperature sensor",
               Type.TEMPERATURE,
               room1);
       Sensor sensor2 = new Sensor(
               "Light sensor",
               Type.LIGHT,
               room1);
       repository.saveAll(
               List.of(sensor1, sensor2)
       );

       room1.addSensor(sensor1);
       room1.addSensor(sensor2);
   }

   private void populateActuators(ActuatorRepository actuatorRepository, RoomRepository roomRepository){
       Room room1 = roomRepository.findAll().get(0);

       Actuator actuator1 = new Actuator(
               "Light",
               Type.LIGHT,
               room1);
       Actuator actuator2 = new Actuator("Heater",
               Type.TEMPERATURE,
               room1);

       actuatorRepository.saveAll(List.of(actuator1, actuator2));

       room1.addActuator(actuator1);
       room1.addActuator(actuator2);
   }


   private void populateUsers(UserRepository userRepository, PasswordEncoder passwordEncoder){
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
   }



}

