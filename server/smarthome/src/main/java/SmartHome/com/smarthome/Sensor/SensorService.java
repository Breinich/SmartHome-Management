package SmartHome.com.smarthome.Sensor;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class SensorService {

    private final SensorRepository sensorRepository;

    @Autowired
    public SensorService(SensorRepository sensorRepository) {
        this.sensorRepository = sensorRepository;
    }

    public List<Sensor> getSmartHomeDevices(){
        return sensorRepository.findAll();
    }

    public void addNewSensor(Sensor sensor) {
       Optional<Sensor> sensorOptional= sensorRepository.findSensorByName(sensor.getName());
       if(sensorOptional.isPresent()) {
                    throw new IllegalStateException("name taken");
       }

       sensorRepository.save(sensor);

    }

    public void deleteSensor(Integer sensorId) {
        boolean exists = sensorRepository.existsById(sensorId);
        if(!exists){
            throw new IllegalStateException("sensor with id " + sensorId + " does not exists");
        }
        sensorRepository.deleteById(sensorId);
    }


    @Transactional
    public void updateSensor(Integer sensorId, String name, String room) {
        Sensor sensor = sensorRepository.findById(sensorId).orElseThrow(() -> new IllegalStateException("sensor with id " + sensorId + " does not exists"));

        if(name!= null && name.length() > 0 && !Objects.equals(sensor.getName(), name)){
            Optional<Sensor> sensorOptional= sensorRepository.findSensorByName(name);
            if(sensorOptional.isPresent()) {
                throw new IllegalStateException("name taken");
            }
            sensor.setName(name);
        }

        if(room!= null && room.length() > 0 && !Objects.equals(sensor.getRoom(), room)){
            sensor.setRoom(room);
        }


    }
}
