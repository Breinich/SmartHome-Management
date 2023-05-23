package com.itcatcetc.smarthome.room;


import com.itcatcetc.smarthome.actuator.Actuator;
import com.itcatcetc.smarthome.sensor.Sensor;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@PreAuthorize("hasRole('GUEST') or hasRole('HOMIE')")
public class RoomService {

    private final RoomRepository roomRepository;

    @Autowired
    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public List<Room> getRooms() {
        return roomRepository.findAll();
    }

    public void addNewRoom(Room room) {
        Optional<Room> roomOptional = roomRepository.findRoomByName(room.getName());
        if (roomOptional.isPresent()) {
            throw new IllegalArgumentException("name taken");
        }

        roomRepository.save(room);
    }

    public void deleteRoom(Integer roomId) {
        boolean exists = roomRepository.existsById(roomId);
        if (!exists) {
            throw new IllegalArgumentException("Room with id " + roomId + " does not exists");
        }
        roomRepository.deleteById(roomId);
    }


    @Transactional
    public void updateRoom(Room room) {
        Room oldRoom = roomRepository.findById(room.getRoomId()).orElseThrow(() -> new IllegalArgumentException("room with id " + room.getRoomId() + " does not exists"));

        if (room.getName() != null && room.getName().length() > 0 && !Objects.equals(room.getName(), oldRoom.getName())) {
            Optional<Room> roomOptional = roomRepository.findRoomByName(room.getName());
            if (roomOptional.isPresent()) {
                throw new IllegalArgumentException("name taken");
            }
            oldRoom.setName(room.getName());
        }

        if (room.getCoverPhoto() != null && room.getCoverPhoto().length() > 0 && !Objects.equals(room.getCoverPhoto(),
                oldRoom.getCoverPhoto())) {

            oldRoom.setCoverPhoto(room.getCoverPhoto());
        }
    }

    public List<Sensor> getSensorsByRoomId(Integer roomId) {
        return roomRepository.findSensorsByRoomId(roomId);
    }

    public List<Actuator> getActuatorsByRoomId(Integer roomId) {
        return roomRepository.findActuatorsByRoomId(roomId);
    }
}
