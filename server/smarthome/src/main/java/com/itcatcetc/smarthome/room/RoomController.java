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
 */
@RestController
@RequestMapping(path = "api/v1/smarthome/rooms")
public class RoomController {
    private final RoomService roomService;

    /**
     * Constructor
     * @param roomService
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
     * @param roomId
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
     * @param roomId
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
     * @param room
     * @return make a new room
     */
    @PostMapping
    @PreAuthorize("hasRole('HOMIE')")
    public void registerNewRoom(@Valid @RequestBody Room room) {
        roomService.addNewRoom(room);
    }

    /**
     * DELETE request
     * @param roomId
     * @return delete a room
     */
    @DeleteMapping(path = "{roomId}")
    @PreAuthorize("hasRole('HOMIE')")
    public void deleteRoom(@Valid @PathVariable("roomId") Integer roomId) {
        roomService.deleteRoom(roomId);
    }

    /**
     * PUT request
     * @param room
     * @return update a room
     */
    @PutMapping
    @PreAuthorize("hasRole('HOMIE')")
    public void updateRoom(@Valid @RequestBody Room room) {
        roomService.updateRoom(room);
    }
}
