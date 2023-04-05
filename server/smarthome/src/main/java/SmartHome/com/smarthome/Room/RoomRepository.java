package SmartHome.com.smarthome.Room;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {

    @Query("SELECT s FROM Sensor s WHERE s.name = ?1")
    Optional<Room> findRoomByName(String name);
}
