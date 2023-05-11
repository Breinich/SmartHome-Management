package com.itcatcetc.smarthome.actuator.command;

import com.itcatcetc.smarthome.sensor.data.SensorData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommandDataRepository extends JpaRepository<ActuatorCommand, Integer> {

    @Query("SELECT c FROM ActuatorCommand c WHERE c.dataId = ?1")
    Optional<ActuatorCommand> findDataById(Integer dataId);
}
