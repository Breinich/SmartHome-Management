package com.itcatcetc.smarthome.actuator.command;

import com.itcatcetc.smarthome.type.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * CommandDataRepository
 * interface responsible for data access layer
 * specifying the domain type as ActuatorCommand and the id type as Integer
 */
@Repository
public interface CommandDataRepository extends JpaRepository<ActuatorCommand, Integer> {

    Optional<ActuatorCommand> findByCommandId(Integer commandId);

    List<ActuatorCommand> findAllByRoom_RoomIdAndConsequenceTypeAndExpirationDateIsNullOrExpirationDateAfter(Integer roomId, Type consequenceType, Date expirationDate);
}
