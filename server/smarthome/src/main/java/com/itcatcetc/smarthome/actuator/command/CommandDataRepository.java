package com.itcatcetc.smarthome.actuator.command;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommandDataRepository extends JpaRepository<ActuatorCommand, Integer> {

    Optional<ActuatorCommand> findByCommandId(Integer commandId);
}
