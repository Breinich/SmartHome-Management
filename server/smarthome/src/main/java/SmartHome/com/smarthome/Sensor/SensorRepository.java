package SmartHome.com.smarthome.Sensor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, Integer> {

    @Query("SELECT s FROM Sensor s WHERE s.name = ?1")
    Optional<Sensor> findSensorByName(String name);

    @Query("SELECT s FROM SensorData s WHERE s.sensor = ?1")
    List<Sensor> findSensorDatasBySensorId(Integer sensorId);


}
