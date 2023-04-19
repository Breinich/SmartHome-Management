package SmartHome.com.smarthome.Room;

import SmartHome.com.smarthome.Sensor.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer>, JpaSpecificationExecutor<Room> {

    @Query("SELECT s FROM Room s WHERE s.name = ?1")
    Optional<Room> findRoomByName(String name);


}
