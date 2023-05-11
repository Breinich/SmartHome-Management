package com.itcatcetc.smarthome.sensor.data;

import com.itcatcetc.smarthome.sensor.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SensorDataRepository extends JpaRepository<SensorData, Integer> {

    Optional<SensorData> findByDataId(Integer dataId);
    List<SensorData> findAllBySensor(Sensor sensor);
    Optional<SensorData> findTopBySensorOrderByTimestampDesc(Sensor sensor);
}
