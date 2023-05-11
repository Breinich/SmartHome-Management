package com.itcatcetc.smarthome.room;

import com.itcatcetc.smarthome.actuator.Actuator;
import com.itcatcetc.smarthome.login.Role;
import com.itcatcetc.smarthome.sensor.Sensor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
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
    @Secured({Role.GUEST, Role.HOMIE})
    public List<Room> getRooms(){
        return roomService.getRooms();
    }

    @GetMapping(path = "/sensors/{roomId}")
    @Secured({Role.GUEST, Role.HOMIE})
    public List<Sensor> getSensorsByRoomId(@PathVariable("roomId") Integer roomId){
        return roomService.getSensorsByRoomId(roomId);
    }

    @GetMapping(path = "/actuators/{roomId}")
    @Secured({Role.GUEST, Role.HOMIE})
    public List<Actuator> getActuatorsByRoomId(@PathVariable("roomId") Integer roomId){
        return roomService.getActuatorsByRoomId(roomId);
    }

    @PostMapping
    @Secured(Role.HOMIE)
    public void registerNewRoom(@RequestBody  Room room){roomService.addNewRoom(room);
    }

    @DeleteMapping(path = "{roomId}")
    @Secured(Role.HOMIE)
    public void deleteRoom(@PathVariable("roomId") Integer roomId){
        roomService.deleteRoom(roomId);
    }

    @PutMapping(path = "{roomId}")
    @Secured(Role.HOMIE)
    public void updateRoom(@PathVariable("roomId") Integer roomId, @RequestParam(required = false) String name){
        roomService.updateRoom(roomId, name);
    }
}
