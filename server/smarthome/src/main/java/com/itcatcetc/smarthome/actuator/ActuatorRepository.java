package com.itcatcetc.smarthome.actuator;

import com.itcatcetc.smarthome.room.Room;
import com.itcatcetc.smarthome.type.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Actuator repository
 */
@Repository
public interface ActuatorRepository extends JpaRepository<Actuator, Integer> {

    /**
     * Find actuator by name
     * @param name actuator name
     * @return actuator
     */
    Optional<Actuator> findByName(String name);

    /**
     * Find all rooms by type
     * @param room room
     * @return actuator list
     */
    List<Actuator> findAllByRoomAndType(Room room, Type type);

}
