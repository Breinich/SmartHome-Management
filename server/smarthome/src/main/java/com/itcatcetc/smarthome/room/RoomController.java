package com.itcatcetc.smarthome.room;

import com.itcatcetc.smarthome.actuator.Actuator;
import com.itcatcetc.smarthome.login.Role;
import com.itcatcetc.smarthome.sensor.Sensor;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="api/v1/smarthome/rooms")
public class RoomController {
    private final RoomService roomService;

    @Autowired
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    @PreAuthorize("hasRole('GUEST') or hasRole('HOMIE')")
    public List<Room> getRooms(){
        return roomService.getRooms();
    }

    @GetMapping(path = "/sensors/{roomId}")
    @PreAuthorize("hasRole('GUEST') or hasRole('HOMIE')")
    public List<Sensor> getSensorsByRoomId(@Valid @PathVariable("roomId") Integer roomId){
        return roomService.getSensorsByRoomId(roomId);
    }

    @GetMapping(path = "/actuators/{roomId}")
    @PreAuthorize("hasRole('GUEST') or hasRole('HOMIE')")
    public List<Actuator> getActuatorsByRoomId(@Valid @PathVariable("roomId") Integer roomId){
        return roomService.getActuatorsByRoomId(roomId);
    }

    @PostMapping
    @PreAuthorize("hasRole('HOMIE')")
    public void registerNewRoom(@Valid @RequestBody  Room room){roomService.addNewRoom(room);
    }

    @DeleteMapping(path = "{roomId}")
    @PreAuthorize("hasRole('HOMIE')")
    public void deleteRoom(@Valid @PathVariable("roomId") Integer roomId){
        roomService.deleteRoom(roomId);
    }

    @PutMapping(path = "{roomId}")
    @PreAuthorize("hasRole('HOMIE')")
    public void updateRoom(@Valid @PathVariable("roomId") Integer roomId, @RequestParam(required = false) String name){
        roomService.updateRoom(roomId, name);
    }
}
