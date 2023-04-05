package SmartHome.com.smarthome.Data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DataRepository extends JpaRepository<Data, Integer> {

    @Query("SELECT s FROM Data s WHERE s.dataId = ?1")
    Optional<Data> findDataById(Integer dataId);
}
