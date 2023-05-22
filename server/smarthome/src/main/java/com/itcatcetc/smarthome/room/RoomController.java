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

@RestController
@RequestMapping(path = "api/v1/smarthome/rooms")
public class RoomController {
    private final RoomService roomService;

    @Autowired
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

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

    @PostMapping
    @PreAuthorize("hasRole('HOMIE')")
    public void registerNewRoom(@Valid @RequestBody Room room) {
        roomService.addNewRoom(room);
    }

    @DeleteMapping(path = "{roomId}")
    @PreAuthorize("hasRole('HOMIE')")
    public void deleteRoom(@Valid @PathVariable("roomId") Integer roomId) {
        roomService.deleteRoom(roomId);
    }

    @PutMapping
    @PreAuthorize("hasRole('HOMIE')")
    public void updateRoom(@Valid @RequestBody Room room) {
        roomService.updateRoom(room);
    }
}
