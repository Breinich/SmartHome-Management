package SmartHome.com.smarthome.SensorData;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SensorDataRepository extends JpaRepository<SensorData, Integer> {

    @Query("SELECT s FROM SensorData s WHERE s.dataId = ?1")
    Optional<SensorData> findDataById(Integer dataId);
}
