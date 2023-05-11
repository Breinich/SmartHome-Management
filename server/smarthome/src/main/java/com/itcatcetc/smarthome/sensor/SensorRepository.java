package com.itcatcetc.smarthome.sensor;

import com.itcatcetc.smarthome.room.Room;
import com.itcatcetc.smarthome.sensor.data.SensorData;
import com.itcatcetc.smarthome.type.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, Integer> {

    @Query("SELECT s FROM Sensor s WHERE s.name = ?1")
    Optional<Sensor> findSensorByName(String name);

    List<Sensor> findAllByRoomAndType(Room room, Type type);

}
