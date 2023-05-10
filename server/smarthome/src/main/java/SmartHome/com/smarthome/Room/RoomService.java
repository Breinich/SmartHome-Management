package SmartHome.com.smarthome.Room;

import SmartHome.com.smarthome.Actuator.Actuator;
import SmartHome.com.smarthome.Sensor.Sensor;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    @Autowired
    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public List<Room> getRooms(){
        return roomRepository.findAll();
    }

    public void addNewRoom(Room room) {
        Optional<Room> roomOptional= roomRepository.findRoomByName(room.getName());
        if(roomOptional.isPresent()) {
            throw new IllegalStateException("name taken");
        }

        roomRepository.save(room);
    }

    public void deleteRoom(Integer roomId) {
        boolean exists = roomRepository.existsById(roomId);
        if(!exists){
            throw new IllegalStateException("Room with id " + roomId + " does not exists");
        }
        roomRepository.deleteById(roomId);
    }


    @Transactional
    public void updateRoom(Integer roomId, String name) {
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new IllegalStateException("room with id " + roomId + " does not exists"));

        if(name != null && name.length() > 0 && !Objects.equals(room.getName(), name)){
            Optional<Room> roomOptional= roomRepository.findRoomByName(name);
            if(roomOptional.isPresent()) {
                throw new IllegalStateException("name taken");
            }
            room.setName(name);
        }
    }

    public List<Sensor> getSensorsByRoomId(Integer roomId) {
        return roomRepository.findSensorsByRoomId(roomId);
    }
    public List<Actuator> getActuatorsByRoomId(Integer roomId) {
        return roomRepository.findActuatorsByRoomId(roomId);
    }
}
