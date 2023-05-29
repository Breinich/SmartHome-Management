package com.itcatcetc.smarthome.sensor.data;

import com.itcatcetc.smarthome.sensor.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * SensorDataRepository
 * interface responsible for data access layer
 * specifying the domain type as SensorData and the id type as Integer
 */
@Repository
public interface SensorDataRepository extends JpaRepository<SensorData, Integer> {

    Optional<SensorData> findByDataId(Integer dataId);

    List<SensorData> findAllBySensor(Sensor sensor);

    Optional<SensorData> findTopBySensorOrderByTimestampDesc(Sensor sensor);
}
