package com.itcatcetc.smarthome.room;


import com.itcatcetc.smarthome.sensor.Sensor;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class RoomSpecifications {
    public static Specification<Room> hasName(String name) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("name"), "%" + name + "%");
    }

    public static Specification<Room> hasSensorWithId(Integer sensorId) {
        return (root, query, criteriaBuilder) -> {
            Join<Sensor, Room> roomsSensor = root.join("sensorSet");
            return criteriaBuilder.equal(roomsSensor.get("id"), sensorId);
        };
    }

    public static Specification<Room> hasActuatorWithId(Integer actuatorId) {
        return (root, query, criteriaBuilder) -> {
            Join<Sensor, Room> roomsActuator = root.join("actuatorSet");
            return criteriaBuilder.equal(roomsActuator.get("id"), actuatorId);
        };
    }
}
