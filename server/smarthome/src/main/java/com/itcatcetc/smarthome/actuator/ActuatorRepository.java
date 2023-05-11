package com.itcatcetc.smarthome.actuator;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ActuatorRepository extends JpaRepository<Actuator, Integer> {

    @Query("SELECT a FROM Actuator a WHERE a.name = ?1")
    Optional<Actuator> findActuatorByName(String name);

    /*@Query("SELECT s FROM SensorData s WHERE s.sensor = ?1")
    List<Actuator> findSensorDatasBySensorId(Integer sensorId);//actuatorcommand*/


}
