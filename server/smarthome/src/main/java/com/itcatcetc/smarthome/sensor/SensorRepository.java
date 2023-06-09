package com.itcatcetc.smarthome.sensor;

import com.itcatcetc.smarthome.room.Room;
import com.itcatcetc.smarthome.type.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * a repository class for sensor
 * interface responsible for data access layer
 * specifying the domain type as Sensor and the id type as Integer
 */
@Repository
public interface SensorRepository extends JpaRepository<Sensor, Integer> {

    Optional<Sensor> findByName(String name);

    List<Sensor> findAllByRoomAndType(Room room, Type type);

}
