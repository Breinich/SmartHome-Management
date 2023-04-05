package SmartHome.com.smarthome.Room;

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
        Optional<Room> sensorOptional= roomRepository.findRoomByName(room.getName());
        if(sensorOptional.isPresent()) {
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

        if(name!= null && name.length() > 0 && !Objects.equals(room.getName(), name)){
            Optional<Room> roomOptional= roomRepository.findRoomByName(name);
            if(roomOptional.isPresent()) {
                throw new IllegalStateException("name taken");
            }
            room.setName(name);
        }

    }
}
