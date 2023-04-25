package SmartHome.com.smarthome.Room;

import SmartHome.com.smarthome.Sensor.Sensor;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class RoomSpecifications {
    public static Specification<Room> hasName(String name) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.<String>get("name"), "%" + name + "%");
    }

    public static Specification<Room> hasSensorWithId(String sensorId) {
        return (root, query, criteriaBuilder) -> {
            Join<Sensor, Room> roomSensor = root.join("sensorSet");
            return criteriaBuilder.equal(roomSensor.get("id"), sensorId);
        };
    }
}