package com.itcatcetc.smarthome;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itcatcetc.smarthome.actuator.Actuator;
import com.itcatcetc.smarthome.actuator.ActuatorRepository;
import com.itcatcetc.smarthome.room.Room;
import com.itcatcetc.smarthome.room.RoomRepository;
import com.itcatcetc.smarthome.sensor.Sensor;
import com.itcatcetc.smarthome.sensor.SensorRepository;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Optional;

import static java.lang.String.format;

@RestController
@RequestMapping(path = "")
public class BaseController {

    private final RoomRepository roomRepository;

    private final SensorRepository sensorRepository;

    private final ActuatorRepository actuatorRepository;

    private final RestTemplate restTemplate;

    public BaseController(RoomRepository roomRepository, SensorRepository sensorRepository, ActuatorRepository actuatorRepository,
                          RestTemplateBuilder restTemplateBuilder) {
        this.roomRepository = roomRepository;
        this.sensorRepository = sensorRepository;
        this.actuatorRepository = actuatorRepository;

        this.restTemplate = restTemplateBuilder.build();
    }

    @GetMapping
    public ResponseEntity<String> getBase() {
        return ResponseEntity.ok("Welcome to SmartHome API");
    }

    @GetMapping(path = "api/v1/smarthome/status")
    @PreAuthorize("hasRole('GUEST') or hasRole('HOMIE')")
    public ResponseEntity<String> getStatus() {

        HashMap<String, String> status = new HashMap<>();

        //ping all sensors and actuators
        for (Sensor sensor : sensorRepository.findAll()) {
            Long sent = System.currentTimeMillis();
            try {
                Long back = restTemplate.getForObject(sensor.getApiEndpoint() + "ping/{time}", Long.class, sent);
                if (back == null)
                    throw new ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Sensor not found");

                status.put("Sensor " + sensor.getName(), format("Ping: %d ms", back - sent));
            } catch (Exception e) {
                status.put("Sensor " + sensor.getName(), "Not reachable");
            }
        }
        for (Actuator actuator : actuatorRepository.findAll()) {
            Long sent = System.currentTimeMillis();
            try {
                Long back = restTemplate.getForObject(actuator.getApiEndpoint() + "ping/{time}", Long.class, sent);
                if (back != null)
                    status.put("Actuator " + actuator.getName(), format("Ping: %d ms", back - sent));
            } catch (Exception e) {
                status.put("Actuator " + actuator.getName(), "Not reachable");
            }
        }

        //read and write to DB
        Room testRoom = new Room("TestRoomgvjhdsfhgwefgsdljhfbsdljahfgsdl");
        roomRepository.save(testRoom);
        status.put("DB - save", "OK");
        Optional<Room> res = roomRepository.findRoomByName("TestRoomgvjhdsfhgwefgsdljhfbsdljahfgsdl");
        if (res.isPresent()) {
            status.put("DB - read", "OK");
            roomRepository.delete(res.get());
            status.put("DB - delete", "OK");
        } else
            status.put("DB - read", "FAILED");

        status.put("API", "OK");

        ObjectMapper mapper = new ObjectMapper();
        String json;
        try {
            json = mapper.writeValueAsString(status);
        } catch (Exception e) {
            json = status.toString();
        }

        return ResponseEntity.ok(json);
    }
}
