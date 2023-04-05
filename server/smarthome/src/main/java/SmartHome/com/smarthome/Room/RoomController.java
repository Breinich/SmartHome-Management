package SmartHome.com.smarthome.Room;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="api/v1/smarthome/room")
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

    @PostMapping
    public void registerNewRoom(@RequestBody  Room room){roomService.addNewRoom(room);
    }

    @DeleteMapping(path = "{roomId}")
    public void deleteSensor(@PathVariable("roomId") Integer roomId){
        roomService.deleteRoom(roomId);
    }

    @PutMapping(path = "{roomId}")
    public void updateRoom(@PathVariable("roomId") Integer roomId, @RequestParam(required = false) String name){
        roomService.updateRoom(roomId, name);
    }
}
