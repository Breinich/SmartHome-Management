package com.itcatcetc.smarthome.sensor;



import com.itcatcetc.smarthome.sensor.data.SensorData;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Join;

public class SensorSpecifications {
    public static Specification<Sensor> hasName(String name) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("name"), "%" + name + "%");
    }

    public static Specification<Sensor> hasSensorWithId(Integer sensorDataId) {
        return (root, query, criteriaBuilder) -> {
            Join<SensorData, Sensor> sensorSensorData = root.join("sensorDataSet");
            return criteriaBuilder.equal(sensorSensorData.get("id"), sensorDataId);
        };
    }
}
