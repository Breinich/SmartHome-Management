package com.itcatcetc.smarthome.room;

import com.itcatcetc.smarthome.actuator.Actuator;
import com.itcatcetc.smarthome.sensor.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * RoomRepository
 * interface responsible for data access layer
 * specifying the domain type as Room and the id type as Integer
 */
@Repository
public interface RoomRepository extends JpaRepository<Room, Integer>, JpaSpecificationExecutor<Room> {

    /**
     * find room by name
     * @param name the name of the room
     * @return  a room with the name
     */
    @Query("SELECT s FROM Room s WHERE s.name = ?1")
    Optional<Room> findRoomByName(String name);

    /**
     * find sensors by room id
     * @param roomId the id of the room
     * @return a list of sensors in the room
     */
    @Query("SELECT s FROM Sensor s WHERE s.room.roomId = ?1")
    List<Sensor> findSensorsByRoomId(Integer roomId);

    /**
     * find actuators by room id
     * @param roomId the id of the room
     * @return a list of actuators in the room
     */
    @Query("SELECT a FROM Actuator a WHERE a.room.roomId = ?1")
    List<Actuator> findActuatorsByRoomId(Integer roomId);

}
