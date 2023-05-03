package SmartHome.com.smarthome.Room;

import SmartHome.com.smarthome.Sensor.Sensor;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<Room> getRooms(){
        return roomService.getRooms();
    }

    @GetMapping(path = "{roomId}")
    public List<Sensor> getSensorsByRoomId(@PathVariable("roomId") Integer roomId){
        return roomService.getSensorsByRoomId(roomId);
    }

    @PostMapping
    public void registerNewRoom(@RequestBody  Room room){roomService.addNewRoom(room);
    }

    @DeleteMapping(path = "{roomId}")
    public void deleteRoom(@PathVariable("roomId") Integer roomId){
        roomService.deleteRoom(roomId);
    }

    @PutMapping(path = "{roomId}")
    public void updateRoom(@PathVariable("roomId") Integer roomId, @RequestParam(required = false) String name){
        roomService.updateRoom(roomId, name);
    }
}
