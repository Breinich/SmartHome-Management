package com.itcatcetc.smarthome.actuator;

import com.itcatcetc.smarthome.room.Room;
import com.itcatcetc.smarthome.type.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ActuatorRepository extends JpaRepository<Actuator, Integer> {

    Optional<Actuator> findByName(String name);

    List<Actuator> findAllByRoomAndType(Room room, Type type);

}
