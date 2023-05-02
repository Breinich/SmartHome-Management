package SmartHome.com.smarthome.Sensor;

import SmartHome.com.smarthome.SensorData.SensorData;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class SensorSpecifications {
    public static Specification<Sensor> hasName(String name) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.<String>get("name"), "%" + name + "%");
    }

    public static Specification<Sensor> hasSensorWithId(Integer sensorDataId) {
        return (root, query, criteriaBuilder) -> {
            Join<SensorData, Sensor> sensorSensorData = root.join("sensorDataSet");
            return criteriaBuilder.equal(sensorSensorData.get("id"), sensorDataId);
        };
    }
}
