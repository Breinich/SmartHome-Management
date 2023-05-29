package com.itcatcetc.smarthome.room;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itcatcetc.smarthome.actuator.Actuator;
import com.itcatcetc.smarthome.sensor.Sensor;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * RoomController is a class that handles HTTP requests
 * and returns responses in JSON format
 * The @RestController annotation marks the class as a controller where every method returns a domain object instead of a view
 * The @RequestMapping annotation maps HTTP requests to handler methods of the controller
 */
@RestController
@RequestMapping(path = "api/v1/smarthome/rooms")
public class RoomController {
    private final RoomService roomService;

    /**
     * Constructor
     * @param roomService the service that handles the business logic
     * {@code @Autowired} annotation marks a constructor, field, setter method, or config method as to be autowired by Spring dependency injection
     */
    @Autowired
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }


    /**
     * GET request
     * @return List of rooms
     */
    @GetMapping
    @PreAuthorize("hasRole('GUEST') or hasRole('HOMIE')")
    public ResponseEntity<String> getRooms() {
        List<Room> list = roomService.getRooms();
        ObjectMapper mapper = new ObjectMapper();
        String json;
        try {
            json = mapper.writeValueAsString(list);
        } catch (Exception e) {
            json = list.toString();
        }
        return ResponseEntity.ok(json);
    }

    /**
     * GET request  /
     * @param roomId the id of the room
     * @return List of sensors in the room
     */
    @GetMapping(path = "/sensors/{roomId}")
    @PreAuthorize("hasRole('GUEST') or hasRole('HOMIE')")
    public ResponseEntity<String> getSensorsByRoomId(@Valid @PathVariable("roomId") Integer roomId) {
        List<Sensor> list = roomService.getSensorsByRoomId(roomId);
        ObjectMapper mapper = new ObjectMapper();
        String json;
        try {
            json = mapper.writeValueAsString(list);
        } catch (Exception e) {
            json = list.toString();
        }
        return ResponseEntity.ok(json);
    }

    /**
     * GET request
     * @param roomId the id of the room
     * @return List of actuators in the room
     */
    @GetMapping(path = "/actuators/{roomId}")
    @PreAuthorize("hasRole('GUEST') or hasRole('HOMIE')")
    public ResponseEntity<String> getActuatorsByRoomId(@Valid @PathVariable("roomId") Integer roomId) {
        List<Actuator> list = roomService.getActuatorsByRoomId(roomId);
        ObjectMapper mapper = new ObjectMapper();
        String json;
        try {
            json = mapper.writeValueAsString(list);
        } catch (Exception e) {
            json = list.toString();
        }
        return ResponseEntity.ok(json);
    }

    /**
     * POST request
     * @param room the room to be added
     */
    @PostMapping
    @PreAuthorize("hasRole('HOMIE')")
    public void registerNewRoom(@Valid @RequestBody Room room) {
        roomService.addNewRoom(room);
    }

    /**
     * DELETE request
     * @param roomId the id of the room
     */
    @DeleteMapping(path = "{roomId}")
    @PreAuthorize("hasRole('HOMIE')")
    public void deleteRoom(@Valid @PathVariable("roomId") Integer roomId) {
        roomService.deleteRoom(roomId);
    }

    /**
     * PUT request
     * @param room the room to be updated
     */
    @PutMapping
    @PreAuthorize("hasRole('HOMIE')")
    public void updateRoom(@Valid @RequestBody Room room) {
        roomService.updateRoom(room);
    }
}
