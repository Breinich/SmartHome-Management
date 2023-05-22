package com.itcatcetc.smarthome.room;

import com.itcatcetc.smarthome.actuator.Actuator;
import com.itcatcetc.smarthome.sensor.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer>, JpaSpecificationExecutor<Room> {

    @Query("SELECT s FROM Room s WHERE s.name = ?1")
    Optional<Room> findRoomByName(String name);

    @Query("SELECT s FROM Sensor s WHERE s.room.roomId = ?1")
    List<Sensor> findSensorsByRoomId(Integer roomId);

    @Query("SELECT a FROM Actuator a WHERE a.room.roomId = ?1")
    List<Actuator> findActuatorsByRoomId(Integer roomId);

}
